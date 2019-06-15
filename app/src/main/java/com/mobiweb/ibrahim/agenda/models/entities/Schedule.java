package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 3/20/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("schedual_id")
    @Expose
    private String schedualId;

    @SerializedName("schedule_id")
    @Expose
    private String schedule_id;

    @SerializedName("schedule_url")
    @Expose
    private String schedule_url;

    public String getSchedualId() {
        return schedualId;
    }

    public void setSchedualId(String schedualId) {
        this.schedualId = schedualId;
    }

    public String getSchedule_url() {
        return schedule_url;
    }

    public void setSchedule_url(String schedule_url) {
        this.schedule_url = schedule_url;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }
}