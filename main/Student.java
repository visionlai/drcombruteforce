package main;

/**
 * @author vision
 * 学生信息
 */
public class Student {
    // 个人信息
    private String number;
    private String name;
    private String gender;
    private String sclass;
    private String idcard;

    // 上网信息
    private String account;
    private String password;
    private String ISP;
    private String bandwidth;
    private String status;
    private String overdate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getISP() {
        return ISP;
    }

    public void setISP(String ISP) {
        this.ISP = ISP;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOverdate() {
        return overdate;
    }

    public void setOverdate(String overdate) {
        this.overdate = overdate;
    }
}
