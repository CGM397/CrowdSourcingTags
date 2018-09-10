package stz.backend.entity;

/**
 * 存储格式：data/user/employer/employerId.txt
 * 文件格式：
 * 0. name
 * 1. password
 * 2. email
 * 3. dpId
 * 4. task amount
 * 5. underway
 * 6. completed
 */
public class EmployerInfo {
    /**
     * 昵称
     */
    String employerName;

    /**
     * 编号，唯一，由用户输入，需要检测是否唯一
     */
    String employerId;

    /**
     * 密码，可以规定长度
     */
    String employerPassword;

    /**
     * 用户邮箱
     */
    String employerEmail;

    /**
     * 头像id
     */
    String dpId;

    /**
     * 已经发布的任务数(进行中加已完成)
     */
    int taskAmount = 0;

    /**
     * 仍在进行中的任务数
     */
    int taskUnderway = 0;

    /**
     * 已经完成的任务数
     */
    int taskCompleted = 0;

    public EmployerInfo(){}

    public EmployerInfo(String employerName, String employerId, String employerPassword,
                        String employerEmail, String dpId, int taskAmount, int taskUnderway,
                        int taskCompleted){
        this.employerName = employerName;
        this.employerId = employerId;
        this.employerPassword = employerPassword;
        this.employerEmail = employerEmail;
        this.dpId = dpId;
        this.taskAmount = taskAmount;
        this.taskUnderway = taskUnderway;
        this.taskCompleted = taskCompleted;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerId() {
        return employerId;
    }

    public String getEmployerPassword() {
        return employerPassword;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public String getDpId() {
        return dpId;
    }

    public int getTaskAmount() {
        return taskAmount;
    }

    public int getTaskUnderway() {
        return taskUnderway;
    }

    public int getTaskCompleted() {
        return taskCompleted;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public void setEmployerPassword(String employerPassword) {
        this.employerPassword = employerPassword;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public void setTaskAmount(int taskAmount) {
        this.taskAmount = taskAmount;
    }

    public void setTaskUnderway(int taskUnderway) {
        this.taskUnderway = taskUnderway;
    }

    public void setTaskCompleted(int taskCompleted) {
        this.taskCompleted = taskCompleted;
    }
}
