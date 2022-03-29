package com.example.d3.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.example.d3.model.DeviceInfo;

public class AppUtil {
    public static DeviceInfo getDeviceInfo(Context context)
    {
       return new DeviceInfo(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID), Build.DEVICE);
    }
}
