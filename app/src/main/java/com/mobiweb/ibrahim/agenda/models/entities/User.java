package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 11/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("father_name")
    @Expose
    private String fatherName;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("father_last_name")
    @Expose
    private String fatherLastName;


    @SerializedName("parent_type")
    @Expose
    private String parent_type;

    @SerializedName("mother_name")
    @Expose
    private String motherName;
    @SerializedName("mother_last_name")
    @Expose
    private String motherLastName;
    @SerializedName("schedule")
    @Expose
    private String schedule;


    @SerializedName("schedule_teacher")
    @Expose
    private String schedule_teacher;

    @SerializedName("enable_grade")
    @Expose
    private String enable_grade;

    @SerializedName("enable_attendance")
    @Expose
    private String enable_attendance;

    @SerializedName("enable_evaluation")
    @Expose
    private String enable_evaluation;






    @SerializedName("childs")
    @Expose
    private ArrayList<Child> childs = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public String getSchedule_teacher() {
        return schedule_teacher;
    }

    public void setSchedule_teacher(String schedule_teacher) {
        this.schedule_teacher = schedule_teacher;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public ArrayList<Child> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Child> childs) {
        this.childs = childs;
    }


    public String getEnable_grade() {
        return enable_grade;
    }

    public void setEnable_grade(String enable_grade) {
        this.enable_grade = enable_grade;
    }

    public String getEnable_attendance() {
        return enable_attendance;
    }

    public void setEnable_attendance(String enable_attendance) {
        this.enable_attendance = enable_attendance;
    }

    public String getEnable_evaluation() {
        return enable_evaluation;
    }

    public void setEnable_evaluation(String enable_evaluation) {
        this.enable_evaluation = enable_evaluation;
    }

    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}