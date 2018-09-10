package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.JudgeResult;
import stz.backend.entity.EmployeeTaskInfo;
import stz.backend.entity.Task;
import stz.backend.enums.AnnotationType;
import stz.backend.service.EmployeeTaskManagementService;
import stz.backend.serviceImpl.EmployeeTaskManagementImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/EmployeeTask")
public class EmployeeTaskController implements EmployeeTaskManagementService {

    private EmployeeTaskManagementImpl employeeTask = new EmployeeTaskManagementImpl();

    @Override
    @RequestMapping(value = "/showAvailableTasks", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<String> showAvailableTasks() {
        return employeeTask.showAvailableTasks();
    }

    @Override
    @RequestMapping(value = "/showUnderwayTasks", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployeeTaskInfo> showUnderwayTasks(@RequestParam String employeeId) {
        return employeeTask.showUnderwayTasks(employeeId);
    }

    @Override
    @RequestMapping(value = "/showCompletedTasks", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<EmployeeTaskInfo> showCompletedTasks(@RequestParam String employeeId) {
        return employeeTask.showCompletedTasks(employeeId);
    }

    @Override
    @RequestMapping(value = "/checkOneTask", method = RequestMethod.POST)
    @ResponseBody
    public Task checkOneTask(@RequestParam String taskId) {
        return employeeTask.checkOneTask(taskId);
    }

    @Override
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public boolean select(@RequestParam String employeeId, @RequestParam  String taskId) {
        return employeeTask.select(employeeId, taskId);
    }

    @Override
    @RequestMapping(value = "/partSelect", method = RequestMethod.POST)
    @ResponseBody
    public boolean partSelect(@RequestParam String employeeId, @RequestParam String taskId, @RequestParam int selectedPictureNum) {
        return employeeTask.partSelect(employeeId, taskId, selectedPictureNum);
    }

    @Override
    @RequestMapping(value = "/getRestPictures", method = RequestMethod.POST)
    @ResponseBody
    public int getRestPictures(@RequestParam String taskId) {
        return employeeTask.getRestPictures(taskId);
    }

    @Override
    @RequestMapping(value = "/giveUp", method = RequestMethod.POST)
    @ResponseBody
    public boolean giveUp(@RequestParam String employeeId, @RequestParam String taskId) {
        return employeeTask.giveUp(employeeId, taskId);
    }

    @Override
    @RequestMapping(value = "/showPictureAnnotationType", method = RequestMethod.POST)
    @ResponseBody
    public AnnotationType showPictureAnnotationType(@RequestParam String taskId, @RequestParam String pictureId) {
        return employeeTask.showPictureAnnotationType(taskId, pictureId);
    }

    @Override
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public boolean submit(@RequestParam String employeeId, @RequestParam String taskId) {
        return employeeTask.submit(employeeId, taskId);
    }

    @Override
    @RequestMapping(value = "/getAward", method = RequestMethod.POST)
    @ResponseBody
    public boolean getAward(@RequestParam String employeeId, @RequestParam String taskId) {
        return employeeTask.getAward(employeeId, taskId);
    }

    @Override
    @RequestMapping(value = "/getPictureBase64Code", method = RequestMethod.POST)
    @ResponseBody
    public String getPictureBase64Code(@RequestParam String taskId, @RequestParam String pictureId) {
        return employeeTask.getPictureBase64Code(taskId, pictureId);
    }

    @Override
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<String> recommend(@RequestParam String employeeId) {
        return employeeTask.recommend(employeeId);
    }

    @Override
    @RequestMapping(value = "/recommendSimilar", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<String> recommendSimilar(@RequestParam String employeeId,@RequestParam String taskId) {
        return employeeTask.recommendSimilar(employeeId,taskId);
    }

    @Override
    public void assessOverall(String taskId, String pictureId) {
        employeeTask.assessOverall(taskId,pictureId);
    }

    @Override
    public void assessRectangle(String prefix, String taskId, String pictureId) {
        employeeTask.assessRectangle(prefix, taskId, pictureId);
    }

    @Override
    public void assessBoundary(String taskId, String pictureId) {
        employeeTask.assessBoundary(taskId,pictureId);
    }

    @Override
    @RequestMapping(value = "/getJudgeResult", method = RequestMethod.POST)
    @ResponseBody
    public JudgeResult getJudgeResult(@RequestParam String taskId,@RequestParam String pictureId) {
        return employeeTask.getJudgeResult(taskId, pictureId);
    }

    @RequestMapping(value = "/showOneTaskInfo", method = RequestMethod.POST)
    @ResponseBody
    public EmployeeTaskInfo showOneTaskInfo(@RequestParam String employeeId, @RequestParam String taskId){
        ArrayList<EmployeeTaskInfo> store = new ArrayList<EmployeeTaskInfo>();
        ArrayList<EmployeeTaskInfo> result = new ArrayList<EmployeeTaskInfo>();
        store.addAll(showUnderwayTasks(employeeId));
        store.addAll(showCompletedTasks(employeeId));
        for(int i = 0; i < store.size(); i++){
            if(store.get(i).getTaskId().equals(taskId)){
                result.add(store.get(i));
            }
        }
        return result.get(0);
    }
}
