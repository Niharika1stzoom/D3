package com.zoom.d3.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DeviceInfo {
    @SerializedName("token")
    String deviceId;
    @SerializedName("name")
    String deviceName;
    Date createdAt;
    Date updatedAt;
    String id;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getId() {
        return id;
    }

    public DeviceInfo(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }
    public String getDeviceName() {
        return deviceName;
    }
}
