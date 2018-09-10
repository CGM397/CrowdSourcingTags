package stz.backend.entity;

import java.util.ArrayList;

/**
 * 存储格式：data/task/employee/employeeId/taskId.txt
 * 0. accept time
 * 1. finish time
 * 2. completed num
 * 3. total num
 * 4. isFinished:t/f
 * 5~end. completed picture id
 */

public class EmployeeTaskInfo {

    String employeeId;

    String taskId;

    /**
     * 众包工人点击select时填写acceptTime
     */
    String acceptTime;

    /**
     * 众包工人点击submit时填写finishTime，之前(包括初始时)为空
     */
    String finishTime;

    int completedPictureNum;

    int totalPictureNum;

    ArrayList<String> completedPictureId;

    /**
     * 本次任务所选择的所有图片Id
     */
    ArrayList<String> selectedPictureId;

    /**
     * 众包工人:标注完所有图片并提交算这个task完成
     */
    boolean isFinished;

    /**
     * 是否已经领取奖励
     */
    boolean receiveAward;

    /**
     * 本次任务完成后的一个评价
     */
    AnnotationJudgement judgement;

    boolean reported = false;

    public EmployeeTaskInfo(){}

    public EmployeeTaskInfo(String employeeId, String taskId, String acceptTime, String finishTime,
                            int completedPictureNum,
                            int totalPictureNum, ArrayList<String> completedPictureId,
                            ArrayList<String> selectedPictureId,
                            boolean isFinished,boolean receiveAward,AnnotationJudgement judgement, boolean reported){
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.acceptTime = acceptTime;
        this.finishTime = finishTime;
        this.completedPictureNum = completedPictureNum;
        this.totalPictureNum = totalPictureNum;
        this.completedPictureId = completedPictureId;
        this.selectedPictureId = selectedPictureId;
        this.isFinished = isFinished;
        this.receiveAward = receiveAward;
        this.judgement = judgement;
        this.reported = reported;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public int getCompletedPictureNum() {
        return completedPictureNum;
    }

    public int getTotalPictureNum() {
        return totalPictureNum;
    }

    public ArrayList<String> getCompletedPictureId() {
        return completedPictureId;
    }

    public ArrayList<String> getSelectedPictureId() {
        return selectedPictureId;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public boolean getReceiveAward() {
        return receiveAward;
    }

    public AnnotationJudgement getJudgement() {
        return judgement;
    }

    public boolean getReported() { return reported; }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setCompletedPictureNum(int completedPictureNum) {
        this.completedPictureNum = completedPictureNum;
    }

    public void setTotalPictureNum(int totalPictureNum) {
        this.totalPictureNum = totalPictureNum;
    }

    public void setCompletedPictureId(ArrayList<String> completedPictureId) {
        this.completedPictureId = completedPictureId;
    }

    public void setSelectedPictureId(ArrayList<String> selectedPictureId) {
        this.selectedPictureId = selectedPictureId;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }

    public void setReceiveAward(boolean receiveAward) {
        this.receiveAward = receiveAward;
    }

    public void setJudgement(AnnotationJudgement judgement) {
        this.judgement = judgement;
    }

    public void setReported(boolean reported) { this.reported = reported; }
}
