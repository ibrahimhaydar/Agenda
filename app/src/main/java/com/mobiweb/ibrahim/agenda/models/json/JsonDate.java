package com.mobiweb.ibrahim.agenda.models.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonDate {
    @SerializedName("date")
    @Expose
    private String server_date;

    public String getServer_date() {
        return server_date;
    }

    public void setServer_date(String server_date) {
        this.server_date = server_date;
    }
}
