package com.mobiweb.ibrahim.agenda.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("id_student")
    @Expose
    private String idStudent;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_present")
    @Expose
    private String isPresent;

    @SerializedName("info")
    @Expose
    private String info="";

    @SerializedName("id_evaluation")
    @Expose
    private String id_evaluation="";

    @SerializedName("grade")
    @Expose
    private String grade="";


    public Student(String idStudent, String info, String id_evaluation,String name) {
        this.idStudent = idStudent;
        this.info = info;
        this.id_evaluation = id_evaluation;
        this.name=name;
    }

    public Student(String idStudent, String name, String grade) {
        this.idStudent = idStudent;
        this.name = name;
        this.grade = grade;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getId_evaluation() {
        return id_evaluation;
    }

    public void setId_evaluation(String id_evaluation) {
        this.id_evaluation = id_evaluation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}