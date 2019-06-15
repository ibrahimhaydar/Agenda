package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostGrades {
    @SerializedName("id_student")
    @Expose
    private String idStudent;


    @SerializedName("grade")
    @Expose
    private String grade;

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


    public PostGrades(String idStudent, String grade) {
        this.idStudent = idStudent;
        this.grade = grade;
    }
}