package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/8/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Classes;

import java.util.List;

public class JsonGetClasses {

    @SerializedName("classes")
    @Expose
    private List<Classes> classes = null;

    public List<Classes> getClasses() {
        return classes;
    }

    public void setClasses(List<Classes> classes) {
        this.classes = classes;
    }

}