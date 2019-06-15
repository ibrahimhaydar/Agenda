package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 3/20/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Schedule;

import java.util.List;

public class JsonSchedules {

    @SerializedName("schedules")
    @Expose
    private List<Schedule> schedules = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}