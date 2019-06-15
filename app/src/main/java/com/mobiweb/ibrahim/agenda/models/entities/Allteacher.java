package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 12/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allteacher {

    @SerializedName("id_teacher")
    @Expose
    private String idTeacher;
    @SerializedName("teacher_name")
    @Expose
    private String teacherName;

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

}