package com.mobiweb.ibrahim.agenda.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ibrahim on 3/3/2018.
 */

public class Announcements {
    @SerializedName("announcement_id")
    @Expose
    private String announcement_id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("announcement_date")
    @Expose
    private String announcement_date;

    @SerializedName("withImage")
    @Expose
    private String withImage;

    @SerializedName("images")
    @Expose
    private ArrayList<Image> images = null;


    @SerializedName("is_filter")
    @Expose
    private String is_filter;

    @SerializedName("array_classes")
    @Expose
    private ArrayList<Allclass> classes = null;

    public String getWithImage() {
        return withImage;
    }

    public void setWithImage(String withImage) {
        this.withImage = withImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getAnnouncement_id() {
        return announcement_id;
    }

    public void setAnnouncement_id(String announcement_id) {
        this.announcement_id = announcement_id;
    }

    public String getAnnouncement_date() {
        return announcement_date;
    }

    public void setAnnouncement_date(String announcement_date) {
        this.announcement_date = announcement_date;
    }


    public String getIs_filter() {
        return is_filter;
    }

    public void setIs_filter(String is_filter) {
        this.is_filter = is_filter;
    }

    public ArrayList<Allclass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Allclass> classes) {
        this.classes = classes;
    }
}