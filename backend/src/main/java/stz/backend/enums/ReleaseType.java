package stz.backend.enums;

public enum ReleaseType {
    APPOINTED("指定委派"),
    NON_APPOINTED("自由委派"),
    SHARED("分块发布");

    String value;
    ReleaseType(String value){
        this.value = value;
    }

    public static String transToString(ReleaseType type){
        if(type.equals(APPOINTED))
            return "指定委派";
        if(type.equals(NON_APPOINTED))
            return "自由委派";
        if(type.equals(SHARED))
            return "分块发布";
        else
            return "Not Found";
    }

    public static ReleaseType transToReleaseType(String job){
        if(job.equals("指定委派"))
            return ReleaseType.APPOINTED;
        if(job.equals("自由委派"))
            return ReleaseType.NON_APPOINTED;
        if(job.equals("分块发布"))
            return ReleaseType.SHARED;
        else
            return null;
    }
}
