package com.mobiweb.ibrahim.agenda.models.json;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Evaluation;

public class JsonEvaluation {

    @SerializedName("evaluations")
    @Expose
    private ArrayList<Evaluation> evaluations = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(ArrayList<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}