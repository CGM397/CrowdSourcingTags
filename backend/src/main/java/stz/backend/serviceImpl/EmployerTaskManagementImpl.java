package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.*;
import stz.backend.enums.ReleaseType;
import stz.backend.service.EmployerTaskManagementService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmployerTaskManagementImpl implements EmployerTaskManagementService {
    EmployeeInfoManagementImpl employeeInfoManagement = new EmployeeInfoManagementImpl();
    EmployeeTaskManagementImpl employeeTaskManagement = new EmployeeTaskManagementImpl();
    MessageManagementImpl messageManagement = new MessageManagementImpl();
    public String getTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return simpleDateFormat.format(date);
    }

    @Override
    public String getNewTaskId() {
        return getTime();
    }

    public void savePicture(String taskId, Picture picture) {
        try{
            Connection conn = BaseDao.getConnection();
            String check = "SELECT * FROM picture WHERE taskId = ? AND pictureId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1,taskId);
            checkStmt.setString(2,picture.getPictureId());
            ResultSet checkRs = checkStmt.executeQuery();
            if(checkRs.next()){
               String delete = "DELETE FROM picture WHERE taskId = ? AND pictureId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,taskId);
                deleteStmt.setString(2,picture.getPictureId());
                deleteStmt.executeUpdate();
            }

            String sql = "INSERT INTO picture (taskId,pictureId,base64Code) VALUES " +
                    "(?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,taskId);
            stmt1.setString(2,picture.getPictureId());
            stmt1.setString(3,picture.getPictureBase64Code());
            stmt1.executeUpdate();
            conn.close();
            stmt1.close();
            checkRs.close();
            checkStmt.close();
        }catch (Exception e){

        }
    }

    public void saveTask(Task task) {
        String taskType = "";
        String overall = "";
        String rect = "";
        String boundary = "";
        String accept = "";
        String appoint = "";
        if(task.getTaskType()!=null){
            for(int i = 0; i < task.getTaskType().size(); i++){
                taskType = taskType + task.getTaskType().get(i) + ";";
            }
        }
        if(task.getOverallPictureId()!=null){
            for(int i = 0; i < task.getOverallPictureId().size(); i++){
                overall = overall + task.getOverallPictureId().get(i) + ";";
            }
        }
        if(task.getRectanglePictureId()!=null){
            for(int i = 0; i < task.getRectanglePictureId().size(); i++){
                rect = rect + task.getRectanglePictureId().get(i) + ";";
            }
        }
        if(task.getBoundaryPictureId()!=null){
            for(int i = 0; i < task.getBoundaryPictureId().size(); i++){
                boundary = boundary + task.getBoundaryPictureId().get(i) + ";";
            }
        }
        if(task.getPictureAcceptedTimes()!=null){
            for(int i = 0; i < task.getPictureAcceptedTimes().size(); i++){
                accept = accept + task.getPictureAcceptedTimes().get(i) + ";";
            }
        }
        if(task.getAppointedEmployeeId()!=null){
            for(int i = 0; i < task.getAppointedEmployeeId().size(); i++){
                appoint = appoint + task.getAppointedEmployeeId().get(i) + ";";
            }
        }

        try{
            Connection conn = BaseDao.getConnection();
            String check = "SELECT * FROM task WHERE taskId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1,task.getTaskId());
            ResultSet checkRs = checkStmt.executeQuery();
            if(checkRs.next()){
                String delete = "DELETE FROM task WHERE taskId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,task.getTaskId());
                deleteStmt.executeUpdate();
            }

            String sql = "INSERT INTO task (isFull,taskId,taskName,taskType,employerId," +
                    "releaseType,startTime,limitTime,neededEmployeeAmount,totalPictureAmount," +
                    "award,overallPictureId,rectanglePictureId,boundaryPictureId," +
                    "pictureAcceptedTimes,appointedEmployeeId,marks) VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setBoolean(1,task.getIsFull());
            stmt1.setString(2,task.getTaskId());
            stmt1.setString(3,task.getTaskName());
            stmt1.setString(4,taskType);
            stmt1.setString(5,task.getEmployerId());
            stmt1.setString(6,ReleaseType.transToString(task.getReleaseType()));
            stmt1.setString(7,task.getStartTime());
            stmt1.setString(8,task.getLimitTime());
            stmt1.setInt(9,task.getNeededEmployeeAmount());
            stmt1.setInt(10,task.getTotalPictureAmount());
            stmt1.setInt(11,task.getAward());
            stmt1.setString(12,overall);
            stmt1.setString(13,rect);
            stmt1.setString(14,boundary);
            stmt1.setString(15,accept);
            stmt1.setString(16,appoint);
            stmt1.setString(17,task.getMarks());
            stmt1.executeUpdate();
            conn.close();
            stmt1.close();
            checkStmt.close();
            checkRs.close();
        }catch (Exception e){

        }
    }

    public void saveEmployerTaskInfo(EmployerTaskInfo employerTaskInfo) {
        String acceptedId = "";
        String completedId = "";
        if(employerTaskInfo.getAcceptedEmployeeId()!=null){
            for(int i = 0; i < employerTaskInfo.getAcceptedEmployeeId().size(); i++){
                acceptedId = acceptedId + employerTaskInfo.getAcceptedEmployeeId().get(i) + ";";
            }
        }
        if(employerTaskInfo.getCompletedEmployeeId()!=null){
            for(int i = 0; i < employerTaskInfo.getCompletedEmployeeId().size(); i++){
                completedId = completedId + employerTaskInfo.getCompletedEmployeeId().get(i) + ";";
            }
        }

        try{
            Connection conn = BaseDao.getConnection();
            String check = "SELECT * FROM employerTaskInfo WHERE employerId = ? AND taskId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1,employerTaskInfo.getEmployerId());
            checkStmt.setString(2,employerTaskInfo.getTaskId());
            ResultSet checkRs = checkStmt.executeQuery();
            if(checkRs.next()){
                String delete = "DELETE FROM employerTaskInfo WHERE employerId = ? AND taskId = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(delete);
                deleteStmt.setString(1,employerTaskInfo.getEmployerId());
                deleteStmt.setString(2,employerTaskInfo.getTaskId());
                deleteStmt.executeUpdate();
            }
            String sql = "INSERT INTO employerTaskInfo (employerId,taskId,neededEmployeeNum," +
                    "acceptedEmployeeNum,completedEmployeeNum,acceptedEmployeeId,completedEmployeeId," +
                    "isFinished) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,employerTaskInfo.getEmployerId());
            stmt1.setString(2,employerTaskInfo.getTaskId());
            stmt1.setInt(3,employerTaskInfo.getNeededEmployeeNum());
            stmt1.setInt(4,employerTaskInfo.getAcceptedEmployeeNum());
            stmt1.setInt(5,employerTaskInfo.getCompletedEmployeeNum());
            stmt1.setString(6,acceptedId);
            stmt1.setString(7,completedId);
            stmt1.setBoolean(8,employerTaskInfo.getIsFinished());
            stmt1.executeUpdate();
            conn.close();
            stmt1.close();
            checkRs.close();
            checkStmt.close();
        }catch (Exception e){

        }
    }

    @Override
    public boolean release(ArrayList<Picture> picture, Task taskInfo) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM task WHERE taskId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,taskInfo.getTaskId());
            ResultSet rs = stmt1.executeQuery();
            if(rs.next()){
               return false;
            }
            BaseDao.closeAll(conn, stmt1, rs);
            for (int i = 0; i < picture.size(); i++) {
                savePicture(taskInfo.getTaskId(), picture.get(i));
            }

            taskInfo.setStartTime(getTime());
            saveTask(taskInfo);

            EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo(taskInfo.getEmployerId(), taskInfo.getTaskId(), taskInfo.getNeededEmployeeAmount(),
                    0, 0, null, null, false);
            saveEmployerTaskInfo(employerTaskInfo);

            EmployerInfoManagementImpl employerInfoManagement = new EmployerInfoManagementImpl();
            EmployerInfo employerInfo = employerInfoManagement.showEmployerInfo(taskInfo.getEmployerId());
            employerInfo.setTaskAmount(employerInfo.getTaskAmount() + 1);
            employerInfo.setTaskUnderway(employerInfo.getTaskUnderway() + 1);
            employerInfoManagement.modifyEmployerInfo(employerInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        UserManagementImpl userManagement = new UserManagementImpl();
        ArrayList<EmployeeInfo> employees = userManagement.showEmployeeInfo();
        for(int i = 0; i < employees.size(); i++){
            if(!taskInfo.getReleaseType().equals(ReleaseType.APPOINTED)||
                    (taskInfo.getAppointedEmployeeId().size()>0&&
                            taskInfo.getAppointedEmployeeId().contains(employees.get(i).getEmployeeId()))) {
                createReleaseMessage(taskInfo.getReleaseType(), taskInfo.getTaskId(), employees.get(i).getEmployeeId());
            }
        }
        return true;
    }

    public void createReleaseMessage(ReleaseType releaseType,String taskId, String userId){
            Message message = new Message();
            message.setMessageId(new Date().getTime()+"");
            message.setUserId(userId);
            message.setReleaseTime(new java.util.Date());
            if(releaseType.equals(ReleaseType.APPOINTED)){
                message.setTitle("新指派任务");
                message.setContent("一个指派任务 " + taskId + " 发布");
            }else{
                message.setTitle("新任务发布");
                message.setContent("一个新任务 " + taskId + " 发布");
            }
            message.setIsRead(false);
            messageManagement.createMessage(message);
    }

    public ArrayList<EmployerTaskInfo> showAllTasks(String employerId){
        ArrayList<EmployerTaskInfo> result = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employerTaskInfo WHERE employerId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,employerId);
            ResultSet rs = stmt1.executeQuery();
            if(!rs.next())
                return result;
            else{
                do{
                    ArrayList<String> acceptId = new ArrayList<>();
                    ArrayList<String> completedId = new ArrayList<>();
                    if(rs.getString("acceptedEmployeeId").length()>0){
                        String[] a = rs.getString("acceptedEmployeeId").split(";");
                        for(int i = 0; i < a.length; i++){
                            acceptId.add(a[i]);
                        }
                    }
                    if(rs.getString("completedEmployeeId").length()>0){
                        String[] c = rs.getString("completedEmployeeId").split(";");
                        for(int i = 0; i < c.length; i++){
                            completedId.add(c[i]);
                        }
                    }
                    EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo(
                            rs.getString("employerId"),
                            rs.getString("taskId"),
                            rs.getInt("neededEmployeeNum"),
                            rs.getInt("acceptedEmployeeNum"),
                            rs.getInt("completedEmployeeNum"),
                            acceptId,
                            completedId,
                            rs.getBoolean("isFinished")
                    );
                        result.add(employerTaskInfo);
                }while(rs.next());
            }
            BaseDao.closeAll(conn, stmt1, rs);
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public ArrayList<EmployerTaskInfo> showCompletedTasks(String employerId) {
        ArrayList<EmployerTaskInfo> result = new ArrayList<>();
        ArrayList<EmployerTaskInfo> store = showAllTasks(employerId);
        if(store != null&&store.size() > 0){
            for(int i = 0; i < store.size(); i++){
                if(store.get(i).getIsFinished())
                    result.add(store.get(i));
            }
        }
        return result;
    }

    @Override
    public ArrayList<EmployerTaskInfo> showUnderwayTasks(String employerId) {
        ArrayList<EmployerTaskInfo> result = new ArrayList<>();
        ArrayList<EmployerTaskInfo> store = showAllTasks(employerId);
        if(store != null&&store.size() > 0){
            for(int i = 0; i < store.size(); i++){
                if(!store.get(i).getIsFinished())
                    result.add(store.get(i));
            }
        }
        return result;
    }

    @Override
    public Task checkOneTask(String taskId) {
        Task result = new Task();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM task WHERE taskId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,taskId);
            ResultSet rs = stmt1.executeQuery();
            if(!rs.next())
                return result;
            else{
                ArrayList<String> type = new ArrayList<>();
                if(rs.getString("taskType").length()>0) {
                    String[] t = rs.getString("taskType").split(";");
                    for (int i = 0; i < t.length; i++) {
                        type.add(t[i]);
                    }
                }
                ArrayList<String> overall = new ArrayList<>();
                if(rs.getString("overallPictureId").length()>0) {
                    String[] o = rs.getString("overallPictureId").split(";");
                    for (int i = 0; i < o.length; i++) {
                        overall.add(o[i]);
                    }
                }
                ArrayList<String> rect = new ArrayList<>();
                if(rs.getString("rectanglePictureId").length()>0) {
                    String[] r = rs.getString("rectanglePictureId").split(";");
                    for (int i = 0; i < r.length; i++) {
                        rect.add(r[i]);
                    }
                }
                ArrayList<String> boundary = new ArrayList<>();
                if(rs.getString("boundaryPictureId").length()>0) {
                    String[] b = rs.getString("boundaryPictureId").split(";");
                    for (int i = 0; i < b.length; i++) {
                        boundary.add(b[i]);
                    }
                }
                ArrayList<Integer> accept = new ArrayList<>();
                if(rs.getString("pictureAcceptedTimes").length()>0) {
                    String[] ac = rs.getString("pictureAcceptedTimes").split(";");
                    for (int i = 0; i < ac.length; i++) {
                        accept.add(Integer.parseInt(ac[i]));
                    }
                }
                ArrayList<String> appoint = new ArrayList<>();
                if(rs.getString("appointedEmployeeId").length()>0) {
                    String[] ap = rs.getString("appointedEmployeeId").split(";");
                    for (int i = 0; i < ap.length; i++) {
                        appoint.add(ap[i]);
                    }
                }

                result = new Task(rs.getBoolean("isFull"),
                        rs.getString("taskId"),
                        rs.getString("taskName"),
                        type,
                        rs.getString("employerId"),
                        ReleaseType.transToReleaseType(rs.getString("releaseType")),
                        rs.getString("startTime"),
                        rs.getString("limitTime"),
                        rs.getInt("neededEmployeeAmount"),
                        rs.getInt("totalPictureAmount"),
                        rs.getInt("award"),
                        overall,
                        rect,
                        boundary,
                        accept,
                        appoint,
                        rs.getString("marks"));
            }
            BaseDao.closeAll(conn, stmt1, rs);
        }catch (Exception e){

        }
        return result;
    }

    /**
     * 路径:data/pictures/taskId/pictureId.txt
     * @param taskId
     * @param pictureId
     * @return
     */
    @Override
    public String getPictureBase64Code(String taskId, String pictureId) {
        String result = "";
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM picture WHERE taskId = ? AND pictureId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1,taskId);
            stmt1.setString(2,pictureId);
            ResultSet rs = stmt1.executeQuery();
            if(!rs.next()){
                return null;
            }else{
                result = rs.getString("base64Code");
            }
            BaseDao.closeAll(conn, stmt1, rs);
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public boolean submit(String employerId, String taskId) {
        boolean result = false;
        //update employerInfo
        EmployerInfoManagementImpl employerInfoManagement = new EmployerInfoManagementImpl();
        EmployerInfo employerInfo = employerInfoManagement.showEmployerInfo(employerId);
        employerInfo.setTaskUnderway(employerInfo.getTaskUnderway() - 1);
        employerInfo.setTaskCompleted(employerInfo.getTaskCompleted() + 1);
        employerInfoManagement.modifyEmployerInfo(employerInfo);
        ArrayList<String> store = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            //update employeeTaskInfo
            String sql = "UPDATE employeeTaskInfo SET finishTime = ?,isFinished = ? WHERE taskId = ? AND isFinished = ?";
            //update employerTaskInfo
            String sql2 = "UPDATE employerTaskInfo SET isFinished = ? WHERE employerId = ? AND taskId = ?";
            //update task,close the task
            String sql3 = "UPDATE task SET isFull = ? WHERE taskId = ?";
            //update employeeInfo
            String sql4 = "SELECT * FROM employeeTaskInfo WHERE taskId = ?";
            //get employeeIds
            String employeeIds = "SELECT employeeId FROM employeeTaskInfo WHERE taskId = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            PreparedStatement stmt3 = conn.prepareStatement(sql3);
            PreparedStatement stmt4 = conn.prepareStatement(sql4);
            PreparedStatement employeeIdStmt = conn.prepareStatement(employeeIds);

            stmt.setString(1,getTime());
            stmt.setBoolean(2,true);
            stmt.setString(3,taskId);
            stmt.setBoolean(4,false);

            stmt2.setBoolean(1,true);
            stmt2.setString(2,employerId);
            stmt2.setString(3,taskId);

            stmt3.setBoolean(1,true);
            stmt3.setString(2,taskId);

            stmt4.setString(1,taskId);

            employeeIdStmt.setString(1,taskId);
            ResultSet employeeIdRs = employeeIdStmt.executeQuery();
            while(employeeIdRs.next()){
                store.add(employeeIdRs.getString("employeeId"));
            }

            stmt.executeUpdate();
            int rs2 = stmt2.executeUpdate();
            int rs3 = stmt3.executeUpdate();
            ResultSet rs4 = stmt4.executeQuery();
            while(rs4.next()){
                String updateEmployeeInfo = "UPDATE employeeInfo SET taskUnderway = taskUnderway - ?," +
                        "taskCompleted = taskCompleted + ? WHERE employeeId = ?";
                PreparedStatement updateEmployeeInfoStmt = conn.prepareStatement(updateEmployeeInfo);
                updateEmployeeInfoStmt.setInt(1,1);
                updateEmployeeInfoStmt.setInt(2,1);
                updateEmployeeInfoStmt.setString(3,rs4.getString("employeeId"));
                updateEmployeeInfoStmt.executeUpdate();
            }
            if(rs2 > 0 && rs3 > 0)
                result = true;
            BaseDao.closeAll(conn, employeeIdStmt,employeeIdRs);
            stmt.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            rs4.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //update annotationJudgement and overallJudgement
        for(int i = 0; i < store.size(); i++){
            int completedTasks = employeeInfoManagement.showEmployeeInfo(store.get(i)).getTaskCompleted();
            try{
                Connection conn = BaseDao.getConnection();
                String score = "SELECT score FROM employeeScore WHERE employeeId = ? AND taskId = ?";
                String sql = "UPDATE annotationJudgement SET accuracy = ? , efficiency = ? , grade = ? WHERE employeeId = ? AND taskId = ?";
                String overallScore = "UPDATE overallJudgement SET accuracy = (accuracy*?+?)/?,efficiency = (efficiency*?+?)/?,grade = grade+? WHERE employeeId = ?";
                String oneEmployeeTaskInfo = "SELECT acceptTime,finishTime,selectedPictureId FROM employeeTaskInfo WHERE employeeId = ? AND taskId = ?";

                PreparedStatement scoreStmt = conn.prepareStatement(score);
                PreparedStatement stmt = conn.prepareStatement(sql);
                PreparedStatement overallScoreStmt = conn.prepareStatement(overallScore);
                PreparedStatement taskInfoStmt = conn.prepareStatement(oneEmployeeTaskInfo);

                scoreStmt.setString(1,store.get(i));
                scoreStmt.setString(2,taskId);
                ResultSet scoreRs = scoreStmt.executeQuery();
                ArrayList<Double> scores = new ArrayList<>();
                double totalScore = 0;
                while(scoreRs.next()){
                    scores.add(scoreRs.getDouble("score"));
                }
                for(int j = 0; j < scores.size(); j++){
                    totalScore += scores.get(j);
                }
                if(scores.size()>0)
                    totalScore = totalScore/scores.size();

                taskInfoStmt.setString(1,store.get(i));
                taskInfoStmt.setString(2,taskId);
                ResultSet infoRs = taskInfoStmt.executeQuery();
                String acceptTime = "", finishTime = "", selectedPictureId = "";
                int pictureNum = 0;
                if(infoRs.next()){
                    acceptTime = infoRs.getString("acceptTime");
                    finishTime = infoRs.getString("finishTime");
                    selectedPictureId = infoRs.getString("selectedPictureId");
                    if(selectedPictureId.length()>0){
                        String[] temp = selectedPictureId.split(";");
                        pictureNum = temp.length;
                    }
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                Date start = simpleDateFormat.parse(acceptTime);
                Date end = simpleDateFormat.parse(finishTime);
                long totalTime = (end.getTime() - start.getTime())/1000;
                double efficiency = totalTime/pictureNum;

                stmt.setDouble(1,totalScore);
                stmt.setDouble(2,efficiency);
                stmt.setDouble(3,totalScore*pictureNum*10);
                stmt.setString(4,store.get(i));
                stmt.setString(5,taskId);
                stmt.executeUpdate();

                overallScoreStmt.setDouble(1,completedTasks-1);
                overallScoreStmt.setDouble(2,totalScore);
                overallScoreStmt.setDouble(3,completedTasks);
                overallScoreStmt.setDouble(4,completedTasks-1);
                overallScoreStmt.setDouble(5,efficiency);
                overallScoreStmt.setDouble(6,completedTasks);
                overallScoreStmt.setDouble(7,totalScore*pictureNum*10);
                overallScoreStmt.setString(8,store.get(i));
                overallScoreStmt.executeUpdate();

                BaseDao.closeAll(conn, stmt, scoreRs);
                taskInfoStmt.close();
                infoRs.close();
                scoreStmt.close();
                overallScoreStmt.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ArrayList<Double> accuracy = new ArrayList<>();
        ArrayList<Double> efficiency = new ArrayList<>();
        ArrayList<Double> award = new ArrayList<>();
        ArrayList<Double> grade = new ArrayList<>();
        for(int i = 0; i < store.size(); i++){
            ArrayList<EmployeeTaskInfo> infos = employeeTaskManagement.showCompletedTasks(store.get(i));
            EmployeeTaskInfo update = new EmployeeTaskInfo();
            for(int j = 0; j < infos.size(); j++){
                if(infos.get(j).getTaskId().equals(taskId)){
                    update = infos.get(j);
                    break;
                }
            }
            accuracy.add(update.getJudgement().getAccuracy());
            efficiency.add(update.getJudgement().getEfficiency());
            grade.add(update.getJudgement().getGrade());
            award.add(employeeTaskManagement.getNewAward(store.get(i),taskId));
        }
        createFinishMessage(store, taskId, accuracy, efficiency, award, grade);
        return result;
    }

    public void createFinishMessage(ArrayList<String> employeeIds, String taskId,
                                    ArrayList<Double> accuracy, ArrayList<Double> efficiency,
                                    ArrayList<Double> award, ArrayList<Double> grade){
        for(int i = 0; i < employeeIds.size(); i++) {
            DecimalFormat df = new DecimalFormat("#.00");
            Message message = new Message();
            message.setMessageId(new Date().getTime()+"");
            message.setUserId(employeeIds.get(i));
            message.setReleaseTime(new java.util.Date());
            message.setTitle("任务完成");
            message.setContent("您的任务 " + taskId +
                    " 完成, 您的评价为: 准确度: " + df.format(accuracy.get(i)) +
                    " 效率:" + df.format(efficiency.get(i)) +
                    " 奖励: " + df.format(award.get(i)) +
                    " 获得经验: " + df.format(grade.get(i)));
            message.setIsRead(false);
            messageManagement.createMessage(message);
        }
    }

    @Override
    public ArrayList<String> getParticipateEmployee(String taskId, String pictureId) {
        ArrayList<String> result = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT employeeId, selectedPictureId FROM employeeTaskInfo WHERE taskId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,taskId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String employeeId = rs.getString("employeeId");
                String store = rs.getString("selectedPictureId");
                if(store.length() > 0){
                    String[] selectedPictureId = store.split(";");
                    for(int i = 0; i < selectedPictureId.length; i++){
                        if(selectedPictureId[i].equals(pictureId)) {
                            result.add(employeeId);
                            break;
                        }
                    }
                }
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public boolean report(String employeeId, String taskId) {
        EmployeeTaskManagementImpl employeeTaskManagement = new EmployeeTaskManagementImpl();
        ArrayList<EmployeeTaskInfo> employeeTaskInfos = employeeTaskManagement.showCompletedTasks(employeeId);
        EmployeeTaskInfo thisEmployeeTaskInfo = new EmployeeTaskInfo();
        for (EmployeeTaskInfo employeeTaskInfo : employeeTaskInfos) {
            if (employeeTaskInfo.getTaskId().equals(taskId)) {
                thisEmployeeTaskInfo = employeeTaskInfo;
                break;
            }
        }
        if (thisEmployeeTaskInfo.getReported()) {
            return false;
        } else {
            EmployeeInfoManagementImpl employeeInfoManagement = new EmployeeInfoManagementImpl();
            EmployeeInfo employeeInfo = employeeInfoManagement.showEmployeeInfo(employeeId);
            employeeInfo.setReportedTimes(employeeInfo.getReportedTimes() + 1);
            employeeInfoManagement.modifyEmployeeInfo(employeeInfo);

            thisEmployeeTaskInfo.setReported(true);
            employeeTaskManagement.saveEmployeeTaskInfo(thisEmployeeTaskInfo);
            return true;
        }
    }
}