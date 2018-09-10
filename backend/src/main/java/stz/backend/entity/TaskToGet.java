package stz.backend.entity;

import java.util.ArrayList;

public class TaskToGet {
    boolean isFull;
    String taskId;
    String taskName;
    ArrayList<String> taskType;
    String employerId;
    String releaseType;
    String startTime;
    String limitTime;
    int neededEmployeeAmount;
    int totalPictureAmount;
    int award;
    ArrayList<String> overallPictureId;
    ArrayList<String> rectanglePictureId;
    ArrayList<String> boundaryPictureId;
    ArrayList<Integer> pictureAcceptedTimes;
    ArrayList<String> appointedEmployeeId;
    String marks;
    public TaskToGet(){}
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

    public String getReleaseType() {
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
}
