package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 1/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("id_image")
    @Expose
    private String idImage;
    @SerializedName("image_name")
    @Expose
    private String imageName;

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Image(String idImage, String imageName) {
        this.idImage = idImage;
        this.imageName = imageName;
    }
}