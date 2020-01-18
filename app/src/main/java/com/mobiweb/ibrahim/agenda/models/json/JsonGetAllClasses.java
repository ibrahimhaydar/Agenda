package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/8/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Allclass;

import java.util.ArrayList;

public class JsonGetAllClasses {

    @SerializedName("status")
    @Expose
    private String status;



    @SerializedName("Allclasses")
    @Expose
    private ArrayList<Allclass> allclasses = null;

    public ArrayList<Allclass> getAllclasses() {
        return allclasses;
    }

    public void setAllclasses(ArrayList<Allclass> allclasses) {
        this.allclasses = allclasses;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}