package stz.backend.service;

import stz.backend.entity.PictureTag;

import java.util.ArrayList;

public interface AnnotationManagementService {

    public boolean savePictureTag(String employeeId, String taskId, PictureTag pictureTag);

    public boolean modifyPictureTag(String employeeId, String taskId, PictureTag pictureTag);

    /**
     * 需要修改的参数同上
     * @param employeeId
     * @param pictureID
     * @return
     */
    public boolean deleteOneTag(String employeeId, String taskId, String pictureID, String partID);

    public ArrayList<String> findByPictureID(String employeeId, String taskId, String pictureID);

    public PictureTag findByTagID(String employeeId, String taskId, String pictureID, String tagID);

}
