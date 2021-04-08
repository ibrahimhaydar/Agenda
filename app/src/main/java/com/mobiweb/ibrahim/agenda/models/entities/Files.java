package com.mobiweb.ibrahim.agenda.models.entities;


/**
 * Created by ibrahim on 1/28/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Files {

    @SerializedName("file_id")
    @Expose
    private String idFile;
    @SerializedName("file_name")
    @Expose
    private String fileName;

    @SerializedName("file_type")
    @Expose
    private String fileType;

    @SerializedName("file_thumb")
    @Expose
    private String fileThumb;

    @SerializedName("uri")
    @Expose
    private String uri;




    public String getIdFile() {
        return idFile;
    }

    public void setIdFile(String idFile) {
        this.idFile = idFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileThumb() {
        return fileThumb;
    }

    public void setFileThumb(String fileThumb) {
        this.fileThumb = fileThumb;
    }

    public Files(String idFile, String fileName, String fileType) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Files(String idFile, String fileName, String fileType, String uri) {
        this.idFile = idFile;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uri = uri;
    }
}