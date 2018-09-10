package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.EmployeeInfo;
import stz.backend.service.EmployeeInfoManagementService;
import stz.backend.serviceImpl.EmployeeInfoManagementImpl;

@RestController
@RequestMapping("/EmployeeInfo")
public class EmployeeInfoController implements EmployeeInfoManagementService {

    private EmployeeInfoManagementImpl employee = new EmployeeInfoManagementImpl();

    @Override
    @RequestMapping(value = "/showEmployeeInfo", method = RequestMethod.POST)
    @ResponseBody
    public EmployeeInfo showEmployeeInfo(@RequestParam String employeeId) {
        return employee.showEmployeeInfo(employeeId);
    }

    @Override
    @RequestMapping(value = "/modifyEmployeeInfo", method = RequestMethod.POST)
    @ResponseBody
    public boolean modifyEmployeeInfo(@RequestBody EmployeeInfo info){
        return employee.modifyEmployeeInfo(info);
    }
}
