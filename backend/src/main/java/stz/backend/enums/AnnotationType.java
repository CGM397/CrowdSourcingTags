package stz.backend.enums;

public enum AnnotationType {
    OVERALL("整体标注"),
    RECTANGLE("方框标注"),
    BOUNDARY("区域标注");

    String value;
    AnnotationType(String value){
        this.value = value;
    }

    public static String transToString(AnnotationType type){
        if(type.equals(OVERALL))
            return "整体标注";
        if(type.equals(RECTANGLE))
            return "方框标注";
        if(type.equals(BOUNDARY))
            return "区域标注";
        else
            return "Not Found";
    }

    public static AnnotationType transToAnnotationType(String annotation){
        if(annotation.equals("整体标注"))
            return AnnotationType.OVERALL;
        if(annotation.equals("方框标注"))
            return AnnotationType.RECTANGLE;
        if(annotation.equals("区域标注"))
            return AnnotationType.BOUNDARY;
        else
            return null;
    }
}
