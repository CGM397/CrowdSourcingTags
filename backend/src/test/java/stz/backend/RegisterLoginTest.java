package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.EmployeeInfo;
import stz.backend.entity.EmployerInfo;
import stz.backend.enums.ProfessionType;
import stz.backend.service.RegisterLoginService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class RegisterLoginTest {
    @Autowired
    private RegisterLoginService registerLoginService;
    private EmployerInfo employerInfo = new EmployerInfo("name", "123456", "password", "email@mail.com", "123456", 100, 100, 0);
    private EmployeeInfo employeeInfo = new EmployeeInfo("name", "654321", "password", "email@mail.com", "123456", 100, 100, 100, 0 ,null,0);

    @Test
    public void testEmployerRegister() {
        assertEquals(true, registerLoginService.EmployerRegister(employerInfo));
    }

    @Test
    public void testEmployeeRegister() {
        assertEquals(true, registerLoginService.EmployeeRegister(employeeInfo));
    }

    @Test
    public void testLogin() {
        assertEquals(true, registerLoginService.login(ProfessionType.EMPLOYER, "123456", "password"));
    }

    @Test
    public void testCheckUserId() {
        assertEquals(true, registerLoginService.checkUserId("123456"));
    }

}
