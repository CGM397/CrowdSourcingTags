package stz.backend.entity;

import java.util.ArrayList;

/**
 * 存储格式：data/picturetag/employeeId/pictureID/tagID.txt
 * 文件格式：
 * 0~i-1. border coordinate(x+" "+y)
 * i. ##BORDER_END##
 * i+1. annotation
 */

public class PictureTag {
    String pictureID;

    String tagID;

    ArrayList<Coordinate> border;

    String annotation;

    public PictureTag(){

    }

    public PictureTag(String pictureID, String tagID, ArrayList<Coordinate> border, String annotation) {
        this.pictureID = pictureID;
        this.tagID = tagID;
        this.border = border;
        this.annotation = annotation;
    }

    public String getPictureID(){
        return pictureID;
    }

    public String getTagID() { return tagID; }

    public ArrayList<Coordinate> getBorder() { return border; }

    public String getAnnotation() { return annotation; }

    public void setPictureID(String pictureID){
        this.pictureID = pictureID;
    }

    public void setTagID(String tagID) { this.tagID = tagID; }

    public void setBorder(ArrayList<Coordinate> border) { this.border = border; }

    public void setAnnotation(String annotation) { this.annotation = annotation; }
}
