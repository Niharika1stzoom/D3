package com.zoom.d3.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.zoom.d3.model.DeviceInfo;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

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

    public static Date getPollTime(Date endDate,int mins) {
        Date date=endDate;
        Calendar c1=Calendar.getInstance();
        c1.setTime(date);
        c1.add(Calendar.MINUTE,mins);
        Date curr=c1.getTime();
        return curr;
    }
    public static Bitmap generateQR(String content, int size) {
        Bitmap bitmap = null;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(content,
                    BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            Log.e("generateQR()", e.getMessage());
        }
        return bitmap;
    }
}
