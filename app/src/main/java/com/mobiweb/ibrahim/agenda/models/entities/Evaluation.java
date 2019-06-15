package com.mobiweb.ibrahim.agenda.models.entities;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Evaluation {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("info")
    @Expose
    private ArrayList<InfoStudent> info = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<InfoStudent> getInfoStudent() {
        return info;
    }

    public void setInfoStudent(ArrayList<InfoStudent> info) {
        this.info = info;
    }

}