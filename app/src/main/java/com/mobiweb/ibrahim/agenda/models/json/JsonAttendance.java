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

}