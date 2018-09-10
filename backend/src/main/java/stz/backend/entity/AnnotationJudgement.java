package stz.backend.entity;

public class AnnotationJudgement {
    /**
     * 准确性，1分制
     */
    double accuracy;

    /**
     * 效率，按照完成时间先后排名
     */
    double efficiency;

    /**
     * 评分，对于一个任务或者所有接收过的任务的完成情况进行评价
     */
    double grade;

    public AnnotationJudgement(){}

    public AnnotationJudgement(double accuracy, double efficiency, double grade){
        this.accuracy = accuracy;
        this.efficiency = efficiency;
        this.grade = grade;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public double getGrade() {
        return grade;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
