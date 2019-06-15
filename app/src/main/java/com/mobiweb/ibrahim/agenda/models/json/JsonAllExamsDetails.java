package com.mobiweb.ibrahim.agenda.models.json;

/**
 * Created by ibrahim on 5/12/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Courses;
import com.mobiweb.ibrahim.agenda.models.entities.Header;
import com.mobiweb.ibrahim.agenda.models.entities.ViewExam;

import java.util.ArrayList;

public class JsonAllExamsDetails {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("schedules_exams_details")
    @Expose
    private ArrayList<SchedulesExamsDetail> schedulesExamsDetails = null;
    @SerializedName("teachersExams")
    @Expose
    private ArrayList<ViewExam> teachersExams = null;


    @SerializedName("courses")
    @Expose
    private ArrayList<Courses> courses = null;


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ArrayList<SchedulesExamsDetail> getSchedulesExamsDetails() {
        return schedulesExamsDetails;
    }

    public void setSchedulesExamsDetails(ArrayList<SchedulesExamsDetail> schedulesExamsDetails) {
        this.schedulesExamsDetails = schedulesExamsDetails;
    }

    public ArrayList<ViewExam> getTeachersExams() {
        return teachersExams;
    }

    public void setTeachersExams(ArrayList<ViewExam> teachersExams) {
        this.teachersExams = teachersExams;
    }


    public ArrayList<Courses> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Courses> courses) {
        this.courses = courses;
    }
}