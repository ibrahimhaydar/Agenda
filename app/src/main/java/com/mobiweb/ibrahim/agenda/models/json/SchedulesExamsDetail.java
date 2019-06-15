package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 4/28/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Info;

import java.util.List;

public class SchedulesExamsDetail {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("info")
    @Expose
    private List<Info> info = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

}