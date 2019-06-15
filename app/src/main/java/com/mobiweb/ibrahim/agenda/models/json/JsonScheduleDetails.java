package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 4/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonScheduleDetails {

    @SerializedName("schedules_exams_details")
    @Expose
    private ArrayList<SchedulesExamsDetail> schedulesExamsDetails = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<SchedulesExamsDetail> getSchedulesExamsDetails() {
        return schedulesExamsDetails;
    }

    public void setSchedulesExamsDetails(ArrayList<SchedulesExamsDetail> schedulesExamsDetails) {
        this.schedulesExamsDetails = schedulesExamsDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}