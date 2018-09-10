package stz.backend.service;

import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;

import java.util.ArrayList;

/**
 * 管理者所用接口
 */
public interface UserManagementService {

    /**
     *
     * @return 所有众包发起者信息
     */
    public ArrayList<EmployerInfo> showEmployerInfo();

    /**
     *
     * @return 所有众包工人信息
     */
    public ArrayList<EmployeeInfo> showEmployeeInfo();

}
