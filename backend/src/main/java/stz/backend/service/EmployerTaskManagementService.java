package stz.backend.service;

import stz.backend.entity.EmployerTaskInfo;
import stz.backend.entity.Picture;
import stz.backend.entity.Task;

import java.util.ArrayList;

public interface EmployerTaskManagementService {

    /**
     * 得到一个新的任务编号，所有众包发起者的任务id应各不相同
     * @return
     */
    public String getNewTaskId();

    /**
     * 在调用这个方法的同时，要生成一个对应的EmployerTaskInfo类并保存，
     * 存picture路径:data/pictures/taskId/pictureId.txt
     * @param taskInfo
     * @return
     */
    public boolean release(ArrayList<Picture> picture, Task taskInfo);

    /**
     * 到EmployerTaskInfo里面去查，返回的是任务的粗略信息
     * @param employerId
     * @return
     */

    public ArrayList<EmployerTaskInfo> showCompletedTasks(String employerId);

    public ArrayList<EmployerTaskInfo> showUnderwayTasks(String employerId);

    /**
     * 返回一个任务的具体内容
     * @param taskId
     * @return
     */
    public Task checkOneTask(String taskId);

    /**
     * 路径:data/pictures/taskId/pictureId.txt
     * @param taskId
     * @param pictureId
     * @return
     */
    public String getPictureBase64Code(String taskId, String pictureId);

    public boolean submit(String employerId, String taskId);

    public ArrayList<String> getParticipateEmployee(String taskId, String pictureId);

    public boolean report(String employeeId, String taskId);

}
