package com.mobiweb.ibrahim.agenda.models.json;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.Student;

public class JsonClassStudents {

    @SerializedName("students")
    @Expose
    private ArrayList<Student> students = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isEdit")
    @Expose
    private String isEdit;

    @SerializedName("max_grade")
    @Expose
    private String max_grade;

    @SerializedName("evaluation")
    @Expose
    private String evaluation;


    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getMax_grade() {
        return max_grade;
    }

    public void setMax_grade(String max_grade) {
        this.max_grade = max_grade;
    }
}