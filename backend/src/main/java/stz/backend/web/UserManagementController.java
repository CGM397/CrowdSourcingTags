package stz.backend.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.service.UserManagementService;
import stz.backend.serviceImpl.UserManagementImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/UserManagement")
public class UserManagementController implements UserManagementService {

    private UserManagementImpl userManagement = new UserManagementImpl();

    @Override
    @RequestMapping(value = "/showEmployerInfo", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployerInfo> showEmployerInfo() {
        return userManagement.showEmployerInfo();
    }

    @Override
    @RequestMapping(value = "/showEmployeeInfo", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployeeInfo> showEmployeeInfo() {
        return userManagement.showEmployeeInfo();
    }
}
