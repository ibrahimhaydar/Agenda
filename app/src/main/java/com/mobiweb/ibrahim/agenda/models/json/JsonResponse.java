package com.mobiweb.ibrahim.agenda.models.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Course;

import java.util.List;

public class JsonResponse {
    @SerializedName("message")
    @Expose
    private String message ;


    @SerializedName("status")
    @Expose
    private String status ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
