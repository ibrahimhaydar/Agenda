package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child {

    @SerializedName("id_child")
    @Expose
    private String idChild;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("id_class")
    @Expose
    private String idClass;
    @SerializedName("id_section")
    @Expose
    private String idSection;
    @SerializedName("class_name")
    @Expose
    private String className;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("birth_date")
    @Expose
    private String birth_date;





    public String getIdChild() {
        return idChild;
    }

    public void setIdChild(String idChild) {
        this.idChild = idChild;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}