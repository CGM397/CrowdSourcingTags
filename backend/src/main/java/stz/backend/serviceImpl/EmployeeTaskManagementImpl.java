package stz.backend.serviceImpl;

import org.springframework.boot.origin.SystemEnvironmentOrigin;
import stz.backend.DAO.BaseDao;
import stz.backend.entity.*;
import stz.backend.enums.AnnotationType;
import stz.backend.service.EmployeeTaskManagementService;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 */
public class EmployeeTaskManagementImpl implements EmployeeTaskManagementService {
    EmployerInfoManagementImpl employerInfoManagement = new EmployerInfoManagementImpl();
    EmployeeInfoManagementImpl employeeInfoManagement = new EmployeeInfoManagementImpl();

    public void saveEmployeeTaskInfo(EmployeeTaskInfo employeeTaskInfo) {
        try{
            String completedPictureId = "";
            String selectedPictureId = "";
            if(employeeTaskInfo.getCompletedPictureId()!=null){
                for(int i = 0; i < employeeTaskInfo.getCompletedPictureId().size(); i++){
                    completedPictureId = completedPictureId + employeeTaskInfo.getCompletedPictureId().get(i) + ";";
                }
            }
            if(employeeTaskInfo.getSelectedPictureId()!=null){
                for(int i = 0; i < employeeTaskInfo.getSelectedPictureId().size(); i++){
                    selectedPictureId = selectedPictureId + employeeTaskInfo.getSelectedPictureId().get(i) + ";";
                }
            }
            Connection conn = BaseDao.getConnection();
            String check = "SELECT * FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
            String checkJudge = "SELECT * FROM annotationJudgement WHERE employeeId = ? AND taskId = ?";

            PreparedStatement checkStmt = conn.prepareStatement(check);
            PreparedStatement checkJudgeStmt = conn.prepareStatement(checkJudge);

            checkStmt.setString(1,employeeTaskInfo.getEmployeeId());
            checkStmt.setString(2,employeeTaskInfo.getTaskId());
            checkJudgeStmt.setString(1,employeeTaskInfo.getEmployeeId());
            checkJudgeStmt.setString(2,employeeTaskInfo.getTaskId());

            ResultSet checkRs = checkStmt.executeQuery();
            ResultSet checkJudgeRs = checkJudgeStmt.executeQuery();

            if(checkRs.next() && checkJudgeRs.next()){
                String delete = "DELETE FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
                String deleteJudge = "DELETE FROM annotationJudgement WHERE employeeId = ? AND taskId = ?";

                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                PreparedStatement deleteJudgeStmt = conn.prepareStatement(deleteJudge);

                deleteStmt.setString(1,employeeTaskInfo.getEmployeeId());
                deleteStmt.setString(2,employeeTaskInfo.getTaskId());
                deleteJudgeStmt.setString(1,employeeTaskInfo.getEmployeeId());
                deleteJudgeStmt.setString(2,employeeTaskInfo.getTaskId());

                deleteStmt.executeUpdate();
                deleteJudgeStmt.executeUpdate();
            }

            String sql = "INSERT INTO employeeTaskInfo (employeeId,taskId,acceptTime," +
                    "finishTime,completedPictureNum,totalPictureNum,completedPictureId," +
                    "selectedPictureId,isFinished,receiveAward,reported) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,employeeTaskInfo.getEmployeeId());
            stmt1.setString(2,employeeTaskInfo.getTaskId());
            stmt1.setString(3,employeeTaskInfo.getAcceptTime());
            stmt1.setString(4,employeeTaskInfo.getFinishTime());
            stmt1.setInt(5,employeeTaskInfo.getCompletedPictureNum());
            stmt1.setInt(6,employeeTaskInfo.getTotalPictureNum());
            stmt1.setString(7,completedPictureId);
            stmt1.setString(8,selectedPictureId);
            stmt1.setBoolean(9,employeeTaskInfo.getIsFinished());
            stmt1.setBoolean(10,employeeTaskInfo.getReceiveAward());
            stmt1.setBoolean(11,employeeTaskInfo.getReported());
            stmt1.executeUpdate();

            String insertJudge = "INSERT INTO annotationJudgement (employeeId,taskId,accuracy," +
                    "efficiency,grade) VALUES (?,?,?,?,?)";
            PreparedStatement insertJudgeStmt = conn.prepareStatement(insertJudge);
            insertJudgeStmt.setString(1,employeeTaskInfo.getEmployeeId());
            insertJudgeStmt.setString(2,employeeTaskInfo.getTaskId());
            insertJudgeStmt.setDouble(3,employeeTaskInfo.getJudgement().getAccuracy());
            insertJudgeStmt.setDouble(4,employeeTaskInfo.getJudgement().getEfficiency());
            insertJudgeStmt.setDouble(5,employeeTaskInfo.getJudgement().getGrade());
            insertJudgeStmt.executeUpdate();

            conn.close();
            stmt1.close();
            checkStmt.close();
            checkRs.close();
            checkJudgeRs.close();
            checkJudgeStmt.close();
            insertJudgeStmt.close();
        }catch (Exception e){

        }
    }

    @Override
    public ArrayList<String> showAvailableTasks() {
        ArrayList<String> result = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM task WHERE isFull = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setBoolean(1,false);
            ResultSet rs = stmt1.executeQuery();
            if(!rs.next())
                return null;
            do{
                result.add(rs.getString("taskId"));
            }while (rs.next());
            BaseDao.closeAll(conn, stmt1, rs);
        }catch (Exception e){

        }

