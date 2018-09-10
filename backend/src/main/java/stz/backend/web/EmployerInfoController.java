package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.EmployerInfo;
import stz.backend.service.EmployerInfoManagementService;
import stz.backend.serviceImpl.EmployerInfoManagementImpl;

@RestController
@RequestMapping("/EmployerInfo")
public class EmployerInfoController implements EmployerInfoManagementService {

    private EmployerInfoManagementImpl employer = new EmployerInfoManagementImpl();

    @Override
    @RequestMapping(value = "/showEmployerInfo", method = RequestMethod.POST)
    @ResponseBody
    public EmployerInfo showEmployerInfo(@RequestParam String employerId) {
        return employer.showEmployerInfo(employerId);
    }

    @Override
    @RequestMapping(value = "/modifyEmployerInfo", method = RequestMethod.POST)
    @ResponseBody
    public boolean modifyEmployerInfo(@RequestBody EmployerInfo info) {
        return employer.modifyEmployerInfo(info);
    }
}
