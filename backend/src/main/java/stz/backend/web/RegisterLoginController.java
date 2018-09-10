package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.enums.ProfessionType;
import stz.backend.service.RegisterLoginService;
import stz.backend.serviceImpl.RegisterLoginImpl;

@RestController
@RequestMapping("/RegisterLogin")
public class RegisterLoginController implements RegisterLoginService {

    private RegisterLoginImpl registerLogin = new RegisterLoginImpl();

    @Override
    @RequestMapping(value = "/employerRegister", method = RequestMethod.POST)
    @ResponseBody
    public boolean EmployerRegister(@RequestBody EmployerInfo info) {
        return registerLogin.EmployerRegister(info);
    }

    @Override
    @RequestMapping(value = "/employeeRegister", method = RequestMethod.POST)
    @ResponseBody
    public boolean EmployeeRegister(@RequestBody EmployeeInfo info) {
        return registerLogin.EmployeeRegister(info);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public boolean convertAndLogin(@RequestParam String profession, @RequestParam String id, @RequestParam String password) {
        ProfessionType type = ProfessionType.EMPLOYEE;
        if(profession.equals("众包工人")){
            type = ProfessionType.EMPLOYEE;
        }
        else if(profession.equals("众包发起者")){
            type = ProfessionType.EMPLOYER;
        }
        return login(type, id, password);
    }

    @Override
    public boolean login(ProfessionType profession, String id, String password) {
        return registerLogin.login(profession, id, password);
    }

    @Override
    @RequestMapping(value = "/checkUserId", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkUserId(@RequestParam String userId) {
        return registerLogin.checkUserId(userId);
    }
}
