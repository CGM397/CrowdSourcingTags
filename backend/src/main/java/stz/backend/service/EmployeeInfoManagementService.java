package stz.backend.service;

import stz.backend.entity.EmployeeInfo;

public interface EmployeeInfoManagementService {

    public EmployeeInfo showEmployeeInfo(String employeeId);

    public boolean modifyEmployeeInfo(EmployeeInfo info);
}
