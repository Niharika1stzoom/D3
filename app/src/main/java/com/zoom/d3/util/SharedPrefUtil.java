package com.zoom.d3.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.zoom.d3.model.DeviceInfo;

public class SharedPrefUtil {
    private static final String KEY_REGISTER ="registered" ;
    private static final String LAST_URL ="last_url" ;

    synchronized public static void setRegistered(Context context,DeviceInfo deviceInfo) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REGISTER,GsonUtils.getGsonObject(deviceInfo));
        editor.apply();
    }
    public static DeviceInfo getDevice(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String device=prefs.getString(KEY_REGISTER,"");
        if(TextUtils.isEmpty(device))
            return null;
        return GsonUtils.getModelObject(device);
    }
    public static boolean isRegistered(Context context) {
        if(SharedPrefUtil.getDevice(context)==null)
            return false;
        return true;
    }
    //for testing
    public static void unRegistered(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        //editor.remove(KEY_REGISTER);
        //editor.remove(LAST_URL);
        //editor.putBoolean(KEY_REGISTER,false);
        // editor.putString(LAST_URL,null);
        editor.apply();

    }
    synchronized public static void setLastUrl(Context context,String url) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LAST_URL,url);
        editor.apply();
    }
    public static String getLastUrl(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(LAST_URL, AppConstants.DEFAULT_URL);
    }
}
