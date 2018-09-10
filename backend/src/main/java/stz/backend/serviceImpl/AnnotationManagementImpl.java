package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.AnnotationJudgement;
import stz.backend.entity.Coordinate;
import stz.backend.entity.EmployeeTaskInfo;
import stz.backend.entity.PictureTag;
import stz.backend.enums.AnnotationType;
import stz.backend.service.AnnotationManagementService;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * data/picturetag/employeeId/taskId/pictureId/tagId.txt
 */
public class AnnotationManagementImpl implements AnnotationManagementService {
    private EmployeeTaskManagementImpl employeeTaskManagement = new EmployeeTaskManagementImpl();
    @Override
    public boolean savePictureTag(String employeeId, String taskId, PictureTag pictureTag) {
        AnnotationType type = employeeTaskManagement.showPictureAnnotationType(taskId,pictureTag.getPictureID());
        try{
            Connection conn = BaseDao.getConnection();
            String check = "SELECT * FROM pictureTag WHERE employeeId = ? AND taskId = ? AND pictureId = ? AND tagId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1,employeeId);
            checkStmt.setString(2,taskId);
            checkStmt.setString(3,pictureTag.getPictureID());
            checkStmt.setString(4,pictureTag.getTagID());
            ResultSet checkRs = checkStmt.executeQuery();
            if(checkRs.next()){
                String delete = "DELETE FROM pictureTag WHERE employeeId = ? AND taskId = ? AND pictureId = ? AND tagId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,employeeId);
                deleteStmt.setString(2,taskId);
                deleteStmt.setString(3,pictureTag.getPictureID());
                deleteStmt.setString(4,pictureTag.getTagID());
                deleteStmt.executeUpdate();
            }
            String border = "";
            if(pictureTag.getBorder()!=null){
                for(int i = 0; i < pictureTag.getBorder().size(); i++){
                    border = border + pictureTag.getBorder().get(i).getX() + "," +
                                      pictureTag.getBorder().get(i).getY() + ";";
                }
            }
            String sql = "INSERT INTO pictureTag (employeeId,taskId,pictureId," +
                    "tagId,border,annotation) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,employeeId);
            stmt.setString(2,taskId);
            stmt.setString(3,pictureTag.getPictureID());
            stmt.setString(4,pictureTag.getTagID());
            stmt.setString(5,border);
            stmt.setString(6,pictureTag.getAnnotation());
            stmt.executeUpdate();

            String update = "SELECT * FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
            String updateJudge = "SELECT * FROM annotationJudgement WHERE employeeId = ? AND taskId = ?";

            PreparedStatement updateStmt = conn.prepareStatement(update);
            PreparedStatement updateJudgeStmt = conn.prepareStatement(updateJudge);

            updateStmt.setString(1,employeeId);
            updateStmt.setString(2,taskId);

            updateJudgeStmt.setString(1,employeeId);
            updateJudgeStmt.setString(2,taskId);

            ResultSet updateRs = updateStmt.executeQuery();
            ResultSet updateJudgeRs = updateJudgeStmt.executeQuery();
            EmployeeTaskInfo employeeTaskInfo = new EmployeeTaskInfo();
            if(updateRs.next() && updateJudgeRs.next()){
                ArrayList<String> completedPictureId = new ArrayList<>();
                if(updateRs.getString("completedPictureId").length()>0){
                    String[] c = updateRs.getString("completedPictureId").split(";");
                    for(int i = 0; i < c.length; i++){
                        completedPictureId.add(c[i]);
                    }
                }
                ArrayList<String> selectedPictureId = new ArrayList<>();
                if(updateRs.getString("selectedPictureId").length()>0){
                    String[] s = updateRs.getString("selectedPictureId").split(";");
                    for(int i = 0; i < s.length; i++){
                        selectedPictureId.add(s[i]);
                    }
                }
                AnnotationJudgement annotationJudgement = new AnnotationJudgement(
                        updateJudgeRs.getDouble("accuracy"),
                        updateJudgeRs.getDouble("efficiency"),
                        updateJudgeRs.getDouble("grade")
                );
                employeeTaskInfo = new EmployeeTaskInfo(updateRs.getString("employeeId"),
                        updateRs.getString("taskId"),
                        updateRs.getString("acceptTime"),
                        updateRs.getString("finishTime"),
                        updateRs.getInt("completedPictureNum"),
                        updateRs.getInt("totalPictureNum"),
                        completedPictureId,
                        selectedPictureId,
                        updateRs.getBoolean("isFinished"),
                        updateRs.getBoolean("receiveAward"),
                        annotationJudgement,
                        updateRs.getBoolean("reported"));
            }
            if (!employeeTaskInfo.getCompletedPictureId().contains(pictureTag.getPictureID())) {
                employeeTaskInfo.setCompletedPictureNum(employeeTaskInfo.getCompletedPictureNum() + 1);
                ArrayList<String> newCompletedPictureId = employeeTaskInfo.getCompletedPictureId();
                newCompletedPictureId.add(pictureTag.getPictureID());
                employeeTaskInfo.setCompletedPictureId(newCompletedPictureId);
                EmployeeTaskManagementImpl employeeTaskManagement = new EmployeeTaskManagementImpl();
                employeeTaskManagement.saveEmployeeTaskInfo(employeeTaskInfo);
            }
            conn.close();
            stmt.close();
            checkRs.close();
            checkStmt.close();
            updateRs.close();
            updateStmt.close();
            updateJudgeRs.close();
            updateJudgeStmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(type.equals(AnnotationType.OVERALL))
            employeeTaskManagement.assessOverall(taskId,pictureTag.getPictureID());
        else if(type.equals(AnnotationType.RECTANGLE))
            employeeTaskManagement.assessRectangle("rectangleAssessData",taskId,pictureTag.getPictureID());
        else if(type.equals(AnnotationType.BOUNDARY))
            employeeTaskManagement.assessBoundary(taskId,pictureTag.getPictureID());
        return true;
    }

    @Override
    public boolean modifyPictureTag(String employeeId, String taskId, PictureTag pictureTag) {
        return savePictureTag(employeeId, taskId, pictureTag);
    }

    @Override
    public boolean deleteOneTag(String employeeId, String taskId, String pictureID, String partID) {
        try{
            Connection conn = BaseDao.getConnection();
            String delete = "DELETE FROM pictureTag WHERE employeeId = ? AND taskId = ? AND pictureId = ? AND tagId = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(delete);
            deleteStmt.setString(1,employeeId);
            deleteStmt.setString(2,taskId);
            deleteStmt.setString(3,pictureID);
            deleteStmt.setString(4,partID);
            int rs = deleteStmt.executeUpdate();
            if(rs == 0) {
                conn.close();
                deleteStmt.close();
                return false;
            }
            conn.close();
            deleteStmt.close();
        }catch (Exception e){

        }
        return true;
    }

    @Override
    public ArrayList<String> findByPictureID(String employeeId, String taskId, String pictureID) {
        ArrayList<PictureTag> store = findPictureTag(employeeId, taskId, pictureID);
        if (store == null) {
            return null;
        }
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < store.size(); i++){
            result.add(store.get(i).getTagID());
        }
        return result;
    }

