package stz.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stz.backend.entity.Coordinate;
import stz.backend.entity.PictureTag;
import stz.backend.service.AnnotationManagementService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class AnnotationManagementTest {
    @Autowired
    private AnnotationManagementService annotationManagementService;
    private PictureTag pictureTag = new PictureTag();

    @Test
    public void testSavePictureTag() {
        ArrayList<Coordinate> border = new ArrayList<>();
        border.add(new Coordinate(1, 1));
        border.add(new Coordinate(2, 2));
        pictureTag = new PictureTag("111111", "222222", border, "somewords");
        assertEquals(true, annotationManagementService.savePictureTag("123456", "123456", pictureTag));
    }

    @Test
    public void testModifyPictureTag() {
        pictureTag.setAnnotation("some change");
        assertEquals(true, annotationManagementService.modifyPictureTag("123456", "123456", pictureTag));
    }

    @Test
    public void testDeleteOneTag() {
        assertEquals(true, annotationManagementService.deleteOneTag("123456", "123456", "111111", "123456"));
    }

    @Test
    public void testFindByPictureID() {
        annotationManagementService.savePictureTag("123456", "123456", pictureTag);
        ArrayList<String> result = annotationManagementService.findByPictureID("123456", "123456", "111111");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("222222");
        assertEquals(expected, result);
    }

    @Test
    public void testFindByTagID() {
        assertEquals(pictureTag, annotationManagementService.findByTagID("123456", "123456", "111111", "222222"));
    }
}
