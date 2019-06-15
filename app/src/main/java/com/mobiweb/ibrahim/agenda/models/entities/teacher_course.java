package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 11/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class teacher_course {

    @SerializedName("id_teacher")
    @Expose
    private String idTeacher;
    @SerializedName("id_course")
    @Expose
    private String idCourse;
    @SerializedName("name_course")
    @Expose
    private String nameCourse;

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getNameCourse() {
        return nameCourse;
    }

    public void setNameCourse(String nameCourse) {
        this.nameCourse = nameCourse;
    }

}