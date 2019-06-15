package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 4/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("id_schedule")
    @Expose
    private String idSchedule;
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

    @SerializedName("teacherAdd")
    @Expose
    private boolean teacherAdd;

    public boolean isTeacherAdd() {
        return teacherAdd;
    }

    public void setTeacherAdd(boolean teacherAdd) {
        this.teacherAdd = teacherAdd;
    }

    private boolean beforeIsBreak=false,afterIsBreak=false;

    public String getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(String idSchedule) {
        this.idSchedule = idSchedule;
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

    public boolean isBeforeIsBreak() {
        return beforeIsBreak;
    }

    public void setBeforeIsBreak(boolean beforeIsBreak) {
        this.beforeIsBreak = beforeIsBreak;
    }

    public boolean isAfterIsBreak() {
        return afterIsBreak;
    }

    public void setAfterIsBreak(boolean afterIsBreak) {
        this.afterIsBreak = afterIsBreak;
    }
}