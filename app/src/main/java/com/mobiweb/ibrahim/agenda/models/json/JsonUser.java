package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.User;

import java.util.List;

public class JsonUser {


    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("status")
    @Expose
    private String status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}