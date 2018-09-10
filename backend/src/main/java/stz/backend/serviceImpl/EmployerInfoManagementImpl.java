package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.EmployerInfo;
import stz.backend.service.EmployerInfoManagementService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployerInfoManagementImpl implements EmployerInfoManagementService {

    @Override
    public EmployerInfo showEmployerInfo(String employerId) {
        EmployerInfo result = new EmployerInfo();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employerInfo WHERE employerId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, employerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = new EmployerInfo(rs.getString("employerName"),
                        rs.getString("employerId"),
                        rs.getString("employerPassword"),
                        rs.getString("employerEmail"),
                        rs.getString("dpId"),
                        rs.getInt("taskAmount"),
                        rs.getInt("taskUnderway"),
                        rs.getInt("taskCompleted"));
            }
            BaseDao.closeAll(conn, stmt, rs);
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public boolean modifyEmployerInfo(EmployerInfo info) {
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employerInfo WHERE employerId = ?";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, info.getEmployerId());
            ResultSet rs = stmt1.executeQuery();
            if(!rs.next()){
                return false;
            }
            else{
                String delete = "DELETE FROM employerInfo WHERE employerId = ?";
                PreparedStatement stmt2 = conn.prepareStatement(delete);
                stmt2.setString(1, info.getEmployerId());
                stmt2.executeUpdate();
                stmt2.close();
            }
            BaseDao.closeAll(conn, stmt1,rs);
        }catch (Exception e){

        }
        RegisterLoginImpl registerLogin = new RegisterLoginImpl();
        return registerLogin.insertEmployerInfo(info);
    }
}
