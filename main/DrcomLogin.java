package main;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author vision
 * drcom 网页登录
 */
public class DrcomLogin {

    private static String drcomUrl = "https://drcom.szu.edu.cn/a70.htm";
    private static String successTitle = "Drcom PC登陆成功页";

    /**
     * 接受用户名和密码信息，返回登录成功标志
     */
    public static boolean isLoginSuccess(String account, String password){
        Connection con = Jsoup.connect(drcomUrl);
        con.header("User-Agent", "Mozilla/5.0");
        con.header("Content-Type", "application/x-www-form-urlencoded");

        // 发送用户名和密码
        con.data("DDDDD", account);
        con.data("upass", password);
        con.data("R1", "0");
        con.data("R2", "");
        con.data("R6", "0");
        con.data("para", "00");
        con.data("0MKKey", "123456");
        con.data("buttonClicked", "");
        con.data("redirect_url", "");
        con.data("err_flag", "");
        con.data("username", "");
        con.data("password", "");
        con.data("user", "");
        con.data("cmd", "");
        con.data("Login", "");
        con.data("R7", "0");

        try {
            Document doc = con.post();
            String title = doc.title().trim();
            return title.equals(successTitle);
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

}
