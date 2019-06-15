package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ibrahim on 4/18/2018.
 */

public class ScheduleExamPost {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("latest_year")
    @Expose
    private String latest_year;

    @SerializedName("id_class_exam")
    @Expose
    private String id_class_exam;

    @SerializedName("teacherAdd")
    @Expose
    private boolean teacherAdd;

    public ScheduleExamPost() {
        type="1";
        latest_year="1";
        date="";
        from="";
        to="";
        title="";
        id_class_exam="";
        teacherAdd=false;


    }

    public ScheduleExamPost(String date, String from, String to, String title, String type, String latest_year,boolean teacherAdd, String id_class_exam) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.title = title;
        this.type = type;
        this.latest_year = latest_year;
        this.teacherAdd=teacherAdd;
        this.id_class_exam = id_class_exam;
    }

    public boolean isTeacherAdd() {
        return teacherAdd;
    }

    public void setTeacherAdd(boolean teacherAdd) {
        this.teacherAdd = teacherAdd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatest_year() {
        return latest_year;
    }

    public void setLatest_year(String latest_year) {
        this.latest_year = latest_year;
    }

    public String getId_class_exam() {
        return id_class_exam;
    }

    public void setId_class_exam(String id_class_exam) {
        this.id_class_exam = id_class_exam;
    }
}