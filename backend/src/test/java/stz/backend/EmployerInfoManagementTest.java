package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.EmployerInfo;
import stz.backend.service.EmployerInfoManagementService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployerInfoManagementTest {
    @Autowired
    private EmployerInfoManagementService employerInfoManagementService;
    private EmployerInfo employerInfo = new EmployerInfo("name", "123456", "password", "email@mail.com", "123456", 100, 100, 0);

    @Test
    public void testShowEmployerInfo() {
        assertEquals(employerInfo, employerInfoManagementService.showEmployerInfo("123456"));
    }

    @Test
    public void testModifyEmployerInfo() {
        EmployerInfo newEmployerInfo = employerInfo;
        newEmployerInfo.setEmployerName("name1");
        assertEquals(employerInfo, employerInfoManagementService.modifyEmployerInfo(newEmployerInfo));
    }
}
