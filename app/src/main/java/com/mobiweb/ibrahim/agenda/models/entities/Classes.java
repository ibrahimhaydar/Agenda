package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 11/8/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classes {

    @SerializedName("id_teacher")
    @Expose
    private String idTeacher;
    @SerializedName("id_course")
    @Expose
    private String idCourse;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("id_class")
    @Expose
    private String idClass;
    @SerializedName("id_section")
    @Expose
    private String idSection;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

}