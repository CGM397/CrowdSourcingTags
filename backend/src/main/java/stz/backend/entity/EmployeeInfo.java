package stz.backend.entity;

/**
 * 存储格式：data/user/employee/employeeId.txt
 * 文件格式：
 * 0. name
 * 1. password
 * 2. email
 * 3. dpId
 * 4. award amount
 * 5. task amount
 * 6. underway
 * 7. completed
 */
public class EmployeeInfo {

    /**
     * 昵称
     */
    String employeeName;

    /**
     * 编号，唯一，由用户输入，需要检测是否唯一
     */
    String employeeId;

    /**
     * 密码，可以规定长度
     */
    String employeePassword;

    /**
     * 用户邮箱
     */
    String employeeEmail;

    /**
     * 头像id
     */
    String dpId;

    /**
     * 获得的总奖励
     */
    int awardAmount;

    /**
     * 已经接收的任务数(进行中加已完成)
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

    AnnotationJudgement judgement;

    int reportedTimes = 0;

    public EmployeeInfo(){}

    public EmployeeInfo(String employeeName, String employeeId, String employeePassword,
                        String employeeEmail, String dpId, int awardAmount, int taskAmount,
                        int taskUnderway, int taskCompleted, AnnotationJudgement judgement, int reportedTimes){
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.employeePassword = employeePassword;
        this.employeeEmail = employeeEmail;
        this.dpId = dpId;
        this.awardAmount = awardAmount;
        this.taskAmount = taskAmount;
        this.taskUnderway = taskUnderway;
        this.taskCompleted = taskCompleted;
        this.judgement = judgement;
        this.reportedTimes = reportedTimes;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getDpId() {
        return dpId;
    }

    public int getAwardAmount() {
        return awardAmount;
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

    public AnnotationJudgement getJudgement() {
        return judgement;
    }

    public int getReportedTimes() { return reportedTimes; }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public void setAwardAmount(int awardAmount) {
        this.awardAmount = awardAmount;
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

    public void setJudgement(AnnotationJudgement judgement) {
        this.judgement = judgement;
    }

    public void setReportedTimes(int reportedTimes) { this.reportedTimes = reportedTimes; }
}
