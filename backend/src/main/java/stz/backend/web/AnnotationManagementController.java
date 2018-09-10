package stz.backend.web;

import org.springframework.web.bind.annotation.*;
import stz.backend.entity.PictureTag;
import stz.backend.entity.TagInfo;
import stz.backend.service.AnnotationManagementService;
import stz.backend.serviceImpl.AnnotationManagementImpl;

import java.util.ArrayList;

@RestController
@RequestMapping("/AnnotationManagement")
public class AnnotationManagementController implements AnnotationManagementService {

    private AnnotationManagementImpl management = new AnnotationManagementImpl();

    @RequestMapping(value = "/savePictureTag", method = RequestMethod.POST)
    @ResponseBody
    public boolean test(@RequestBody TagInfo tagInfo){
        String employeeId = tagInfo.getEmployeeId();
        String taskId = tagInfo.getTaskId();
        PictureTag pictureTag = tagInfo.getPictureTag();
        return savePictureTag(employeeId, taskId, pictureTag);
    }

    @Override
    public boolean savePictureTag(String employeeId, String taskId, PictureTag pictureTag) {
        return management.savePictureTag(employeeId, taskId, pictureTag);
    }

    @Override
    @RequestMapping(value = "/modifyPictureTag", method = RequestMethod.POST)
    @ResponseBody
    public boolean modifyPictureTag(@RequestParam String employeeId, @RequestParam String taskId, @RequestBody PictureTag pictureTag) {
        return management.modifyPictureTag(employeeId, taskId, pictureTag);
    }

    @Override
    @RequestMapping(value = "/deleteOneTag", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteOneTag(@RequestParam String employeeId, @RequestParam String taskId, @RequestParam String pictureID, @RequestParam String partID) {
        return management.deleteOneTag(employeeId, taskId, pictureID, partID);
    }

    @Override
    @RequestMapping(value = "/findByPictureID", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<String> findByPictureID(@RequestParam String employeeId, @RequestParam String taskId, @RequestParam String pictureID) {
        return management.findByPictureID(employeeId, taskId, pictureID);
    }

    @Override
    @RequestMapping(value = "/findByTagID", method = RequestMethod.POST)
    @ResponseBody
    public PictureTag findByTagID(@RequestParam String employeeId, @RequestParam String taskId, @RequestParam String pictureID, @RequestParam String tagID) {
        return management.findByTagID(employeeId, taskId, pictureID, tagID);
    }
}
