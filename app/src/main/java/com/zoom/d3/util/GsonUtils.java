package com.zoom.d3.util;

import com.zoom.d3.model.DeviceInfo;
import com.google.gson.Gson;

public class GsonUtils {
    public static String getGsonObject(DeviceInfo deviceInfo) {
        Gson gson = new Gson();
        return gson.toJson(deviceInfo);
    }

    public static DeviceInfo getModelObject(String deviceInfo) {
        Gson gson = new Gson();
        return gson.fromJson(deviceInfo, DeviceInfo.class);

    }
}

