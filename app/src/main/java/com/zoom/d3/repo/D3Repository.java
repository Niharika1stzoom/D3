package com.zoom.d3.repo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.zoom.d3.model.DeviceInfo;
import com.zoom.d3.model.Playlist;
import com.zoom.d3.network.D3ApiInterface;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.SharedPrefUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class D3Repository {
    private D3ApiInterface mInterface;
    public D3Repository(D3ApiInterface apiInterface) {
        mInterface = apiInterface;
    }


    public void getPlaylistURL(DeviceInfo deviceInfo, MutableLiveData<List<Playlist>> liveData) {
      //  Call<List<Playlist>> call = mInterface.getPlaylists(deviceInfo.getId(),
       //         Calendar.getInstance().getTime());
        Date date=new Date();
        String st= ISO8601Utils.format(Calendar.getInstance().getTime());
            Call<List<Playlist>> call = mInterface.getPlaylists(deviceInfo.getId(),st);
            call.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(@NonNull Call<List<Playlist>> call,
                                       @NonNull Response<List<Playlist>> response) {
                    if (response.isSuccessful()) {
                        liveData.postValue(response.body());
                    } else {
                        liveData.postValue(null);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG, "Failure" + t.getLocalizedMessage());
                }
            });

    }
    public void getDevice(DeviceInfo deviceInfo, MutableLiveData<DeviceInfo> liveData) {
        Call<DeviceInfo> call = mInterface.getDevice(deviceInfo.getDeviceId());
        call.enqueue(new Callback<DeviceInfo>() {
            @Override
            public void onResponse(@NonNull Call<DeviceInfo> call,
                                   @NonNull Response<DeviceInfo> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceInfo> call, @NonNull Throwable t) {
                liveData.postValue(null);
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
            }
        });
    }

    public void registerDevice(DeviceInfo deviceInfo, MutableLiveData<DeviceInfo> liveData) {
        Call<DeviceInfo> call = mInterface.registerDevice(deviceInfo);
        call.enqueue(new Callback<DeviceInfo>() {
            @Override
            public void onResponse(@NonNull Call<DeviceInfo> call,
                                   @NonNull Response<DeviceInfo> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceInfo> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG,"Failure"+t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void saveLastURL(Context context, String playlistUrl) {
        SharedPrefUtil.setLastUrl(context,playlistUrl);
    }

    public String getLastUrl(Context context) {
        return SharedPrefUtil.getLastUrl(context);
    }
}