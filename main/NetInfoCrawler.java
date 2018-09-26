package main;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.Connection.Method;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vision
 * 爬取学生班里的网络套餐信息
 */
public class NetInfoCrawler {

    private static Pattern findCheckCodeRegex = Pattern.compile("checkcode=\"(\\d*)\"");

    /**
     * 解析 JSON 型数据 netInfo，将套餐信息存入 studnet 中
     * @param netInfo 套餐信息
     * @param student 学生
     */
    private static void handleNetInfoJSON(String netInfo, Student student) throws JSONException {
        // 提取信息
        JSONObject netInfoJSON = (JSONObject) JSONObject.fromObject(netInfo).get("note");
        String[] msg = netInfoJSON.get("overdate").toString().split("日期为");
        String overdate = msg.length == 2 ? msg[1] : null;
        String[] service = netInfoJSON.get("service").toString().split(" ");
        String ISP = service[0];
        String bandwidth = service.length == 2 ? service[1] : null;
        String status = netInfoJSON.get("status").toString();

        // 写入信息
        student.setOverdate(overdate);
        student.setISP(ISP);
        student.setBandwidth(bandwidth);
        student.setStatus(status);
    }

    /**
     * 使用 student 中的用户名和密码爬取套餐信息
     * @param student 学生
     */
    public static void crawlNetInfo(Student student) throws IOException, JSONException {
        // 提取用户名和密码
        String account = student.getAccount();
        String password = student.getPassword();

        // ---------------------- 爬取套餐信息 ---------------------
        // 访问初始页面，爬取验证码
        Connection con = Jsoup.connect("http://172.30.254.5/self/nav_login");
        con.header("User-Agent", "Mozilla/5.0");

        Response response = con.method(Method.GET).execute();
        Map<String, String> cookies = response.cookies();

        // 获取 cookie 中 sessionid
        String sessionId = cookies.get("JSESSIONID");

        // 获取验证码
        String body = response.body();
        Matcher m = findCheckCodeRegex.matcher(body);
        String checkcode = null;
        if (m.find()) {
            checkcode = m.group(1);
        }

        // 访问验证码页面
        con = Jsoup.connect("http://172.30.254.5/self/RandomCodeAction.action?randomNum=0.7372450537630908");
        con.header("User-Agent", "Mozilla/5.0");
        con.cookie("JSESSIONID", sessionId);
        con.ignoreContentType(true).get();

        // 登入账号
        con = Jsoup.connect("http://172.30.254.5/self/LoginAction.action");
        con.header("User-Agent", "Mozilla/5.0");
        con.header("Content-Type", "application/x-www-form-urlencoded");
        con.cookie("JSESSIONID", sessionId);
        // 配置 post 的数据
        con.data("account", account);
        con.data("password", password);
        con.data("code", "");
        con.data("checkcode", checkcode);
        con.data("Submit", "Login");
        con.post();

        // 访问获取网络套餐json数据
        con = Jsoup.connect("http://172.30.254.5/self/refreshaccount?t=0.6020702642121645");
        con.header("User-Agent", "Mozilla/5.0");
        con.cookie("JSESSIONID", sessionId);
        body = con.ignoreContentType(true).get().body().text();

        // 处理 JSON 数据
        handleNetInfoJSON(body, student);
    }
}
