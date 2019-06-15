package com.mobiweb.ibrahim.agenda.models.entities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoStudent {

    @SerializedName("id_student")
    @Expose
    private String idStudent;


    @SerializedName("name_student")
    @Expose
    private String name_student;

    @SerializedName("course_name")
    @Expose
    private String course_name;

    @SerializedName("evaluation")
    @Expose
    private String evaluation;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id_class")
    @Expose
    private String idClass;
    @SerializedName("id_section")
    @Expose
    private String idSection;
    @SerializedName("id_evaluation")
    @Expose
    private String idEvaluation;

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public String getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(String isEvaluation) {
        this.idEvaluation = isEvaluation;
    }

    public String getName_student() {
        return name_student;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }
}