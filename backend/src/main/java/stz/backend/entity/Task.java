package stz.backend.entity;

import stz.backend.enums.ReleaseType;

import java.util.ArrayList;

/**
 * 存储格式：data/tasks/taskId.txt
 * 文件格式：
 * 0. isFull: t/f
 * 1. task name
 * 2. employer id
 * 3. release type
 * 4. start time
 * 5. limit time
 * 6. needed employee amount
 * 7. total picture amount
 * 8. award
 * 9. marks
 * 10. overall picture id: string[0]+" "+string[1]+" "+...
 * 11. rectangle picture id: ...
 * 12. boundary picture id: ...
 * 13. appointed employee id: ...
 */

public class Task {

    /**
     * 任务所需人数是否已满
     */
    boolean isFull;

    /**
     * 任务编号(唯一)
     */
    String taskId;

    /**
     * 任务名(不唯一)
     */
    String taskName;

    /**
     * 任务类型，最多5个
     */
    ArrayList<String> taskType;

    /**
     * 该任务对应的众包发起者的编号
     */
    String employerId;

    /**
     * 发布形式
     */
    ReleaseType releaseType;

    /**
     *任务创建时间
     */
    String startTime;

    /**
     * 限定时间
     */
    String limitTime;

    /**
     * 需要的众包工人数
     */
    int neededEmployeeAmount;

    /**
     * 总的图片数
     */
    int totalPictureAmount;

    /**
     * 积分奖励
     */
    int award;

    /**
     * 整体标注图片区
     */
    ArrayList<String> overallPictureId;

    /**
     * 方框标注图片区
     */
    ArrayList<String> rectanglePictureId;

    /**
     * 区域标注图片区
     */
    ArrayList<String> boundaryPictureId;

    /**
     * 图片被接收的次数，一般是1；分块发布的任务：每当一名工人接收此图片后，次数加一，从0开始
     */
    ArrayList<Integer> pictureAcceptedTimes;

    /**
     * 指定工人的编号
     */
    ArrayList<String> appointedEmployeeId;

    /**
     * 任务描述
     */
    String marks;

    public Task(){}
    public Task(boolean isFull, String taskId, String taskName,ArrayList<String> taskType,
                String employerId, ReleaseType releaseType,
                String startTime, String limitTime, int neededEmployeeAmount, int totalPictureAmount,
                int award, ArrayList<String> overallPictureId, ArrayList<String> rectanglePictureId,
                ArrayList<String> boundaryPictureId, ArrayList<Integer> pictureAcceptedTimes,
                ArrayList<String> appointedEmployeeId, String marks){
        this.isFull = isFull;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskType = taskType;
        this.employerId = employerId;
        this.releaseType = releaseType;
        this.startTime = startTime;
        this.limitTime = limitTime;
        this.neededEmployeeAmount = neededEmployeeAmount;
        this.totalPictureAmount = totalPictureAmount;
        this.award = award;
        this.overallPictureId = overallPictureId;
        this.rectanglePictureId = rectanglePictureId;
        this.boundaryPictureId = boundaryPictureId;
        this.pictureAcceptedTimes = pictureAcceptedTimes;
        this.appointedEmployeeId = appointedEmployeeId;
        this.marks = marks;
    }

    public boolean getIsFull() {
        return isFull;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public ArrayList<String> getTaskType() {
        return taskType;
    }

    public String getEmployerId() {
        return employerId;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public int getNeededEmployeeAmount() {
        return neededEmployeeAmount;
    }

    public int getTotalPictureAmount() {
        return totalPictureAmount;
    }

    public int getAward() {
        return award;
    }

    public ArrayList<String> getOverallPictureId() {
        return overallPictureId;
    }

    public ArrayList<String> getRectanglePictureId() {
        return rectanglePictureId;
    }

    public ArrayList<String> getBoundaryPictureId() {
        return boundaryPictureId;
    }

    public ArrayList<Integer> getPictureAcceptedTimes() {
        return pictureAcceptedTimes;
    }

    public ArrayList<String> getAppointedEmployeeId() {
        return appointedEmployeeId;
    }

    public String getMarks() {
        return marks;
    }

    public void setIsFull(boolean full) {
        this.isFull = full;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskType(ArrayList<String> taskType) {
        this.taskType = taskType;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public void setNeededEmployeeAmount(int neededEmployeeAmount) {
        this.neededEmployeeAmount = neededEmployeeAmount;
    }

    public void setTotalPictureAmount(int totalPicutreAmount) {
        this.totalPictureAmount = totalPicutreAmount;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public void setOverallPictureId(ArrayList<String> overallPictureId) {
        this.overallPictureId = overallPictureId;
    }

    public void setRectanglePictureId(ArrayList<String> rectanglePictureId) {
        this.rectanglePictureId = rectanglePictureId;
    }

    public void setBoundaryPictureId(ArrayList<String> boundaryPictureId) {
        this.boundaryPictureId = boundaryPictureId;
    }

    public void setPictureAcceptedTimes(ArrayList<Integer> pictureAcceptedTimes) {
        this.pictureAcceptedTimes = pictureAcceptedTimes;
    }

    public void setAppointedEmployeeId(ArrayList<String> appointedEmployeeId) {
        this.appointedEmployeeId = appointedEmployeeId;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
