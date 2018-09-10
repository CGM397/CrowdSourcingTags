package stz.backend.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/adminLogin")
    public String adminLogin(){
        return "adminLogin";
    }

    @RequestMapping("/CrowdSourcingTags")
    public String registerLogin(){
        return "login";
    }

    @RequestMapping("/employer-home")
    public String employerHome(){
        return "employer-home";
    }

    @RequestMapping("/employer-info")
    public String employerInfo(){
        return "employer-info";
    }

    @RequestMapping("/employer-underway")
    public String employerUnderway(){
        return "employer-underway";
    }

    @RequestMapping("/employer-completed")
    public String employerCompleted(){
        return "employer-completed";
    }

    @RequestMapping("/employer-contact")
    public String employerContact(){
        return "employer-contact";
    }

    @RequestMapping("/employer-taskDetail")
    public String employerTaskDetail(){
        return "employer-taskDetail";
    }

    @RequestMapping("/employee-accept")
    public String employeeAccept(){
        return "employee-accept";
    }

    @RequestMapping("/employer-data")
    public String employerData(){
        return "employer-data";
    }

    @RequestMapping("/employer-assess")
    public String employerAssess(){
        return "employer-assess";
    }

    @RequestMapping("/openPublication")
    public String employerOpenPublication(){
        return "openPublication";
    }

    @RequestMapping("/specifiedPublication")
    public String employerSpecifiedPublication(){
        return "specifiedPublication";
    }

    @RequestMapping("/cutPartPublication")
    public String employerCutPartPublication(){
        return "cutPartPublication";
    }

    @RequestMapping("/employee-home")
    public String employeeHome(){
        return "employee-home";
    }

    @RequestMapping("/employee-exam")
    public String employeeExam(){
        return "employee-exam";
    }

    @RequestMapping("/employee-info")
    public String employeeInfo(){
        return "employee-info";
    }

    @RequestMapping("/employee-underway")
    public String employeeUnderway(){
        return "employee-underway";
    }

    @RequestMapping("/employee-completed")
    public String employeeCompleted(){
        return "employee-completed";
    }

    @RequestMapping("/employee-contact")
    public String employeeContact(){
        return "employee-contact";
    }

    @RequestMapping("/employee-canvas")
    public String employeeCanvas(){
        return "employee-canvas";
    }

    @RequestMapping("/employee-taskDetail")
    public String employeeTaskDetail(){
        return "employee-taskDetail";
    }

    @RequestMapping("/employee-newTask")
    public String employeeNewTask(){
        return "employee-newTask";
    }

    @RequestMapping("/employee-data")
    public String employeeData(){
        return "employee-data";
    }
}