    @Override
    public PictureTag findByTagID(String employeeId, String taskId, String pictureID, String tagID) {
        ArrayList<PictureTag> store = findPictureTag(employeeId, taskId, pictureID);
        if(store == null)
            return null;
        PictureTag res = new PictureTag();
        for (int i = 0; i < store.size(); i++) {
            if (store.get(i).getTagID().contains(tagID)) {
                return store.get(i);
            }
        }
        return res;
    }

    public ArrayList<PictureTag> findPictureTag(String employeeId, String taskId, String pictureID){
        ArrayList<PictureTag> result = new ArrayList<PictureTag>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM pictureTag WHERE employeeId = ? AND taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,employeeId);
            stmt.setString(2,taskId);
            stmt.setString(3,pictureID);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next())
                return null;
            do{
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                if(rs.getString("border").length()>0){
                    String[] pos = rs.getString("border").split(";");
                    for(int i = 0; i < pos.length; i++){
                        String[] temp = pos[i].split(",");
                        Coordinate store = new Coordinate(Integer.parseInt(temp[0]),
                                                          Integer.parseInt(temp[1]));
                        coordinates.add(store);
                    }
                }

                PictureTag pictureTag = new PictureTag(rs.getString("pictureId"),
                        rs.getString("tagId"),
                        coordinates,
                        rs.getString("annotation"));
                result.add(pictureTag);
            }while(rs.next());
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){

        }
        return result;
    }
}