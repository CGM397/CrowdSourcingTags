package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.EmployerTaskInfo;
import stz.backend.service.EmployerTaskManagementService;
import stz.backend.serviceImpl.EmployerTaskManagementImpl;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployerTaskManagementTest {
    @Autowired
    private EmployerTaskManagementService employerTaskManagementService;
    private EmployerTaskInfo employerTaskInfo = new EmployerTaskInfo("123456", "111111", 100, 0, 0, null, null, false);

    @Test
    public void testGetNewTaskId() {
        EmployerTaskManagementImpl employerTaskManagementImpl = new EmployerTaskManagementImpl();
        assertEquals(employerTaskManagementImpl.getTime(), employerTaskManagementService.getNewTaskId());
    }

    @Test
    public void testRelease() {
        assertEquals(true, employerTaskManagementService.release(null, null));
    }

    @Test
    public void testShowCompletedTasks() {
        assertEquals(null, employerTaskManagementService.showCompletedTasks("123456"));
    }

    @Test
    public void testShowUnderwayTasks() {
        assertEquals(employerTaskInfo, employerTaskManagementService.showUnderwayTasks("123456").get(0));
    }

    @Test
    public void testCheckOneTask() {
        assertEquals(null, employerTaskManagementService.checkOneTask("123123"));
    }

    @Test
    public void testGetPictureBase64Code() {
        assertEquals(null, employerTaskManagementService.getPictureBase64Code("123123", "111111"));
    }
}
