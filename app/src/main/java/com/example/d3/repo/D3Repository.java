package com.example.d3.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.d3.model.APIResponse;
import com.example.d3.model.DeviceInfo;
import com.example.d3.network.D3ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class D3Repository {
    private D3ApiInterface mInterface;

    public D3Repository(D3ApiInterface apiInterface) {
        mInterface = apiInterface;
    }

    public void getPlaylistURL(DeviceInfo deviceInfo,MutableLiveData<String> liveData) {
        Call<APIResponse> call = mInterface.getURL();
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse> call,
                                   @NonNull Response<APIResponse> response) {
                if (response.isSuccessful()) {
                   //liveData.postValue(null);
                    liveData.postValue(response.body().getName());
                   // Log.d("D3In","Got string playlist not null"+response.body().getName());
                } else {
                    //Log.d("D3In","Got string playlist not null in unsuccess"+response.body());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                liveData.postValue(null);
                Log.d("D3In","Got string playlist not null in unsuccess"+t.getLocalizedMessage());
            }
        });
    }

    public void registerDevice(DeviceInfo deviceInfo, MutableLiveData<DeviceInfo> liveData) {
        Call<DeviceInfo> call = mInterface.createPost(deviceInfo);
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
            }
        });
    }
}