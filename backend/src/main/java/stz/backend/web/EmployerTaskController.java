package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.*;
import stz.backend.enums.ReleaseType;
import stz.backend.service.EmployerTaskManagementService;
import stz.backend.serviceImpl.EmployerTaskManagementImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/EmployerTask")
public class EmployerTaskController implements EmployerTaskManagementService {

    private EmployerTaskManagementImpl employerTask = new EmployerTaskManagementImpl();

    @Override
    @RequestMapping(value = "/getNewTaskId", method = RequestMethod.POST)
    @ResponseBody
    public String getNewTaskId() {
        return employerTask.getNewTaskId();
    }

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public boolean releaseTask(@RequestBody TaskInfo taskInfo){
        ArrayList<Picture> picture = taskInfo.getPictureCode();
        TaskToGet taskToGet = taskInfo.getTask();
        String type = taskToGet.getReleaseType();
        ReleaseType releaseType = ReleaseType.APPOINTED;
        if(type.equals("指定委派")){
            releaseType = ReleaseType.APPOINTED;
        }else if(type.equals("自由委派")){
            releaseType = ReleaseType.NON_APPOINTED;
        }else {
            releaseType = ReleaseType.SHARED;
        }
        Task task = new Task(taskToGet.getIsFull(), taskToGet.getTaskId(), taskToGet.getTaskName(),
                             taskToGet.getTaskType(),taskToGet.getEmployerId(),releaseType,
                             taskToGet.getStartTime(),taskToGet.getLimitTime(),taskToGet.getNeededEmployeeAmount(),
                             taskToGet.getTotalPictureAmount(),taskToGet.getAward(),
                             taskToGet.getOverallPictureId(),taskToGet.getRectanglePictureId(),
                             taskToGet.getBoundaryPictureId(),taskToGet.getPictureAcceptedTimes(),
                             taskToGet.getAppointedEmployeeId(), taskToGet.getMarks());
        return release(picture, task);
    }

    @Override
    public boolean release(@RequestParam ArrayList<Picture> picture, @RequestBody Task taskInfo) {
        return employerTask.release(picture, taskInfo);
    }

    @Override
    @RequestMapping(value = "/showCompletedTasks", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployerTaskInfo> showCompletedTasks(@RequestParam String employerId) {
        return employerTask.showCompletedTasks(employerId);
    }

    @Override
    @RequestMapping(value = "/showUnderwayTasks", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployerTaskInfo> showUnderwayTasks(@RequestParam String employerId) {
        return employerTask.showUnderwayTasks(employerId);
    }

    @Override
    @RequestMapping(value = "/checkOneTask", method = RequestMethod.POST)
    @ResponseBody
    public Task checkOneTask(@RequestParam String taskId) {
        return employerTask.checkOneTask(taskId);
    }

    @Override
    @RequestMapping(value = "/getPictureBase64Code", method = RequestMethod.POST)
    @ResponseBody
    public String getPictureBase64Code(@RequestParam String taskId, @RequestParam String pictureId) {
        return employerTask.getPictureBase64Code(taskId,pictureId);
    }

    @Override
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public boolean submit(@RequestParam String employerId, @RequestParam String taskId) {
        return employerTask.submit(employerId, taskId);
    }

    @Override
    @RequestMapping(value = "/getParticipateEmployee", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<String> getParticipateEmployee(@RequestParam String taskId, @RequestParam String pictureId) {
        return employerTask.getParticipateEmployee(taskId, pictureId);
    }

    @Override
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public boolean report(@RequestParam String employeeId,@RequestParam String taskId) {
        return employerTask.report(employeeId,taskId);
    }

    @RequestMapping(value = "/showOneTaskInfo", method = RequestMethod.POST)
    @ResponseBody
    public EmployerTaskInfo showOneTaskInfo(@RequestParam String employerId, @RequestParam String taskId){
        ArrayList<EmployerTaskInfo> result = new ArrayList<EmployerTaskInfo>();
        ArrayList<EmployerTaskInfo> store = new ArrayList<EmployerTaskInfo>();
        result.addAll(showCompletedTasks(employerId));
        result.addAll(showUnderwayTasks(employerId));
        for(int i = 0; i < result.size(); i++){
            if(result.get(i).getTaskId().equals(taskId)){
                store.add(result.get(i));
            }
        }
        if(store.size()>0)
            return store.get(0);
        return null;
    }
}
