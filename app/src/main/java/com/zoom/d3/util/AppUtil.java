package com.zoom.d3.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

  /*  public static Bitmap generateQR(String str,int size)  {
        Bitmap bitmap=null;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, size, size);

            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        }catch (WriterException exception)
        {}
        return bitmap;
    }*/
}
