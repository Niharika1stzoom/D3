package com.example.d3.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.example.d3.model.DeviceInfo;
import com.google.android.material.snackbar.Snackbar;

public class AppUtil {
    public static DeviceInfo getDeviceInfo(Context context)
    {
       return new DeviceInfo(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID), Build.DEVICE);
    }
    public static void showSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }
}
