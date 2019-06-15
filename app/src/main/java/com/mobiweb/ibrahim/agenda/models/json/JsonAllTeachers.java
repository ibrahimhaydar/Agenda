package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 12/12/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Allteacher;

import java.util.ArrayList;

public class JsonAllTeachers {

    @SerializedName("Allteachers")
    @Expose
    private ArrayList<Allteacher> allteachers = null;

    public ArrayList<Allteacher> getAllteachers() {
        return allteachers;
    }

    public void setAllteachers(ArrayList<Allteacher> allteachers) {
        this.allteachers = allteachers;
    }

}