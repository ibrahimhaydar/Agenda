package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostEvaluation {
    @SerializedName("id_student")
    @Expose
    private String idStudent;
    @SerializedName("id_evaluation")
    @Expose
    private String id_evaluation;
    @SerializedName("info")
    @Expose
    private String info;


    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getId_evaluation() {
        return id_evaluation;
    }

    public void setId_evaluation(String id_evaluation) {
        this.id_evaluation = id_evaluation;
    }

    public PostEvaluation(String idStudent, String info) {
        this.idStudent = idStudent;
        this.info = info;
    }
    public PostEvaluation(String idStudent, String info,String id_evaluation) {
        this.idStudent = idStudent;
        this.info = info;
        this.id_evaluation=id_evaluation;
    }
}
