package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.entity.Message;
import stz.backend.enums.ProfessionType;
import stz.backend.service.RegisterLoginService;

import java.sql.*;
import java.util.Date;

public class RegisterLoginImpl implements RegisterLoginService {
    MessageManagementImpl messageManagement = new MessageManagementImpl();

    public void createWelcomeMessage(String identity, String userId){
        Message message = new Message();
        message.setMessageId(new Date().getTime()+"");
        message.setUserId(userId);
        message.setReleaseTime(new java.util.Date());
        message.setTitle("欢迎新用户");
        message.setContent("欢迎新用户，您的身份为 "+identity+" "+userId);
        message.setIsRead(false);
        messageManagement.createMessage(message);
    }

    public void createExamMessage(String userId){
        Message message = new Message();
        message.setMessageId(new Date().getTime()+"");
        message.setUserId(userId);
        message.setReleaseTime(new java.util.Date());
        message.setTitle("入门考试通知");
        message.setContent("请尽快参加入门考试获取参加任务的资格");
        message.setIsRead(false);
        messageManagement.createMessage(message);
    }

    public boolean insertEmployerInfo(EmployerInfo info){
        try {
            Connection conn = BaseDao.getConnection();
            String sql = "INSERT INTO employerInfo (employerName,employerId,employerPassword,employerEmail," +
                    "dpId,taskAmount,taskUnderway,taskCompleted) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, info.getEmployerName());
            stmt1.setString(2, info.getEmployerId());
            stmt1.setString(3, info.getEmployerPassword());
            stmt1.setString(4, info.getEmployerEmail());
            stmt1.setString(5, info.getDpId());
            stmt1.setInt(6, info.getTaskAmount());
            stmt1.setInt(7, info.getTaskUnderway());
            stmt1.setInt(8, info.getTaskCompleted());
            int rs = stmt1.executeUpdate();
            conn.close();
            stmt1.close();
            if (rs > 0)
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean EmployerRegister(EmployerInfo info) {
        boolean result = insertEmployerInfo(info);
        if(result){
            createWelcomeMessage("众包发起者",info.getEmployerId());
        }
        return result;
    }

    public boolean insertEmployeeInfo(EmployeeInfo info){
        try {
            Connection conn = BaseDao.getConnection();
            String sql = "INSERT INTO employeeInfo (employeeName,employeeId,employeePassword,employeeEmail," +
                    "dpId,awardAmount,taskAmount,taskUnderway,taskCompleted,reportedTimes) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, info.getEmployeeName());
            stmt1.setString(2, info.getEmployeeId());
            stmt1.setString(3, info.getEmployeePassword());
            stmt1.setString(4, info.getEmployeeEmail());
            stmt1.setString(5, info.getDpId());
            stmt1.setInt(6, info.getAwardAmount());
            stmt1.setInt(7, info.getTaskAmount());
            stmt1.setInt(8, info.getTaskUnderway());
            stmt1.setInt(9, info.getTaskCompleted());
            stmt1.setInt(10, info.getReportedTimes());
            int rs = stmt1.executeUpdate();
            String sql2 = "INSERT INTO overallJudgement (employeeId,accuracy,efficiency,grade) VALUES (?,?,?,?)";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1,info.getEmployeeId());
            stmt2.setDouble(2,info.getJudgement().getAccuracy());
            stmt2.setDouble(3,info.getJudgement().getEfficiency());
            stmt2.setDouble(4,info.getJudgement().getGrade());
            int rs2 = stmt2.executeUpdate();
            conn.close();
            stmt1.close();
            stmt2.close();
            if (rs > 0 && rs2 > 0)
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean EmployeeRegister(EmployeeInfo info) {
        boolean result = insertEmployeeInfo(info);
        if(result){
            createWelcomeMessage("众包工人",info.getEmployeeId());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            createExamMessage(info.getEmployeeId());
        }
        return result;
    }

    @Override
    public boolean login(ProfessionType profession, String id, String password) {
        try {
            Connection conn = BaseDao.getConnection();
            PreparedStatement stmt1;
            ResultSet rs;
            if (profession.equals(ProfessionType.EMPLOYER)) {
                String sql = "SELECT * FROM employerInfo WHERE employerId = ? AND employerPassword = ?";
                stmt1 = conn.prepareStatement(sql);
                stmt1.setString(1, id);
                stmt1.setString(2, password);
                rs = stmt1.executeQuery();
            } else {
                String sql = "SELECT * FROM employeeInfo WHERE employeeId = ? AND employeePassword = ?";
                stmt1 = conn.prepareStatement(sql);
                stmt1.setString(1, id);
                stmt1.setString(2, password);
                rs = stmt1.executeQuery();
            }

            if (rs.next()) {
                BaseDao.closeAll(conn, stmt1, rs);
                return true;
            } else {
                BaseDao.closeAll(conn, stmt1, rs);
                return false;
            }
        }catch (Exception e){

        }
        return false;

    }

    @Override
    public boolean checkUserId(String userId) {
        try {
            Connection conn = BaseDao.getConnection();
            String checkEmployee = "SELECT * FROM employeeInfo WHERE employeeId = ?";
            String checkEmployer = "SELECT * FROM employerInfo WHERE employerId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(checkEmployee);
            PreparedStatement stmt2 = conn.prepareStatement(checkEmployer);
            stmt1.setString(1, userId);
            stmt2.setString(1, userId);
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            if (rs1.next() || rs2.next()) {
                conn.close();
                stmt1.close();
                stmt2.close();
                rs1.close();
                rs2.close();
                return false;
            } else {
                conn.close();
                stmt1.close();
                stmt2.close();
                rs1.close();
                rs2.close();
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }
}