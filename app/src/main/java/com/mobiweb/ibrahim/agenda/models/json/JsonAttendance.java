package com.mobiweb.ibrahim.agenda.models.json;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Attendance;

public class JsonAttendance {

    @SerializedName("attendance")
    @Expose
    private ArrayList<Attendance> attendance = null;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("absence_count")
    @Expose
    private String absence_count;

    @SerializedName("present_count")
    @Expose
    private String present_count;




    public ArrayList<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<Attendance> attendance) {
        this.attendance = attendance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbsence_count() {
        return absence_count;
    }

    public void setAbsence_count(String absence_count) {
        this.absence_count = absence_count;
    }

    public String getPresent_count() {
        return present_count;
    }

    public void setPresent_count(String present_count) {
        this.present_count = present_count;
    }
}