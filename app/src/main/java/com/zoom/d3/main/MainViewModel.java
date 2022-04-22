package com.zoom.d3.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zoom.d3.model.DeviceInfo;
import com.zoom.d3.repo.D3Repository;
import com.zoom.d3.util.SharedPrefUtil;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {
    private Context mContext;
    private MutableLiveData<DeviceInfo> mDeviceLiveData;
    @Inject
    D3Repository repository;
    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mDeviceLiveData=new MutableLiveData<>();
    }

    //checks whether the device is registered in shared pref
    public boolean checkDeviceRegister() {
        return SharedPrefUtil.isRegistered(mContext);
    }

    public LiveData<DeviceInfo> registerDevice(DeviceInfo deviceInfo) {
        repository.registerDevice(deviceInfo,mDeviceLiveData);
        return mDeviceLiveData;
    }

    public void setRegistered(DeviceInfo device) {
        SharedPrefUtil.setRegistered(mContext,device);
    }
    public DeviceInfo getDevice() {
        return SharedPrefUtil.getDevice(mContext);
    }
    public LiveData<DeviceInfo> getDeviceDetails(DeviceInfo deviceInfo) {
        repository.getDevice(deviceInfo,mDeviceLiveData);
        return mDeviceLiveData;
    }
}