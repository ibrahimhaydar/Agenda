package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 5/12/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("id_exam")
    @Expose
    private String idExam;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("more")
    @Expose
    private String more;
    @SerializedName("id_class")
    @Expose
    private String idClass;
    @SerializedName("id_section")
    @Expose
    private String idSection;

    public String getIdExam() {
        return idExam;
    }

    public void setIdExam(String idExam) {
        this.idExam = idExam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

}
