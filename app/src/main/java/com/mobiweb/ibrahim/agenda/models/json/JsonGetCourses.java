package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/8/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Course;

import java.util.List;

public class JsonGetCourses {

    @SerializedName("courses")
    @Expose
    private List<Course> courses = null;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}