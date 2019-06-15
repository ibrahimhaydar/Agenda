package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 4/16/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.SchedulesExam;

import java.util.ArrayList;

public class JsonScheduleExam {

    @SerializedName("schedules_exams")
    @Expose
    private ArrayList<SchedulesExam> schedulesExams = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<SchedulesExam> getSchedulesExams() {
        return schedulesExams;
    }

    public void setSchedulesExams(ArrayList<SchedulesExam> schedulesExams) {
        this.schedulesExams = schedulesExams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}