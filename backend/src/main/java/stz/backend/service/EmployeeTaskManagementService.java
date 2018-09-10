package stz.backend.service;

import stz.backend.entity.JudgeResult;
import stz.backend.entity.EmployeeTaskInfo;
import stz.backend.entity.Task;
import stz.backend.enums.AnnotationType;

import java.util.ArrayList;

public interface EmployeeTaskManagementService {

    /**
     * 到Task里面去查，如果isFull=false，那就把taskId加到ArrayList<String>中
     * @return
     */
    public ArrayList<String> showAvailableTasks();

    /**
     * 到EmployeeTaskInfo里面去找
     * @param employeeId
     * @return
     */
    public ArrayList<EmployeeTaskInfo> showUnderwayTasks(String employeeId);

    public ArrayList<EmployeeTaskInfo> showCompletedTasks(String employeeId);
    /**
     * 点击一个任务编号，返回具体任务内容
     * @param taskId
     * @return
     */
    public Task checkOneTask(String taskId);

    /**
     * 生成一个EmployeeTaskInfo的TXT文件并保存
     * 同时用taskId到对应的Task里面找到对应的EmployerId
     * 用对应的EmployerId找到对应的EmployerTaskInfo
     * 将该employeeId加入到其中的 ArrayList<String> acceptedEmployeeId 中
     * 并且acceptedEmployeeNum加一，检测acceptedEmployeeNum等不等于neededEmployeeNum
     * 若相等，则将对应的Task里面的isFull设为true
     * @param employeeId
     * @param taskId
     * @return
     */
    public boolean select(String employeeId, String taskId);

    /**
     * 分块发布任务
     * @param employeeId
     * @param taskId
     * @param selectedPictureNum
     * @return
     */
    public boolean partSelect(String employeeId, String taskId, int selectedPictureNum);

    /**
     * 得到剩余的可被接收的图片的数量
     * @param taskId
     * @return
     */
    public int getRestPictures(String taskId);

    /**
     * 删除对应的EmployeeTaskInfo
     * 其他修改参数同上
     * @param employeeId
     * @param taskId
     * @return
     */
    public boolean giveUp(String employeeId, String taskId);

    /**
     * 到Task里面找
     * @param taskId
     * @param pictureId
     * @return
     */
    public AnnotationType showPictureAnnotationType(String taskId, String pictureId);

    /**
     * 该任务所有图片都已经标注完成才能提交
     * 将对应的EmployeeTaskInfo里面的isFinished设为true
     * 其他修改参数同上
     * @param employeeId
     * @param taskId
     * @return
     */
    public boolean submit(String employeeId, String taskId);

    /**
     * 修改对应的EmployeeInfo里面的awardAmount
     * @param employeeId
     * @param taskId
     * @return
     */
    public boolean getAward(String employeeId, String taskId);

    /**
     * 路径:data/pictures/taskId/pictureId.txt
     * @param taskId
     * @param pictureId
     * @return
     */
    public String getPictureBase64Code(String taskId, String pictureId);

    /**
     * 推荐任务
     * @param employeeId
     * @return
     */
    public ArrayList<String> recommend(String employeeId);

    /**
     * 在一个任务完成之后推荐相似任务
     * @param employeeId
     * @return
     */
    public ArrayList<String> recommendSimilar(String employeeId,String taskId);

    /**整体标注
     * 对于一个任务的一张图片的评价、整合
     * @param taskId
     */
    public void assessOverall(String taskId, String pictureId);

    /**方框标注
     * 对于一个任务的一张图片的评价、整合
     * @param taskId
     */
    public void assessRectangle(String prefix, String taskId, String pictureId);

    /**区域标注
     * 对于一个任务的一张图片的评价、整合
     * @param taskId
     */
    public void assessBoundary(String taskId, String pictureId);

    /**
     * 得到一个任务的一个图片的整合结果
     * @param taskId
     * @param pictureId
     * @return
     */
    public JudgeResult getJudgeResult(String taskId, String pictureId);
}
