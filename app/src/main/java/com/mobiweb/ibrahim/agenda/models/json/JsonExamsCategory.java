package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 4/14/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.AllCategory;

import java.util.ArrayList;

public class JsonExamsCategory {

    @SerializedName("AllCategories")
    @Expose
    private ArrayList<AllCategory> allCategories = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<AllCategory> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(ArrayList<AllCategory> allCategories) {
        this.allCategories = allCategories;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}