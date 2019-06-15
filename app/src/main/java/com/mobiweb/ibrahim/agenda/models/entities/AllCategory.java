package com.mobiweb.ibrahim.agenda.models.entities;

/**
 * Created by ibrahim on 4/14/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCategory {

    @SerializedName("id_category")
    @Expose
    private String idCategory;
    @SerializedName("title")
    @Expose
    private String title;

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}