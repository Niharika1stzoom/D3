package com.zoom.d3.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Playlist {
    String name,description,type;
    @SerializedName("previewURL")
    String previewUrl;
    List<String> files;
    Date startDate,endDate,updatedAt;
    String id;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public List<String> getFiles() {
        return files;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getId() {
        return id;
    }
}
