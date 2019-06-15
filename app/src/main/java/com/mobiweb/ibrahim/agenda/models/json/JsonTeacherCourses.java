package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.teacher_course;

import java.util.ArrayList;

public class JsonTeacherCourses {

    @SerializedName("courses")
    @Expose
    private ArrayList<teacher_course> courses = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<teacher_course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<teacher_course> courses) {
        this.courses = courses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}