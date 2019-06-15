package com.mobiweb.ibrahim.agenda.models.entities;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostClasses {
    @SerializedName("id_class")
    @Expose
    private String id_class;

    @SerializedName("id_section")
    @Expose
    private String id_section;

    public String getId_class() {
        return id_class;
    }

    public void setId_class(String id_class) {
        this.id_class = id_class;
    }

    public String getId_section() {
        return id_section;
    }

    public void setId_section(String id_section) {
        this.id_section = id_section;
    }

    public PostClasses(String id_class, String id_section) {
        this.id_class = id_class;
        this.id_section = id_section;
    }
}
