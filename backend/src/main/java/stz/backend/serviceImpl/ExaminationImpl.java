package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.Coordinate;
import stz.backend.entity.Examination;
import stz.backend.entity.PictureTag;
import stz.backend.service.ExaminationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ExaminationImpl implements ExaminationService {
    @Override
    public Examination getExam(String examId) {
        Examination result = new Examination();
        result.setExamId(examId);
        ArrayList<String> overallPictureId = new ArrayList<>();
        ArrayList<PictureTag> answer = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM examination WHERE examId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,examId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                overallPictureId.add(rs.getString("pictureId"));
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                if(rs.getString("border").length()>=1){
                    String[] pos = rs.getString("border").split(";");
                    for(int i = 0; i < pos.length; i++){
                        String[] temp = pos[i].split(",");
                        Coordinate store = new Coordinate(Integer.parseInt(temp[0]),
                                Integer.parseInt(temp[1]));
                        coordinates.add(store);
                    }
                }
                PictureTag tag = new PictureTag(rs.getString("pictureId"),
                        "tagId", coordinates, rs.getString("annotation"));
                answer.add(tag);
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        result.setAnswer(answer);
        result.setOverallPictureId(overallPictureId);
        return result;
    }

    @Override
    public double judgeExam(String employeeId, String examId, ArrayList<PictureTag> paper, int time) {
        double score = 0;
        int size = paper.size();
        Examination exam = getExam(examId);
        ArrayList<PictureTag> answer = exam.getAnswer();
        for (int i = 0; i < size; i++) {
            Coordinate p1 = answer.get(i).getBorder().get(0);
            Coordinate p2 = answer.get(i).getBorder().get(1);
            Coordinate q1 = paper.get(i).getBorder().get(0);
            Coordinate q2 = paper.get(i).getBorder().get(1);
            double dist = distance(p1, p2);
            double bord = assessRectangle(p1, p2, q1, q2);

            String[] keywords = answer.get(i).getAnnotation().split("/");
            double count = 0;
            for (String keyword : keywords) {
                if (paper.get(i).getAnnotation().contains(keyword)) count++;
            }
            double word=0;
            if(count>0){
                word=100;
            }
            score += (bord + word) / 2;
        }
        score /= size;
        if (score >= 60) {
            double efficiency = time / (double) (exam.getOverallPictureId().size());
            updateOverallJudgement(employeeId, score, efficiency);
        }
        return score;
    }

    public double distance(Coordinate p1, Coordinate p2) {
        return Math.sqrt((p1.getX() - p2.getX())*(p1.getX() - p2.getX()) + (p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
    }

    public double assessPoint(Coordinate std, Coordinate point, double dist) {
        double result = 100;
        if (distance(std, point) > 0.3 * dist) {
            result -= (distance(std, point) / (0.3 * dist) - 1) * 100;
        }
        return result;
    }

    public double assessRectangle(Coordinate std1, Coordinate std2, Coordinate pnt1, Coordinate pnt2) {
        int x1 = Math.min(Math.max(std1.getX(), std2.getX()), Math.max(pnt1.getX(), pnt2.getX()));
        int y1 = Math.min(Math.max(std1.getY(), std2.getY()), Math.max(pnt1.getY(), pnt2.getY()));
        int x2 = Math.max(Math.min(std1.getX(), std2.getX()), Math.min(pnt1.getX(), pnt2.getX()));
        int y2 = Math.max(Math.min(std1.getY(), std2.getY()), Math.min(pnt1.getY(), pnt2.getY()));

        double s1 = (pnt2.getX() - pnt1.getX()) * (pnt2.getY() - pnt1.getY());
        double s2 = (x2 - x1) * (y2 - y1);
        double s3 = (std2.getX() - std1.getX()) * (std2.getY() - std1.getY());

        double a = (s1 - s2) / s3;
        double b = (s3 - s2) / s3;

        double result = 100;
        if (a + b > 0.5){
            result = (1.5 - (a + b)) * 100;
        }
        if(result<0)
            result = 0;
        return result;
    }

    public void updateOverallJudgement(String employeeId, double accuracy, double efficiency){
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "UPDATE overallJudgement SET accuracy = ? , efficiency = ? WHERE employeeId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1,accuracy/100.0);
            stmt.setDouble(2,efficiency);
            stmt.setString(3,employeeId);
            stmt.executeUpdate();
            conn.close();
            stmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
