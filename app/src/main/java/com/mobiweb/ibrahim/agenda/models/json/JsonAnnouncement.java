package com.mobiweb.ibrahim.agenda.models.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Announcements;

import java.util.ArrayList;

/**
 * Created by ibrahim on 2/28/2018.
 */

public class JsonAnnouncement {
    @SerializedName("announcement")
@Expose
private ArrayList<Announcements> announcement = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Announcements> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(ArrayList<Announcements> announcement) {
        this.announcement = announcement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}