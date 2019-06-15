package com.mobiweb.ibrahim.agenda.models.entities;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostAttendance {
    @SerializedName("id_student")
    @Expose
    private String idStudent;
    @SerializedName("present")
    @Expose
    private String present;



    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public PostAttendance(String idStudent, String present) {
        this.idStudent = idStudent;
        this.present = present;
    }
}
