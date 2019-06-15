package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Courses {
    @SerializedName("course_id")
    @Expose
    private String course_id;
    @SerializedName("course_name")
    @Expose
    private String course_name;


    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
