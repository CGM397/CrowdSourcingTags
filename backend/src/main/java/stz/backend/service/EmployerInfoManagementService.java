package stz.backend.service;

import stz.backend.entity.EmployerInfo;

public interface EmployerInfoManagementService {

    public EmployerInfo showEmployerInfo(String employerId);

    public boolean modifyEmployerInfo(EmployerInfo info);
}
