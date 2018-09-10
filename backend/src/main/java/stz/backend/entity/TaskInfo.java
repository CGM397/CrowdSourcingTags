package stz.backend.entity;

import java.util.ArrayList;

public class TaskInfo {
    ArrayList<Picture> pictureCode;
    TaskToGet task;

    public TaskInfo(){}

    public ArrayList<Picture> getPictureCode() {
        return pictureCode;
    }

    public TaskToGet getTask() {
        return task;
    }
}
