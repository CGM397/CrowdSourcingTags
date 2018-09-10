package stz.backend.entity;

public class Picture {
    String pictureId;
    String pictureBase64Code;

    public Picture(){}

    public Picture(String pictureId, String pictureBase64Code){
        this.pictureId = pictureId;
        this.pictureBase64Code = pictureBase64Code;
    }

    public String getPictureId() {
        return pictureId;
    }

    public String getPictureBase64Code() {
        return pictureBase64Code;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public void setPictureBase64Code(String pictureBase64Code) {
        this.pictureBase64Code = pictureBase64Code;
    }
}
