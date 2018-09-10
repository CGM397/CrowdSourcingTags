package stz.backend.entity;

import java.util.ArrayList;

public class ExamToSend {
    String employeeId;
    String examId;
    ArrayList<PictureTag> paper;
    int time;
    public ExamToSend(){}
    public ExamToSend(String employeeId, String examId, ArrayList<PictureTag> paper, int time){
        this.employeeId = employeeId;
        this.examId = examId;
        this.paper = paper;
        this.time = time;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getExamId() {
        return examId;
    }

    public ArrayList<PictureTag> getPaper() {
        return paper;
    }

    public int getTime() {
        return time;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setPaper(ArrayList<PictureTag> paper) {
        this.paper = paper;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
