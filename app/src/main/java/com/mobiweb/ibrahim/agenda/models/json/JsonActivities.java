package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 1/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Activities;

import java.util.ArrayList;

public class JsonActivities {

    @SerializedName("activities")
    @Expose
    private ArrayList<Activities> activities = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Activities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activities> activities) {
        this.activities = activities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}