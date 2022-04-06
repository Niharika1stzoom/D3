package com.example.d3.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefUtil {
    private static final String KEY_REGISTER ="registered" ;
    private static final String LAST_URL ="last_url" ;

    synchronized public static void setRegistered(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_REGISTER,true);
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

    public static boolean isRegistered(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY_REGISTER, false);
    }
    //for testing
    public static void unRegistered(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_REGISTER);
        editor.remove(LAST_URL);
        //editor.putBoolean(KEY_REGISTER,false);
       // editor.putString(LAST_URL,null);
        editor.apply();

    }
}
