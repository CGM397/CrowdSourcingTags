package stz.backend.service;

import stz.backend.entity.Examination;
import stz.backend.entity.PictureTag;

import java.util.ArrayList;

public interface ExaminationService {
    public Examination getExam(String examId);

    public double judgeExam(String employeeId, String examId, ArrayList<PictureTag> paper, int time);
}
