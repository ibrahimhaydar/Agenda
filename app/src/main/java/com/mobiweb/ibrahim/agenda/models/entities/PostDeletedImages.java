package com.mobiweb.ibrahim.agenda.models.entities;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDeletedImages {
    @SerializedName("id_image")
    @Expose
    private String id_image;

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }

    public PostDeletedImages(String id_image) {
        this.id_image = id_image;
    }
}
