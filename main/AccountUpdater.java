package main;

import net.sf.json.JSONException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author vision
 * 更新账号信息
 */
public class AccountUpdater {

    public static void main(String[] args) throws ClassNotFoundException {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement queryStatement = con.prepareStatement("SELECT account, password FROM account");
            PreparedStatement updateStatement = con.prepareStatement("UPDATE account SET ISP=?, bandwidth=?, status=?, overdate=? WHERE account=?");
            ResultSet result = queryStatement.executeQuery();

            // 查询获取账号和密码
            while (result.next()) {
                String account = result.getString("account");
                String password = result.getString("password");

                Student student = new Student();
                student.setAccount(account);
                student.setPassword(password);

                // 获取网络套餐信息
                try {
                    NetInfoCrawler.crawlNetInfo(student);
                } catch (IOException | JSONException e) {
                    System.out.println(e);
                    student.setStatus("异常");
                }

                // 更新套餐信息
                String ISP = student.getISP();
                String bandwidth = student.getBandwidth();
                String status = student.getStatus();
                String overdate = student.getOverdate();
                updateStatement.setString(1, ISP);
                updateStatement.setString(2, bandwidth);
                updateStatement.setString(3, status);
                updateStatement.setString(4, overdate);
                updateStatement.setString(5, account);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
