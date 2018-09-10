package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.EmployeeTaskInfo;
import stz.backend.enums.AnnotationType;
import stz.backend.service.EmployeeTaskManagementService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeTaskManagementTest {
    @Autowired
    private EmployeeTaskManagementService employeeTaskManagementService;
    private EmployeeTaskInfo employeeTaskInfo = new EmployeeTaskInfo("654321", "111111", "1", "12", 0, 100, null,null,false,false,null,false);

    @Test
    public void testShowAvailableTasks() {
        assertEquals(employeeTaskInfo, employeeTaskManagementService.showAvailableTasks().get(0));
    }

    @Test
    public void testShowUnderwayTasks() {
        assertEquals(employeeTaskInfo, employeeTaskManagementService.showUnderwayTasks("654321").get(0));
    }

    @Test
    public void testShowCompletedTasks() {
        assertEquals(null, employeeTaskManagementService.showCompletedTasks("654321"));
    }

    @Test
    public void testCheckOneTask() {
        assertEquals(employeeTaskInfo, employeeTaskManagementService.checkOneTask("111111"));
    }

    @Test
    public void testSelect() {
        assertEquals(true, employeeTaskManagementService.select("654321", "111111"));
    }

    @Test
    public void testGiveUp() {
        assertEquals(true, employeeTaskManagementService.giveUp("654321", "111111"));
    }

    @Test
    public void testShowPictureAnnotationType() {
        assertEquals(AnnotationType.RECTANGLE, employeeTaskManagementService.showPictureAnnotationType("654321", "111111"));
    }

    @Test
    public void testSubmit() {
        assertEquals(true, employeeTaskManagementService.submit("654321", "111111"));
    }

    @Test
    public void testGetAward() {
        assertEquals(true, employeeTaskManagementService.getAward("654321", "111111"));
    }

    @Test
    public void testGetPictureBase64Code() {
        assertEquals(null, employeeTaskManagementService.getPictureBase64Code("111111", "111111"));
    }
}
