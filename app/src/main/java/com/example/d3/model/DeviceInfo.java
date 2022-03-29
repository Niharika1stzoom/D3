package com.example.d3.model;

public class DeviceInfo {
    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    String deviceId;

    public DeviceInfo(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    String deviceName;
}
