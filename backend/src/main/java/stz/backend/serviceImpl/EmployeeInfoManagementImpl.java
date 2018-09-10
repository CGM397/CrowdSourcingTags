package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.AnnotationJudgement;
import stz.backend.entity.EmployeeInfo;
import stz.backend.service.EmployeeInfoManagementService;

import java.sql.*;


public class EmployeeInfoManagementImpl implements EmployeeInfoManagementService {
    @Override
    public EmployeeInfo showEmployeeInfo(String employeeId) {

        EmployeeInfo result = new EmployeeInfo();
        AnnotationJudgement annotationJudgement = new AnnotationJudgement();
        try {
            Connection conn = BaseDao.getConnection();

            String sql = "SELECT * FROM employeeInfo WHERE employeeId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, employeeId);
            ResultSet rs = stmt1.executeQuery();

            String overallJudgement = "SELECT * FROM overallJudgement WHERE employeeId = ?";
            PreparedStatement stmt2 = conn.prepareStatement(overallJudgement);
            stmt2.setString(1, employeeId);
            ResultSet overall = stmt2.executeQuery();
            if (overall.next()) {
                annotationJudgement = new AnnotationJudgement(overall.getDouble("accuracy"),
                        overall.getDouble("efficiency"),
                        overall.getDouble("grade"));
            }
            if (rs.next()) {
                result = new EmployeeInfo(rs.getString("employeeName"),
                        rs.getString("employeeId"),
                        rs.getString("employeePassword"),
                        rs.getString("employeeEmail"),
                        rs.getString("dpId"),
                        rs.getInt("awardAmount"),
                        rs.getInt("taskAmount"),
                        rs.getInt("taskUnderway"),
                        rs.getInt("taskCompleted"),
                        annotationJudgement,
                        rs.getInt("reportedTimes"));
            }
            BaseDao.closeAll(conn, stmt1, rs);
            stmt2.close();
            overall.close();
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public boolean modifyEmployeeInfo(EmployeeInfo info){
        try{
            Connection conn = BaseDao.getConnection();

            String sql = "SELECT * FROM employeeInfo WHERE employeeId = ?";
            String judgement = "SELECT * FROM overallJudgement WHERE employeeId = ?";

            PreparedStatement judgeStmt = conn.prepareStatement(judgement);
            judgeStmt.setString(1,info.getEmployeeId());

            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, info.getEmployeeId());

            ResultSet rs = stmt1.executeQuery();
            ResultSet judgeRs = judgeStmt.executeQuery();

            if(!rs.next() || !judgeRs.next()){
                return false;
            }
            else{
                String delete = "DELETE FROM employeeInfo WHERE employeeId = ?";
                String deleteJudge = "DELETE FROM overallJudgement WHERE employeeId = ?";

                PreparedStatement stmt2 = conn.prepareStatement(delete);
                PreparedStatement deleteStmt = conn.prepareStatement(deleteJudge);

                deleteStmt.setString(1,info.getEmployeeId());
                stmt2.setString(1, info.getEmployeeId());

                stmt2.executeUpdate();
                deleteStmt.executeUpdate();

                stmt2.close();
                deleteStmt.close();
            }
            conn.close();
            stmt1.close();
            rs.close();
            judgeStmt.close();
            judgeRs.close();
        }catch (Exception e){

        }
        RegisterLoginImpl registerLogin = new RegisterLoginImpl();
        return registerLogin.insertEmployeeInfo(info);
    }
}
