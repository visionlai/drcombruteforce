package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author vision
 * 获取一个数据库的连接
 */
public class DBConnection {
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/szu";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DBDRIVER);
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        return con;
    }
}
