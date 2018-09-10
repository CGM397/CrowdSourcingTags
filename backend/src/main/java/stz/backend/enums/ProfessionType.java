package stz.backend.enums;

public enum ProfessionType {
    EMPLOYER("众包发起者"),
    EMPLOYEE("众包工人");

    String value;

    ProfessionType(String value){
        this.value = value;
    }

    public static String transToString(ProfessionType type){
        if(type.equals(EMPLOYER))
            return "众包发起者";
        if(type.equals(EMPLOYEE))
            return "众包工人";
        else
            return "Not Found";
    }

    public static ProfessionType transToProfession(String job){
        if(job.equals("众包发起者"))
            return ProfessionType.EMPLOYER;
        if(job.equals("众包工人"))
            return ProfessionType.EMPLOYEE;
        else
            return null;
    }
}
