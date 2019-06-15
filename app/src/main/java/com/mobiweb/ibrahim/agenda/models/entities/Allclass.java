package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 11/8/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allclass {

    @SerializedName("id_class")
    @Expose
    private String idClass;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("id_section")
    @Expose
    private String idSection;


    @SerializedName("isSelected")
    @Expose
    private Boolean isSelected=false;

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIdSection() {
        return idSection;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}