package stz.backend.entity;

import java.util.ArrayList;

public class Examination {
    String examId; // 考题id

    ArrayList<String> overallPictureId; // 图片id

    ArrayList<PictureTag> answer; // 考题答案
    // border里分别为左上和右下点
    // annotation用“/”分割关键词

    public Examination(){}

    public Examination(String examId, ArrayList<String> overallPictureId, ArrayList<PictureTag> answer) {
        this.examId = examId;
        this.overallPictureId = overallPictureId;
        this.answer = answer;
    }

    public String getExamId() { return examId; }

    public ArrayList<String> getOverallPictureId() { return overallPictureId; }

    public ArrayList<PictureTag> getAnswer() { return answer; }

    public void setExamId(String examId) { this.examId = examId; }

    public void setOverallPictureId(ArrayList<String> overallPictureId) { this.overallPictureId = overallPictureId; }

    public void setAnswer(ArrayList<PictureTag> answer) { this.answer = answer; }
}
