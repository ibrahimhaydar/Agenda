package com.mobiweb.ibrahim.agenda.models.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Videos;

import java.util.ArrayList;

/**
 * Created by ibrahim on 3/4/2018.
 */

public class JsonVideos {

    @SerializedName("videos")
    @Expose
    private ArrayList<Videos> videos = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Videos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Videos> videos) {
        this.videos = videos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}