        return result;
    }

    public ArrayList<EmployeeTaskInfo> showAllTasks(String employeeId){
        ArrayList<EmployeeTaskInfo> result = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeTaskInfo WHERE employeeId = ?";
            String getJudge = "SELECT * FROM annotationJudgement WHERE employeeId = ?";

            PreparedStatement stmt1 = conn.prepareStatement(sql);
            PreparedStatement judgeStmt = conn.prepareStatement(getJudge);

            stmt1.setString(1,employeeId);
            judgeStmt.setString(1,employeeId);

            ResultSet rs = stmt1.executeQuery();
            ResultSet judgeRs = judgeStmt.executeQuery();

            ArrayList<AnnotationJudgement> annotationJudgements = new ArrayList<>();
            ArrayList<String> storeTaskId = new ArrayList<>();
            while(judgeRs.next()){
                AnnotationJudgement oneJudge = new AnnotationJudgement(
                        judgeRs.getDouble("accuracy"),
                        judgeRs.getDouble("efficiency"),
                        judgeRs.getDouble("grade"));
                annotationJudgements.add(oneJudge);
                storeTaskId.add(judgeRs.getString("taskId"));
            }

            while(rs.next()){
                ArrayList<String> completedPictureId = new ArrayList<>();
                if(rs.getString("completedPictureId").length()>0){
                    String[] c = rs.getString("completedPictureId").split(";");
                    for(int i = 0; i < c.length; i++){
                        completedPictureId.add(c[i]);
                    }
                }
                ArrayList<String> selectedPictureId = new ArrayList<>();
                if(rs.getString("selectedPictureId").length()>0){
                    String[] s = rs.getString("selectedPictureId").split(";");
                    for(int i = 0; i < s.length; i++){
                        selectedPictureId.add(s[i]);
                    }
                }
                AnnotationJudgement annotationJudgement = new AnnotationJudgement();
                for(int i = 0; i < annotationJudgements.size(); i++){
                    if(storeTaskId.get(i).equals(rs.getString("taskId"))){
                        annotationJudgement = annotationJudgements.get(i);
                    }
                }

                EmployeeTaskInfo info = new EmployeeTaskInfo(rs.getString("employeeId"),
                        rs.getString("taskId"),
                        rs.getString("acceptTime"),
                        rs.getString("finishTime"),
                        rs.getInt("completedPictureNum"),
                        rs.getInt("totalPictureNum"),
                        completedPictureId,
                        selectedPictureId,
                        rs.getBoolean("isFinished"),
                        rs.getBoolean("receiveAward"),
                        annotationJudgement,
                        rs.getBoolean("reported"));
                result.add(info);
            }
            BaseDao.closeAll(conn, stmt1, rs);
            judgeRs.close();
            judgeStmt.close();
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public ArrayList<EmployeeTaskInfo> showUnderwayTasks(String employeeId) {
        ArrayList<EmployeeTaskInfo> result = new ArrayList<>();
        ArrayList<EmployeeTaskInfo> store = showAllTasks(employeeId);
        for(int i = 0; i < store.size(); i++){
            if(store.get(i).getIsFinished() == false)
                result.add(store.get(i));
        }

        return result;
    }

    @Override
    public ArrayList<EmployeeTaskInfo> showCompletedTasks(String employeeId) {
        ArrayList<EmployeeTaskInfo> result = new ArrayList<>();
        ArrayList<EmployeeTaskInfo> store = showAllTasks(employeeId);
        for(int i = 0; i < store.size(); i++){
            if(store.get(i).getIsFinished() == true)
                result.add(store.get(i));
        }

        return result;
    }

    @Override
    public Task checkOneTask(String taskId) {
        EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
        return employerTaskManagement.checkOneTask(taskId);
    }

    @Override
    public boolean select(String employeeId, String taskId){
        try{
            //save employeeTaskInfo
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,employeeId);
            stmt1.setString(2,taskId);
            ResultSet rs = stmt1.executeQuery();
            if(rs.next())
                return false;
            Task task = checkOneTask(taskId);
            String employerId = task.getEmployerId();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String acceptTime = simpleDateFormat.format(date);
            AnnotationJudgement annotationJudgement = new AnnotationJudgement(0,0,0);
            ArrayList<String> selectedPictureId = new ArrayList<>();
            selectedPictureId.addAll(task.getOverallPictureId());
            selectedPictureId.addAll(task.getRectanglePictureId());
            selectedPictureId.addAll(task.getBoundaryPictureId());
            EmployeeTaskInfo employeeTaskInfo = new EmployeeTaskInfo(employeeId, taskId, acceptTime,"finishTime", 0,
                    task.getTotalPictureAmount(), null,selectedPictureId, false,false, annotationJudgement, false);
            saveEmployeeTaskInfo(employeeTaskInfo);

            //update employerTaskInfo
            String sql2 = "SELECT * FROM employerTaskInfo WHERE employerId = ? AND taskId = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1,employerId);
            stmt2.setString(2,taskId);
            ResultSet rs2 = stmt2.executeQuery();
            if(!rs2.next())
                return false;
            ArrayList<String> acceptedEmployeeId = new ArrayList<>();
            if(rs2.getString("acceptedEmployeeId").length()>0){
                String[] a = rs2.getString("acceptedEmployeeId").split(";");
                for(int i = 0; i < a.length; i++){
                    acceptedEmployeeId.add(a[i]);
                }
            }
            ArrayList<String> completedEmployeeId = new ArrayList<>();
            if(rs2.getString("completedEmployeeId").length()>0){
                String[] c = rs2.getString("completedEmployeeId").split(";");
                for(int i = 0; i < c.length; i++){
                    completedEmployeeId.add(c[i]);
                }
            }
            EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo(rs2.getString("employerId"),
                    rs2.getString("taskId"),
                    rs2.getInt("neededEmployeeNum"),
                    rs2.getInt("acceptedEmployeeNum"),
                    rs2.getInt("completedEmployeeNum"),
                    acceptedEmployeeId,
                    completedEmployeeId,
                    rs2.getBoolean("isFinished"));

            EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
            acceptedEmployeeId = employerTaskInfo.getAcceptedEmployeeId();
            acceptedEmployeeId.add(employeeId);
            employerTaskInfo.setAcceptedEmployeeId(acceptedEmployeeId);
            employerTaskInfo.setAcceptedEmployeeNum(employerTaskInfo.getAcceptedEmployeeNum() + 1);

            task.setIsFull(employerTaskInfo.getAcceptedEmployeeNum() == employerTaskInfo.getNeededEmployeeNum());
            ArrayList<Integer> pictureAcceptedTimes = task.getPictureAcceptedTimes();
            for(int i = 0; i < pictureAcceptedTimes.size(); i++){
                pictureAcceptedTimes.set(i,pictureAcceptedTimes.get(i)+1);
            }
            task.setPictureAcceptedTimes(pictureAcceptedTimes);
            employerTaskManagement.saveTask(task);
            employerTaskManagement.saveEmployerTaskInfo(employerTaskInfo);

            //update employeeInfo
            EmployeeInfo employeeInfo = employeeInfoManagement.showEmployeeInfo(employeeId);
            employeeInfo.setTaskAmount(employeeInfo.getTaskAmount() + 1);
            employeeInfo.setTaskUnderway(employeeInfo.getTaskUnderway() + 1);
            employeeInfoManagement.modifyEmployeeInfo(employeeInfo);
            BaseDao.closeAll(conn, stmt1, rs);
            stmt2.close();
            rs2.close();
        }catch (Exception e){

        }
        return true;
    }

    @Override
    public boolean partSelect(String employeeId, String taskId, int selectedPictureNum){
        boolean result = false;
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,employeeId);
            stmt1.setString(2,taskId);
            ResultSet rs = stmt1.executeQuery();
            if(rs.next())
                return false;
            Task task = checkOneTask(taskId);
            String employerId = task.getEmployerId();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String acceptTime = simpleDateFormat.format(date);
            AnnotationJudgement annotationJudgement = new AnnotationJudgement(0,0,0);
            ArrayList<String> selectedPictureId = new ArrayList<>();
            ArrayList<Integer> pictureAcceptedTimes = task.getPictureAcceptedTimes();
            ArrayList<String> allPictures = new ArrayList<>();
            allPictures.addAll(task.getOverallPictureId());
            allPictures.addAll(task.getRectanglePictureId());
            allPictures.addAll(task.getBoundaryPictureId());

            int startPos = 0;
            for(int i = 0; i < pictureAcceptedTimes.size() - 1; i++){
                if(pictureAcceptedTimes.get(i) > pictureAcceptedTimes.get(i+1))
                    startPos = i + 1;
            }
            //update task.pictureAcceptedTimes
            for(int i = startPos; i < selectedPictureNum && i < pictureAcceptedTimes.size(); i++){
                pictureAcceptedTimes.set(i, pictureAcceptedTimes.get(i) + 1);
            }
            if(selectedPictureNum > pictureAcceptedTimes.size() - startPos){
                for(int i = 0; i < selectedPictureNum + startPos - pictureAcceptedTimes.size(); i++){
                    pictureAcceptedTimes.set(i, pictureAcceptedTimes.get(i) + 1);
                }
            }
            //insert employeeTaskInfo.selectedPictureId
            for(int i = startPos; i < selectedPictureNum + startPos && i < allPictures.size(); i++){
                selectedPictureId.add(allPictures.get(i));
            }
            EmployeeTaskInfo employeeTaskInfo = new EmployeeTaskInfo(employeeId, taskId, acceptTime,"finishTime", 0,
                    selectedPictureNum, null,selectedPictureId, false,false, annotationJudgement, false);
            //create employeeTaskInfo
            saveEmployeeTaskInfo(employeeTaskInfo);

            //update employerTaskInfo
            String sql2 = "SELECT * FROM employerTaskInfo WHERE employerId = ? AND taskId = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1,employerId);
            stmt2.setString(2,taskId);
            ResultSet rs2 = stmt2.executeQuery();
            if(!rs2.next())
                return false;
            ArrayList<String> acceptedEmployeeId = new ArrayList<>();
            if(rs2.getString("acceptedEmployeeId").length()>0){
                String[] a = rs2.getString("acceptedEmployeeId").split(";");
                for(int i = 0; i < a.length; i++){
                    acceptedEmployeeId.add(a[i]);
                }
            }
            ArrayList<String> completedEmployeeId = new ArrayList<>();
            if(rs2.getString("completedEmployeeId").length()>0){
                String[] c = rs2.getString("completedEmployeeId").split(";");
                for(int i = 0; i < c.length; i++){
                    completedEmployeeId.add(c[i]);
                }
            }
            EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo(rs2.getString("employerId"),
                    rs2.getString("taskId"),
                    rs2.getInt("neededEmployeeNum"),
                    rs2.getInt("acceptedEmployeeNum"),
                    rs2.getInt("completedEmployeeNum"),
                    acceptedEmployeeId,
                    completedEmployeeId,
                    rs2.getBoolean("isFinished"));

            EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
            acceptedEmployeeId = employerTaskInfo.getAcceptedEmployeeId();
            acceptedEmployeeId.add(employeeId);
            employerTaskInfo.setAcceptedEmployeeId(acceptedEmployeeId);
            employerTaskInfo.setAcceptedEmployeeNum(employerTaskInfo.getAcceptedEmployeeNum() + 1);

            //update task and employerTaskInfo
            boolean isFull = true;
            for(int i = 0; i < pictureAcceptedTimes.size(); i++){
                if(pictureAcceptedTimes.get(i) < task.getNeededEmployeeAmount()){
                    isFull = false;
                    break;
                }
            }
            task.setIsFull(isFull);
            task.setPictureAcceptedTimes(pictureAcceptedTimes);
            employerTaskManagement.saveTask(task);
            employerTaskManagement.saveEmployerTaskInfo(employerTaskInfo);

            //update employeeInfo
            EmployeeInfo employeeInfo = employeeInfoManagement.showEmployeeInfo(employeeId);
            employeeInfo.setTaskAmount(employeeInfo.getTaskAmount() + 1);
            employeeInfo.setTaskUnderway(employeeInfo.getTaskUnderway() + 1);
            employeeInfoManagement.modifyEmployeeInfo(employeeInfo);
            BaseDao.closeAll(conn, stmt1, rs);
            stmt2.close();
            rs2.close();
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int getRestPictures(String taskId){
        int result = 0;
        Task task = checkOneTask(taskId);
        ArrayList<Integer> pictureAcceptedTimes = task.getPictureAcceptedTimes();
        int neededEmployeeAmount = task.getNeededEmployeeAmount();
        for(int i = 0; i < pictureAcceptedTimes.size(); i++){
            if(pictureAcceptedTimes.get(i) < neededEmployeeAmount)
                result++;
        }
        return result;
    }

    @Override
    public boolean giveUp(String employeeId, String taskId) {
        /*File employeeFile = new File("data/task/employee/" + employeeId + "/" + taskId + ".txt");
        if (!employeeFile.exists()) {
            return false;
        } else {
            employeeFile.delete();

            Task task = checkOneTask(taskId);
            String employerId = task.getEmployerId();
            EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo();

            File employerFile = new File("data/task/employer/" + employerId + "/" + taskId + ".txt");
            if (!employerFile.exists()) {
                return false;
            } else {
                EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
                employerTaskInfo = employerTaskManagement.constructEmployerTaskInfo(employerId, taskId, employerFile);
            }

            ArrayList<String> acceptedEmployeeId = employerTaskInfo.getAcceptedEmployeeId();
            acceptedEmployeeId.remove(employeeId);
            employerTaskInfo.setAcceptedEmployeeId(acceptedEmployeeId);
            employerTaskInfo.setAcceptedEmployeeNum(employerTaskInfo.getAcceptedEmployeeNum() - 1);

            task.setIsFull(employerTaskInfo.getAcceptedEmployeeNum() == employerTaskInfo.getNeededEmployeeNum());

            EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
            employerTaskManagement.saveTask(task);
            employerTaskManagement.saveEmployerTaskInfo(employerTaskInfo);

            return true;
        }*/
        return true;
    }

    @Override
    public AnnotationType showPictureAnnotationType(String taskId, String pictureId) {
        EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
        Task task = employerTaskManagement.checkOneTask(taskId);
        for (int i = 0; i < task.getOverallPictureId().size(); i++) {
            if (task.getOverallPictureId().get(i).equals(pictureId)) {
                return AnnotationType.OVERALL;
            }
        }
        for (int i = 0; i < task.getRectanglePictureId().size(); i++) {
            if (task.getRectanglePictureId().get(i).equals(pictureId)) {
                return AnnotationType.RECTANGLE;
            }
        }
        for (int i = 0 ;i < task.getBoundaryPictureId().size(); i++) {
            if (task.getBoundaryPictureId().get(i).equals(pictureId)) {
                return AnnotationType.BOUNDARY;
            }
        }
        return null;
    }

    @Override
    public boolean submit(String employeeId, String taskId) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
            String getJudge = "SELECT * FROM annotationJudgement WHERE employeeId = ? AND taskId = ?";

            PreparedStatement stmt1 = conn.prepareStatement(sql);
            PreparedStatement getJudgeStmt = conn.prepareStatement(getJudge);

            stmt1.setString(1,employeeId);
            stmt1.setString(2,taskId);
            getJudgeStmt.setString(1,employeeId);
            getJudgeStmt.setString(2,taskId);

            ResultSet rs = stmt1.executeQuery();
            ResultSet getJudgeRs = getJudgeStmt.executeQuery();

            EmployeeTaskInfo employeeTaskInfo = new EmployeeTaskInfo();
            if(rs.next() && getJudgeRs.next()){
                ArrayList<String> completedPictureId = new ArrayList<>();
                if(rs.getString("completedPictureId").length()>0){
                    String[] c = rs.getString("completedPictureId").split(";");
                    for(int i = 0; i < c.length; i++){
                        completedPictureId.add(c[i]);
                    }
                }
                ArrayList<String> selectedPictureId = new ArrayList<>();
                if(rs.getString("selectedPictureId").length()>0){
                    String[] s = rs.getString("selectedPictureId").split(";");
                    for(int i = 0; i < s.length; i++){
                        selectedPictureId.add(s[i]);
                    }
                }
                AnnotationJudgement annotationJudgement = new AnnotationJudgement(
                        getJudgeRs.getDouble("accuracy"),
                        getJudgeRs.getDouble("efficiency"),
                        getJudgeRs.getDouble("grade")
                );
                employeeTaskInfo = new EmployeeTaskInfo(rs.getString("employeeId"),
                        rs.getString("taskId"),
                        rs.getString("acceptTime"),
                        rs.getString("finishTime"),
                        rs.getInt("completedPictureNum"),
                        rs.getInt("totalPictureNum"),
                        completedPictureId,
                        selectedPictureId,
                        rs.getBoolean("isFinished"),
                        rs.getBoolean("receiveAward"),
                        annotationJudgement,
                        rs.getBoolean("reported"));

                Task task = checkOneTask(taskId);
                String employerId = task.getEmployerId();

                //update employerTaskInfo
                String checkEmployer = "SELECT * FROM employerTaskInfo WHERE employerId = ? AND taskId = ?";
                PreparedStatement stmt2 = conn.prepareStatement(checkEmployer);
                stmt2.setString(1,employerId);
                stmt2.setString(2,taskId);
                ResultSet employerRs = stmt2.executeQuery();
                if(!employerRs.next())
                    return false;
                EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
                ArrayList<String> acceptedEmployeeId = new ArrayList<>();
                if(employerRs.getString("acceptedEmployeeId").length()>0){
                    String[] a = employerRs.getString("acceptedEmployeeId").split(";");
                    for(int i = 0; i < a.length; i++){
                        acceptedEmployeeId.add(a[i]);
                    }
                }
                ArrayList<String> completedEmployeeId = new ArrayList<>();
                if(employerRs.getString("completedEmployeeId").length()>0){
                    String[] c = employerRs.getString("completedEmployeeId").split(";");
                    for(int i = 0; i < c.length; i++){
                        completedEmployeeId.add(c[i]);
                    }
                }
                EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo(employerRs.getString("employerId"),
                        employerRs.getString("taskId"),
                        employerRs.getInt("neededEmployeeNum"),
                        employerRs.getInt("acceptedEmployeeNum"),
                        employerRs.getInt("completedEmployeeNum"),
                        acceptedEmployeeId,
                        completedEmployeeId,
                        employerRs.getBoolean("isFinished"));
                completedEmployeeId = employerTaskInfo.getCompletedEmployeeId();
                completedEmployeeId.add(employeeId);
                employerTaskInfo.setCompletedEmployeeId(completedEmployeeId);
                employerTaskInfo.setCompletedEmployeeNum(employerTaskInfo.getCompletedEmployeeNum() + 1);
                employerTaskInfo.setIsFinished(employerTaskInfo.getAcceptedEmployeeNum() == employerTaskInfo.getCompletedEmployeeNum()
                                               && task.getIsFull());
                if (employerTaskInfo.getIsFinished()) {
                    EmployerInfo employerInfo = employerInfoManagement.showEmployerInfo(employerId);
                    employerInfo.setTaskUnderway(employerInfo.getTaskUnderway() - 1);
                    employerInfo.setTaskCompleted(employerInfo.getTaskCompleted() + 1);
                    employerInfoManagement.modifyEmployerInfo(employerInfo);
                }

                //update employerTaskInfo
                employerTaskManagement.saveEmployerTaskInfo(employerTaskInfo);

                //update employeeTaskInfo
                employeeTaskInfo.setFinishTime(employerTaskManagement.getTime());
                employeeTaskInfo.setIsFinished(true);
                saveEmployeeTaskInfo(employeeTaskInfo);

                //update employeeInfo
                //EmployeeInfo employeeInfo = employeeInfoManagement.showEmployeeInfo(employeeId);
                //employeeInfo.setTaskCompleted(employeeInfo.getTaskCompleted() + 1);
                //employeeInfo.setTaskUnderway(employeeInfo.getTaskUnderway() - 1);
                //employeeInfoManagement.modifyEmployeeInfo(employeeInfo);
                stmt2.close();
                employerRs.close();
            }
            BaseDao.closeAll(conn, stmt1, rs);
            getJudgeRs.close();
            getJudgeStmt.close();
        }catch (Exception e){

        }
        return true;
    }

    public double getNewAward(String employeeId, String taskId){
        Task task = checkOneTask(taskId);
        int award = task.getAward();
        ArrayList<EmployeeTaskInfo> store = showCompletedTasks(employeeId);
        EmployeeTaskInfo update = new EmployeeTaskInfo();
        for(int i = 0; i < store.size(); i++){
            if(store.get(i).getTaskId().equals(taskId)){
                update = store.get(i);
                break;
            }
        }
        double newAward = (update.getCompletedPictureNum()/(double)task.getTotalPictureAmount()*award)*update.getJudgement().getAccuracy();
        return newAward;
    }

    @Override
    public boolean getAward(String employeeId, String taskId) {
        EmployeeInfo employeeInfo = employeeInfoManagement.showEmployeeInfo(employeeId);

        ArrayList<EmployeeTaskInfo> store = showCompletedTasks(employeeId);
        EmployeeTaskInfo update = new EmployeeTaskInfo();
        for(int i = 0; i < store.size(); i++){
            if(store.get(i).getTaskId().equals(taskId)){
                update = store.get(i);
                break;
            }
        }
        update.setReceiveAward(true);
        employeeInfo.setAwardAmount(employeeInfo.getAwardAmount() + (int)getNewAward(employeeId, taskId));
        saveEmployeeTaskInfo(update);
        return employeeInfoManagement.modifyEmployeeInfo(employeeInfo);
    }

    @Override
    public String getPictureBase64Code(String taskId, String pictureId) {
        EmployerTaskManagementImpl employerTaskManagement = new EmployerTaskManagementImpl();
        return employerTaskManagement.getPictureBase64Code(taskId, pictureId);
    }

    @Override
    public ArrayList<String> recommend(String employeeId) {
        ArrayList<String> underwayTasks = new ArrayList<>();
        ArrayList<EmployeeTaskInfo> taskInfo = showAllTasks(employeeId);
        ArrayList<String> taskIds = new ArrayList<>();
        for(int i = 0; i < taskInfo.size(); i++)
            taskIds.add(taskInfo.get(i).getTaskId());
        ArrayList<String> result = new ArrayList<>();
        int taskNum = 0;
        int employeeNum = 0;
        HashMap<Integer, String> storeTaskId= new HashMap<>();
        ArrayList<String> storeEmployeeId = new ArrayList<>();
        int k = 0;
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT taskId,isFull FROM task WHERE releaseType<>?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,"指定委派");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String oneTaskId = rs.getString("taskId");
                if(!rs.getBoolean("isFull"))
                    underwayTasks.add(oneTaskId);
                storeTaskId.put(taskNum, oneTaskId);
                taskNum++;
            }
            String sql2 = "SELECT employeeId FROM employeeInfo";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            ResultSet rs2 = stmt2.executeQuery();
            while(rs2.next()) {
                if(!rs2.getString("employeeId").equals(employeeId))
                    storeEmployeeId.add(rs2.getString("employeeId"));
                employeeNum++;
            }
            if(taskNum>=5)
                k = 5;
            else
                k = taskNum;
            if(employeeNum == 0 || taskNum == 0)
                return result;
            double[][] employeeScore = new double[employeeNum - 1][taskNum];
            double[] selectedEmployee = new double[taskNum];
            for(int i = 0; i < employeeNum - 1; i++){
                String employee = storeEmployeeId.get(i);
                for(int j = 0; j < taskNum; j++){
                    String task = storeTaskId.get(j);
                    String getAccuracy = "SELECT isFinished FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
                    PreparedStatement getAccuracyStmt = conn.prepareStatement(getAccuracy);
                    getAccuracyStmt.setString(1,employee);
                    getAccuracyStmt.setString(2,task);
                    ResultSet getAccuracyRs = getAccuracyStmt.executeQuery();
                    if(getAccuracyRs.next()){
                        employeeScore[i][j] = 1;
                    }
                    else
                        employeeScore[i][j] = 0;
                }
            }
            for(int i = 0; i < taskNum; i++){
                String task = storeTaskId.get(i);
                String getAccuracy = "SELECT isFinished FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";
                PreparedStatement getAccuracyStmt = conn.prepareStatement(getAccuracy);
                getAccuracyStmt.setString(1,employeeId);
                getAccuracyStmt.setString(2,task);
                ResultSet getAccuracyRs = getAccuracyStmt.executeQuery();
                if(getAccuracyRs.next()){
                    selectedEmployee[i] = 1;
                }
                else
                    selectedEmployee[i] = 0;
            }
            File file = new File("recommendData/param.txt");
            FileWriter fw = new FileWriter(file, false);
            fw.write((employeeNum - 1)+"\r\n" + taskNum + "\r\n" + k + "\r\n");
            for(int i = 0; i < employeeNum - 1; i++){
                for(int j = 0; j < taskNum - 1; j++){
                    fw.write(employeeScore[i][j] + " ");
                }
                fw.write(employeeScore[i][taskNum - 1] + "\r\n");
            }
            for(int i = 0; i < taskNum - 1; i++){
                fw.write(selectedEmployee[i]+" ");
            }
            fw.write(selectedEmployee[taskNum - 1] + "\r\n");
            fw.flush();
            fw.close();
            BaseDao.closeAll(conn, stmt, rs);
            stmt2.close();
            rs2.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //execute python
        executePython("recommend.py", "recommendData");
        //get result
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("recommendData/result.txt")));
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                int col = Integer.parseInt(line);
                if(!taskIds.contains(storeTaskId.get(col))&&underwayTasks.contains(storeTaskId.get(col)))
                    result.add(storeTaskId.get(col));
                if(result.size()==k)
                    break;
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<String> recommendSimilar(String employeeId,String taskId) {
        ArrayList<String> result = new ArrayList<>();
        HashMap<Integer,String> taskRemarks = new HashMap<>();
        HashMap<Integer,String> taskIds = new HashMap<>();
        Task lastFinishedTask = checkOneTask(taskId);
        taskRemarks.put(0,lastFinishedTask.getMarks());
        taskIds.put(0,taskId);
        ArrayList<EmployeeTaskInfo> taskInfo = showAllTasks(employeeId);
        ArrayList<String> selectedTaskIds = new ArrayList<>();
        for(int i = 0; i < taskInfo.size(); i++)
            selectedTaskIds.add(taskInfo.get(i).getTaskId());
        int taskNum = 0;
        int k = 0;

        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT taskId,marks,taskType FROM task WHERE isFull = ? AND releaseType<>?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1,false);
            stmt.setString(2,"指定委派");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String oneTaskId = rs.getString("taskId");
                String oneRemark = rs.getString("marks");
                String taskTypes = rs.getString("taskType");
                int sign = 0;
                if(taskTypes.length()>0){
                    String[] types = taskTypes.split(";");
                    for(int i = 0; i < types.length; i++){
                        if(lastFinishedTask.getTaskType().contains(types[i])) {
                            sign = 1;
                        }
                    }
                }
                if(!selectedTaskIds.contains(oneTaskId) && sign == 1) {
                        taskNum++;
                        taskRemarks.put(taskNum, oneRemark);
                        taskIds.put(taskNum, oneTaskId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(taskNum<5)
            k = taskNum;
        else
            k = 5;
        //write data
        deleteSentences("recommendSimilarData");
        try{
            File temp = new File("recommendSimilarData/sentences");
            temp.mkdirs();
            File file = new File("recommendSimilarData/param.txt");
            FileWriter fw = new FileWriter(file, false);
            fw.write(k+"");
            for(int i = 0; i < taskRemarks.size(); i++){
                File fileRemarks = new File("recommendSimilarData/sentences/"+i+".txt");
                FileWriter fwRemarks = new FileWriter(fileRemarks, false);
                fwRemarks.write(taskRemarks.get(i));
                fwRemarks.flush();
                fwRemarks.close();
            }
            fw.flush();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        //execute python
        executePython("recommendSimilar.py", "recommendSimilarData");

        //get result
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("recommendSimilarData/result.txt")));
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                int col = Integer.parseInt(line);
                result.add(taskIds.get(col));
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void assessOverall(String taskId, String pictureId) {
        int k = 2;
        ArrayList<PictureTag> pictureTags = new ArrayList<>();
        ArrayList<String> employeeIds = new ArrayList<>();
        ArrayList<Point> store = new ArrayList<>();            //store the points to write in file
        double leftTopUpperLimit = 0, rightBottomUpperLimit = 0;
        Coordinate standardPointOne = new Coordinate();        //leftTop
        Coordinate standardPointTwo = new Coordinate();        //rightBottom
        HashMap<String, Double> score = new HashMap<>();       //overall score(frame + annotation)
        HashMap<String, Double> annotationScore = new HashMap<>(); //store the annotation score
        JudgeResult result = new JudgeResult();
        result.setTaskId(taskId);
        result.setPictureId(pictureId);
        //get pictureTags
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM pictureTag WHERE taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,taskId);
            stmt.setString(2,pictureId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                employeeIds.add(rs.getString("employeeId"));
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                if(rs.getString("border").length()>0){
                    String[] pos = rs.getString("border").split(";");
                    for(int i = 0; i < pos.length; i++){
                        String[] temp = pos[i].split(",");
                        coordinates.add(new Coordinate(Integer.parseInt(temp[0]),
                                Integer.parseInt(temp[1])));
                    }
                }
                Point point1 = new Point(0,0);
                Point point2 = new Point(0,0);
                if(coordinates.size()>=2){
                    point1.setX((coordinates.get(0).getX() < coordinates.get(1).getX()) ?
                            coordinates.get(0).getX() : coordinates.get(1).getX());
                    point1.setY((coordinates.get(0).getY() < coordinates.get(1).getY()) ?
                            coordinates.get(0).getY() : coordinates.get(1).getY());
                    point2.setX((coordinates.get(0).getX() > coordinates.get(1).getX()) ?
                            coordinates.get(0).getX() : coordinates.get(1).getX());
                    point2.setY((coordinates.get(0).getY() > coordinates.get(1).getY()) ?
                            coordinates.get(0).getY() : coordinates.get(1).getY());
                }
                store.add(point1);
                store.add(point2);
                PictureTag pictureTag = new PictureTag(rs.getString("pictureId"),
                        rs.getString("tagId"),
                        coordinates,
                        rs.getString("annotation"));
                pictureTags.add(pictureTag);
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(employeeIds.size()<5)
            return;
        for(int i = 0; i < employeeIds.size(); i++)
            annotationScore.put(employeeIds.get(i),0.0);                         //magic number
        //write param and sentences
        writeParam("overallAssessData", k, store);
        HashMap<String, String> send = new HashMap<>();
        for(int i = 0; i < pictureTags.size(); i++){
            send.put(employeeIds.get(i),pictureTags.get(i).getAnnotation());
        }
        writeSentences("overallAssessData", send);
        //execute python
        executePython("assess_points.py","overallAssessData");
        executePython("assess_sentences.py","overallAssessData");
        //get frame result
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("overallAssessData/frameResult.txt")));
            String line = "";
            ArrayList<String> content = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null){
                content.add(line);
            }
            PictureTag tag = new PictureTag();
            tag.setPictureID(pictureId);
            tag.setTagID("1");
            tag.setAnnotation("");
            if(content.size()>=2){
                String[] temp1 = content.get(0).split(",");
                String[] temp2 = content.get(1).split(",");
                if(Float.parseFloat(temp1[0]) < Float.parseFloat(temp2[0])){
                    leftTopUpperLimit = Double.parseDouble(temp1[3]);
                    rightBottomUpperLimit = Double.parseDouble(temp2[3]);
                }else{
                    leftTopUpperLimit = Double.parseDouble(temp2[3]);
                    rightBottomUpperLimit = Double.parseDouble(temp1[3]);
                }
                ArrayList<Coordinate> cos = new ArrayList<>();
                int co1x = Math.round(Float.parseFloat(temp1[0]) < Float.parseFloat(temp2[0]) ?
                        Float.parseFloat(temp1[0]):Float.parseFloat(temp2[0]));
                int co1y = Math.round(Float.parseFloat(temp1[1]) < Float.parseFloat(temp2[1]) ?
                        Float.parseFloat(temp1[1]):Float.parseFloat(temp2[1]));
                int co2x = Math.round(Float.parseFloat(temp1[0]) > Float.parseFloat(temp2[0]) ?
                        Float.parseFloat(temp1[0]):Float.parseFloat(temp2[0]));
                int co2y = Math.round(Float.parseFloat(temp1[1]) > Float.parseFloat(temp2[1]) ?
                        Float.parseFloat(temp1[1]):Float.parseFloat(temp2[1]));
                standardPointOne = new Coordinate(co1x, co1y);
                standardPointTwo = new Coordinate(co2x, co2y);
                cos.add(standardPointOne);
                cos.add(standardPointTwo);
                tag.setBorder(cos);
            }
            ArrayList<PictureTag> tags = new ArrayList<>();
            tags.add(tag);
            result.setStandardResult(tags);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get annotation result
        annotationScore = readAnnotationResult("overallAssessData",annotationScore);
        //calculate the score
        for(int i = 0; i < pictureTags.size(); i++){
            int leftTopX = 0, leftTopY = 0, rightBottomX = 0, rightBottomY = 0;
            if(pictureTags.get(i).getBorder().size()>=2){
                PictureTag tempTag = pictureTags.get(i);
                leftTopX = tempTag.getBorder().get(0).getX() < tempTag.getBorder().get(1).getX() ?
                        tempTag.getBorder().get(0).getX() : tempTag.getBorder().get(1).getX();
                leftTopY = tempTag.getBorder().get(0).getY() < tempTag.getBorder().get(1).getY() ?
                        tempTag.getBorder().get(0).getY() : tempTag.getBorder().get(1).getY();
                rightBottomX = tempTag.getBorder().get(0).getX() > tempTag.getBorder().get(1).getX() ?
                        tempTag.getBorder().get(0).getX() : tempTag.getBorder().get(1).getX();
                rightBottomY = tempTag.getBorder().get(0).getY() > tempTag.getBorder().get(1).getY() ?
                        tempTag.getBorder().get(0).getY() : tempTag.getBorder().get(1).getY();
                double offsetOne = (standardPointOne.getX() - leftTopX)*(standardPointOne.getX() - leftTopX)+
                        (standardPointOne.getY() - leftTopY)*(standardPointOne.getY() - leftTopY);
                offsetOne = Math.sqrt(offsetOne);
                double offsetTwo = (standardPointTwo.getX() - rightBottomX)*(standardPointTwo.getX() - rightBottomX)+
                        (standardPointTwo.getY() - rightBottomY)*(standardPointTwo.getY() - rightBottomY);
                offsetTwo = Math.sqrt(offsetTwo);
                double borderScore = 0;
                if(offsetOne - leftTopUpperLimit > 0){
                    borderScore += 0.5 * (1 - (offsetOne - leftTopUpperLimit)/leftTopUpperLimit);
                }else
                    borderScore += 0.5;
                if(offsetTwo - rightBottomUpperLimit > 0){
                    borderScore += 0.5 * (1 - (offsetTwo - rightBottomUpperLimit)/rightBottomUpperLimit);
                }else
                    borderScore += 0.5;
                if(borderScore<0)
                    borderScore = 0;
                //方框得分和标注得分加权算总分
                score.put(employeeIds.get(i), borderScore * 0.6 + annotationScore.get(employeeIds.get(i)) * 0.4);
            }
        }

        //find the annotationStandardResult
        int selectedEmployee = 0;
        double highestScore = 0.0;
        for(int i = 0; i < employeeIds.size(); i++){
            if(highestScore<score.get(employeeIds.get(i))) {
                highestScore = score.get(employeeIds.get(i));
                selectedEmployee = i;
            }
        }
        PictureTag tempTag = result.getStandardResult().get(0);
        tempTag.setAnnotation(pictureTags.get(selectedEmployee).getAnnotation());
        ArrayList<PictureTag> s = new ArrayList<>();
        s.add(tempTag);
        result.setStandardResult(s);
        result.setEmployeeScore(score);

        insertStandardResult(result);
        for(String employeeId : score.keySet())
            insertEmployeeScore(employeeId,taskId,pictureId,score.get(employeeId));
    }

    @Override
    public void assessRectangle(String prefix, String taskId, String pictureId) {
        JudgeResult result = new JudgeResult();
        HashMap<String, Double> score = new HashMap<>();
        HashMap<String, Double> annotationScore = new HashMap<>();
        int k = 0;
        ArrayList<String> employeeIds = getAllEmployeeIds(taskId, pictureId);
        if(employeeIds.size()<5)
            return;
        for(int i = 0; i < employeeIds.size(); i++) {
            score.put(employeeIds.get(i), 0.0);
            annotationScore.put(employeeIds.get(i),0.0);
        }
        HashMap<Integer, Integer> tagCounts = new HashMap<>();
        ArrayList<PictureTag> pictureTags = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();

        for(int i = 0; i < employeeIds.size(); i++){
            ArrayList<PictureTag> tags = getAllPictureTags(employeeIds.get(i), taskId, pictureId);
            if(tagCounts.containsKey(tags.size()))
                tagCounts.put(tags.size(), tagCounts.get(tags.size())+1);
            else
                tagCounts.put(tags.size(),1);
            pictureTags.addAll(tags);
        }
        //find the K
        int counts = 0;
        for(Integer oneCounts : tagCounts.keySet()){
            if(counts < tagCounts.get(oneCounts)) {
                counts = tagCounts.get(oneCounts);
                k = oneCounts;
            }
        }
        //calculate the gravityCenter
        points.addAll(getAllGravityCenter(pictureTags));
        //write param
        writeParam(prefix, k, points);
        //executePython
        executePython("assess_points.py",prefix);
        //get frame results
        ArrayList<StandardPoint> standardPoints = readFrameResult(prefix);
        for(int i = 0; i < employeeIds.size(); i++){
            ArrayList<PictureTag> tags = getAllPictureTags(employeeIds.get(i),taskId,pictureId);
            ArrayList<Point> sp = getAllGravityCenter(tags);
            ArrayList<Integer> hitCount = new ArrayList<>();
            for(int a = 0; a < standardPoints.size(); a++){
                hitCount.add(0);
            }
            for(int j = 0; j < standardPoints.size(); j++){
                for(int m = 0; m < sp.size(); m++){
                    double distance = (sp.get(m).getX() - standardPoints.get(j).getPoint().getX())*
                                    (sp.get(m).getX() - standardPoints.get(j).getPoint().getX()) +
                                    (sp.get(m).getY() - standardPoints.get(j).getPoint().getY())*
                                    (sp.get(m).getY() - standardPoints.get(j).getPoint().getY());
                    distance = Math.sqrt(distance);
                    if(distance < standardPoints.get(j).getUpperLimit() && hitCount.get(j) != 1){
                        hitCount.set(j, 1);
                        standardPoints.get(j).addAnnotation(employeeIds.get(i), tags.get(m).getAnnotation());
                        sp.remove(m);
                    }
                }
            }
            int hitCounts = 0;
            for(int a = 0; a < hitCount.size(); a++){
                if(hitCount.get(a) == 1)
                    hitCounts++;
            }
            double borderScore = 0;
            if(hitCount.size()>0) {
                borderScore = hitCounts / (double) (hitCount.size());
            }
            if(sp.size()>standardPoints.size())
                borderScore -= ((sp.size()-standardPoints.size())/(double)(standardPoints.size()))/2.0;
            if(borderScore < 0)
                borderScore = 0;
            score.put(employeeIds.get(i),borderScore * 0.6);
        }
        for(int i = 0; i < standardPoints.size(); i++) {
            if(standardPoints.get(i).annotations.size()>0){
                writeSentences(prefix, standardPoints.get(i).annotations);
                executePython("assess_sentences.py",prefix);
                annotationScore = readAnnotationResult(prefix,annotationScore);
                for(String employeeId : annotationScore.keySet()){
                    score.put(employeeId,score.get(employeeId) + (annotationScore.get(employeeId)/(double)(standardPoints.size())) * 0.4);
                }
            }
        }
        //find highest score
        double highestScore = 0.0;
        String selectedEmployee = "";
        for(String employeeId : score.keySet()){
            if(score.get(employeeId)>=highestScore){
                highestScore = score.get(employeeId);
                selectedEmployee = employeeId;
            }
        }
        result.setStandardResult(getAllPictureTags(selectedEmployee,taskId,pictureId));
        result.setEmployeeScore(score);
        result.setTaskId(taskId);
        result.setPictureId(pictureId);
        insertStandardResult(result);
        for(String employeeId : score.keySet())
            insertEmployeeScore(employeeId,taskId,pictureId,score.get(employeeId));
    }

    @Override
    public void assessBoundary(String taskId, String pictureId) {
        assessRectangle("boundaryAssessData",taskId,pictureId);
    }

    @Override
    public JudgeResult getJudgeResult(String taskId, String pictureId) {
        JudgeResult result = new JudgeResult();
        result.setTaskId(taskId);
        result.setPictureId(pictureId);
        ArrayList<PictureTag> tags = new ArrayList<>();
        HashMap<String, Double> employeeScore = new HashMap<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM standardResult WHERE taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,taskId);
            stmt.setString(2,pictureId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                String border = rs.getString("border");
                if(border.length()>0){
                    String[] pos = border.split(";");
                    for(int i = 0; i < pos.length; i++){
                        String[] temp = pos[i].split(",");
                        coordinates.add(new Coordinate(Integer.parseInt(temp[0]),
                                Integer.parseInt(temp[1])));
                    }
                }
                PictureTag tag = new PictureTag(pictureId,rs.getString("tagId"),
                        coordinates, rs.getString("annotation"));
                tags.add(tag);
            }
            result.setStandardResult(tags);
            String score = "SELECT * FROM employeeScore WHERE taskId = ? AND pictureId = ?";
            PreparedStatement scoreStmt = conn.prepareStatement(score);
            scoreStmt.setString(1,taskId);
            scoreStmt.setString(2,pictureId);
            ResultSet scoreRs = scoreStmt.executeQuery();
            while(scoreRs.next()){
                employeeScore.put(scoreRs.getString("employeeId"),
                        scoreRs.getDouble("score"));
            }
            result.setEmployeeScore(employeeScore);
            BaseDao.closeAll(conn, stmt, rs);
            scoreRs.close();
            scoreStmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void executePython(String url, String prefix){
        try {
            Process proc = Runtime.getRuntime().exec("python " + url + " " + prefix);
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllEmployeeIds(String taskId, String pictureId){
        ArrayList<String> employeeIds = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT employeeId FROM pictureTag WHERE taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,taskId);
            stmt.setString(2,pictureId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                if(!employeeIds.contains(rs.getString("employeeId")))
                    employeeIds.add(rs.getString("employeeId"));
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return employeeIds;
    }

    public ArrayList<PictureTag> getAllPictureTags(String employeeId, String taskId, String pictureId){
        ArrayList<PictureTag> pictureTags = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM pictureTag WHERE employeeId = ? AND taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,employeeId);
            stmt.setString(2,taskId);
            stmt.setString(3,pictureId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                ArrayList<Coordinate> coordinates = new ArrayList<>();
                String bo = rs.getString("border");
                if(bo.length()>0){
                    String[] pos = bo.split(";");
                    for(int i = 0; i < pos.length; i++){
                        String[] temp = pos[i].split(",");
                        Coordinate co = new Coordinate(Integer.parseInt(temp[0]),
                                Integer.parseInt(temp[1]));
                        coordinates.add(co);
                    }
                }
                PictureTag pictureTag = new PictureTag(rs.getString("pictureId"),
                        rs.getString("tagId"),
                        coordinates,
                        rs.getString("annotation"));
                pictureTags.add(pictureTag);
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pictureTags;
    }

    public void writeParam(String prefix, int k, ArrayList<Point> points){
        try{
            File file = new File(prefix + "/param.txt");
            FileWriter fw = new FileWriter(file, false);
            fw.write(k+"");
            File fileParam = new File(prefix + "/data.txt");
            FileWriter fwParam = new FileWriter(fileParam, false);
            for(int i = 0; i < points.size(); i++){
                fwParam.write(points.get(i).getX() + "," + points.get(i).getY() + "\r\n");
            }
            fw.flush();
            fwParam.flush();
            fw.close();
            fwParam.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeSentences(String prefix, HashMap<String,String> tagAnnotations){
        deleteSentences(prefix);
        try{
            File temp = new File(prefix+"/sentences");
            temp.mkdirs();
            for(String employeeId : tagAnnotations.keySet()){
                File fileSentence = new File(prefix + "/sentences/"+employeeId+".txt");
                FileWriter fwSentence = new FileWriter(fileSentence,false);
                fwSentence.write(tagAnnotations.get(employeeId));
                fwSentence.flush();
                fwSentence.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<StandardPoint> readFrameResult(String prefix){
        ArrayList<StandardPoint> points = new ArrayList<>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(prefix + "/frameResult.txt")));
            String line = "";
            ArrayList<String> content = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null){
                content.add(line);
            }
            for(int i = 0; i < content.size(); i++){
                String[] temp = content.get(i).split(",");
                Point standard = new Point(Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
                double limit = Double.parseDouble(temp[3]);
                StandardPoint sp = new StandardPoint(standard, limit);
                points.add(sp);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public void deleteSentences(String prefix){
        try{
            File file = new File(prefix+"/sentences");
            String[] lists = file.list();
            for(int i = 0; i < lists.length; i++){
                File fileToDelete = new File(prefix + "/sentences/" + lists[i]);
                fileToDelete.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Double> readAnnotationResult(String prefix, HashMap<String, Double> annotationScore){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(prefix + "/annotationResult.txt")));
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                String[] tempLine = line.split("/");
                annotationScore.put(tempLine[2].substring(0,tempLine[2].indexOf('.')),1.0);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return annotationScore;
    }

    public void insertStandardResult(JudgeResult result){
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM standardResult WHERE taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,result.getTaskId());
            stmt.setString(2,result.getPictureId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String delete = "DELETE FROM standardResult WHERE taskId = ? AND pictureId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,result.getTaskId());
                deleteStmt.setString(2,result.getPictureId());
                deleteStmt.executeUpdate();
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Connection conn = BaseDao.getConnection();
            for(int j = 0; j < result.getStandardResult().size(); j++){
                String border = "";
                if(result.getStandardResult().get(j).getBorder()!=null){
                    for(int i = 0; i < result.getStandardResult().get(j).getBorder().size(); i++){
                        border = border + result.getStandardResult().get(j).getBorder().get(i).getX() + "," +
                                result.getStandardResult().get(j).getBorder().get(i).getY() + ";";
                    }
                }
                String insert = "INSERT INTO standardResult(taskId,pictureId,tagId,border,annotation) " +
                        "VALUES (?,?,?,?,?)";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setString(1,result.getTaskId());
                insertStmt.setString(2,result.getPictureId());
                insertStmt.setString(3,j+"");
                insertStmt.setString(4,border);
                insertStmt.setString(5,result.getStandardResult().get(j).getAnnotation());
                insertStmt.executeUpdate();
            }
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertEmployeeScore(String employeeId, String taskId, String pictureId, double score){
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeScore WHERE employeeId = ? AND taskId = ? AND pictureId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,employeeId);
            stmt.setString(2,taskId);
            stmt.setString(3,pictureId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String delete = "DELETE FROM employeeScore WHERE employeeId = ? AND taskId = ? AND pictureId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,employeeId);
                deleteStmt.setString(2,taskId);
                deleteStmt.setString(3,pictureId);
                deleteStmt.executeUpdate();
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Connection conn = BaseDao.getConnection();
            String insert = "INSERT INTO employeeScore(employeeId,taskId,pictureId,score) " +
                    "VALUES (?,?,?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(insert);
            insertStmt.setString(1,employeeId);
            insertStmt.setString(2,taskId);
            insertStmt.setString(3,pictureId);
            insertStmt.setDouble(4,score);
            insertStmt.executeUpdate();
            conn.close();
            insertStmt.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Point> getAllGravityCenter(ArrayList<PictureTag> pictureTags){
        ArrayList<Point> points = new ArrayList<>();
        for(int i = 0; i < pictureTags.size(); i++){
            ArrayList<Coordinate> coordinates = pictureTags.get(i).getBorder();
            ArrayList<Point> temp = new ArrayList<>();
            for(int j = 0; j < coordinates.size(); j++){
                Point point = new Point(coordinates.get(j).getX(),coordinates.get(j).getY());
                temp.add(point);
            }
            points.add(gravityCenter(temp));
        }
        return points;
    }

    public Point diff(Point p, Point q) {
        return new Point(p.x - q.x, p.y - q.y);
    }

    public double cross(Point p, Point q) {
        return p.x * q.y - p.y * q.x;
    }

    public Point gravityCenter(ArrayList<Point> points) {
        double totalArea = 0;
        Point result = new Point(0, 0);
        int n = points.size();
        if(n == 2){
            result.x = (points.get(0).x + points.get(1).x)/2.0;
            result.y = (points.get(0).y + points.get(1).y)/2.0;
            return result;
        }
        for (int i = 1; i < n - 1; i++) {
            double tempArea = cross(diff(points.get(i), points.get(0)), diff(points.get(i + 1), points.get(0))) / 2;
            result.x += (points.get(i).x + points.get(i + 1).x + points.get(0).x) / 3 * tempArea;
            result.y += (points.get(i).y + points.get(i + 1).y + points.get(0).y) / 3 * tempArea;
            totalArea += tempArea;
        }
        result.x /= totalArea;
        result.y /= totalArea;
        return result;
    }
    public class Point {
        private double x;
        private double y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() { return x; }

        public double getY() { return y; }

        public void setX(double x) { this.x = x; }

        public void setY(double y) { this.y = y; }
    }

    public class StandardPoint {
        private Point point;
        private double upperLimit;
        private HashMap<String,String> annotations = new HashMap<>();
        public StandardPoint(Point point, double upperLimit){
            this.point = point;
            this.upperLimit = upperLimit;
        }

        public Point getPoint() {
            return point;
        }

        public double getUpperLimit() {
            return upperLimit;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public void setUpperLimit(double upperLimit) {
            this.upperLimit = upperLimit;
        }

        public void addAnnotation(String employeeId, String oneAnnotation){
            this.annotations.put(employeeId, oneAnnotation);
        }
    }
}
