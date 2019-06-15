package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.teacher_class;

import java.util.ArrayList;

public class JsonTeacherClasses {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("classes")
    @Expose
    private ArrayList<teacher_class> classes = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<teacher_class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<teacher_class> classes) {
        this.classes = classes;
    }

}