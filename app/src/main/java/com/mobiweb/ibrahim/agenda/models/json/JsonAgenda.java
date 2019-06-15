package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 11/9/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Agenda;

import java.util.ArrayList;

public class JsonAgenda {

    @SerializedName("agenda")
    @Expose
    private ArrayList<Agenda> agenda = null;


    @SerializedName("status")
    @Expose
    private String status;


    public ArrayList<Agenda> getAgenda() {
        return agenda;
    }

    public void setAgenda(ArrayList<Agenda> agenda) {
        this.agenda = agenda;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}