package stz.backend.serviceImpl;

import stz.backend.DAO.BaseDao;
import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.service.UserManagementService;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class UserManagementImpl implements UserManagementService {
    @Override
    public ArrayList<EmployerInfo> showEmployerInfo() {
        ArrayList<EmployerInfo> result = new ArrayList<>();
        try {
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employerInfo";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            ResultSet rs = stmt1.executeQuery();
            while(rs.next()){
                EmployerInfo employerInfo = new EmployerInfo(rs.getString("employerName"),
                        rs.getString("employerId"),
                        rs.getString("employerPassword"),
                        rs.getString("employerEmail"),
                        rs.getString("dpId"),
                        rs.getInt("taskAmount"),
                        rs.getInt("taskUnderway"),
                        rs.getInt("taskCompleted"));
                result.add(employerInfo);
            }
            BaseDao.closeAll(conn, stmt1, rs);
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public ArrayList<EmployeeInfo> showEmployeeInfo() {
        ArrayList<EmployeeInfo> result = new ArrayList<>();
        try{
            Connection conn = BaseDao.getConnection();
            String sql = "SELECT * FROM employeeInfo";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            ResultSet rs = stmt1.executeQuery();
            while(rs.next()){
                EmployeeInfoManagementImpl employeeInfoManagement = new EmployeeInfoManagementImpl();
                result.add(employeeInfoManagement.showEmployeeInfo(rs.getString("employeeId")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
