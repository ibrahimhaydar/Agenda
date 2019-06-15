package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Agenda {

    @SerializedName("hw_id")
    @Expose
    private String hw_id;

    @SerializedName("hw_title")
    @Expose
    private String hwTitle;
    @SerializedName("hw_desc")
    @Expose
    private String hwDesc;

    @SerializedName("hw_date")
    @Expose
    private String hw_date;

    @SerializedName("hw_image")
    @Expose
    private ArrayList<String> hwImage = null;

    @SerializedName("hw_info")
    @Expose
    private String hw_info;

    public String getHw_id() {
        return hw_id;
    }

    public void setHw_id(String hw_id) {
        this.hw_id = hw_id;
    }

    public String getHw_date() {
        return hw_date;
    }

    public void setHw_date(String hw_date) {
        this.hw_date = hw_date;
    }

    public String getHwTitle() {
        return hwTitle;
    }

    public void setHwTitle(String hwTitle) {
        this.hwTitle = hwTitle;
    }

    public String getHwDesc() {
        return hwDesc;
    }

    public void setHwDesc(String hwDesc) {
        this.hwDesc = hwDesc;
    }


    public ArrayList<String> getHwImage() {
        return hwImage;
    }

    public void setHwImage(ArrayList<String> hwImage) {
        this.hwImage = hwImage;
    }

    public String getHw_info() {
        return hw_info;
    }

    public void setHw_info(String hw_info) {
        this.hw_info = hw_info;
    }
}