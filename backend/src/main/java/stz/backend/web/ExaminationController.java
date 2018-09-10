package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.ExamToSend;
import stz.backend.entity.Examination;
import stz.backend.entity.PictureTag;
import stz.backend.service.ExaminationService;
import stz.backend.serviceImpl.ExaminationImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/Examination")
public class ExaminationController implements ExaminationService {
    private ExaminationImpl examination = new ExaminationImpl();

    @Override
    @RequestMapping(value = "/getExam", method = RequestMethod.POST)
    @ResponseBody
    public Examination getExam(@RequestParam String examId) {
        return examination.getExam(examId);
    }

    @RequestMapping(value = "/judgeExam", method = RequestMethod.POST)
    @ResponseBody
    public double judge(@RequestBody ExamToSend exam){
        return judgeExam(exam.getEmployeeId(), exam.getExamId(), exam.getPaper(), exam.getTime());
    }

    @Override
    public double judgeExam(String employeeId, String examId, ArrayList<PictureTag> paper, int time) {
        return examination.judgeExam(employeeId,examId,paper,time);
    }
}
