package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 11/29/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("status")
    @Expose
    private String result;

    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}