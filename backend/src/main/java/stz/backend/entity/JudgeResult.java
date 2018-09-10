package stz.backend.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 针对一个任务的一张图片的标注整合结果
 */
public class JudgeResult {

    String taskId;

    String pictureId;
    /**
     * 一张图片所有的tag
     */
    ArrayList<PictureTag> standardResult;

    /**
     * 工人Id+准确度得分
     */
    HashMap<String, Double> employeeScore;

    public JudgeResult(){}

    public JudgeResult(String taskId, String pictureId, ArrayList<PictureTag> standardResult, HashMap<String, Double> employeeScore){
        this.taskId = taskId;
        this.pictureId = pictureId;
        this.standardResult = standardResult;
        this.employeeScore = employeeScore;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getPictureId() {
        return pictureId;
    }

    public ArrayList<PictureTag> getStandardResult() {
        return standardResult;
    }

    public HashMap<String, Double> getEmployeeScore() {
        return employeeScore;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public void setStandardResult(ArrayList<PictureTag> standardResult) {
        this.standardResult = standardResult;
    }

    public void setEmployeeScore(HashMap<String, Double> employeeScore) {
        this.employeeScore = employeeScore;
    }
}
