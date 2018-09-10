package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.EmployeeInfo;
import stz.backend.service.EmployeeInfoManagementService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeInfoManagementTest {
    @Autowired
    private EmployeeInfoManagementService employeeInfoManagementService;
    private EmployeeInfo employeeInfo = new EmployeeInfo("name", "654321", "password", "email@mail.com", "123456", 100, 100, 100, 0,null,0);

    @Test
    public void testShowEmployeeInfo() {
        assertEquals(employeeInfo, employeeInfoManagementService.showEmployeeInfo("654321"));
    }

    @Test
    public void testModifyEmployeeInfo() {
        EmployeeInfo newEmployeeInfo = employeeInfo;
        newEmployeeInfo.setEmployeeName("name1");
        assertEquals(employeeInfo, employeeInfoManagementService.modifyEmployeeInfo(newEmployeeInfo));
    }
}
