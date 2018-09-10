package stz.backend.entity;

import java.util.ArrayList;

/**
 * 存储格式：data/task/employer/employerId/taskId.txt
 * 0: needed num
 * 1: accepted num
 * 2: employee id: string[0]...
 * 3: completed num
 * 4: employee id: string[0]...
 */
public class EmployerTaskInfo {

    String employerId;

    String taskId;

    /**
     * 需要的众包工人数
     */
    int neededEmployeeNum;

    int acceptedEmployeeNum;

    int completedEmployeeNum;

    /**
     * 已接收任务的参与者，当有参与者放弃任务时，要自动减去相应数量的人数
     */
    ArrayList<String> acceptedEmployeeId;

    /**
     * 已提交结果的参与者
     */
    ArrayList<String> completedEmployeeId;

    /**
     * 众包发起者:这个task所需要的参与者都已经提交结果算这个task完成
     */
    boolean isFinished;

    public EmployerTaskInfo(){}

    public EmployerTaskInfo(String employerId, String taskId, int neededEmployeeNum,
                            int acceptedEmployeeNum, int completedEmployeeNum,
                            ArrayList<String> acceptedEmployeeId,
                            ArrayList<String> completedEmployeeId,
                            boolean isFinished){
        this.employerId = employerId;
        this.taskId = taskId;
        this.neededEmployeeNum = neededEmployeeNum;
        this.acceptedEmployeeNum = acceptedEmployeeNum;
        this.completedEmployeeNum = completedEmployeeNum;
        this.acceptedEmployeeId = acceptedEmployeeId;
        this.completedEmployeeId = completedEmployeeId;
        this.isFinished = isFinished;
    }

    public String getEmployerId() {
        return employerId;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getNeededEmployeeNum() {
        return neededEmployeeNum;
    }

    public int getAcceptedEmployeeNum() {
        return acceptedEmployeeNum;
    }

    public int getCompletedEmployeeNum() {
        return completedEmployeeNum;
    }

    public ArrayList<String> getAcceptedEmployeeId() {
        return acceptedEmployeeId;
    }

    public ArrayList<String> getCompletedEmployeeId() {
        return completedEmployeeId;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setNeededEmployeeNum(int neededEmployeeNum) {
        this.neededEmployeeNum = neededEmployeeNum;
    }

    public void setAcceptedEmployeeNum(int acceptedEmployeeNum) {
        this.acceptedEmployeeNum = acceptedEmployeeNum;
    }

    public void setCompletedEmployeeNum(int completedEmployeeNum) {
        this.completedEmployeeNum = completedEmployeeNum;
    }

    public void setAcceptedEmployeeId(ArrayList<String> acceptedEmployeeId) {
        this.acceptedEmployeeId = acceptedEmployeeId;
    }

    public void setCompletedEmployeeId(ArrayList<String> completedEmployeeId) {
        this.completedEmployeeId = completedEmployeeId;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }
}
