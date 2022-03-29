package com.example.d3.main;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.d3.model.DeviceInfo;
import com.example.d3.repo.D3Repository;
import com.example.d3.util.SharedPrefUtil;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {
    private Context mContext;
    private MutableLiveData<DeviceInfo> mMutableLiveData;
    private MutableLiveData<String> mMutablePlaylist;
    @Inject
    D3Repository repository;
    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getApplicationContext();
        mMutableLiveData=new MutableLiveData<>();
        mMutablePlaylist=new MutableLiveData<>();
    }

    //checks whether the device is registered in shared pref
    public boolean checkDeviceRegister() {
        //check in Shared Ref
        return SharedPrefUtil.isRegistered(mContext);

    }



    public LiveData<DeviceInfo> registerDevice(DeviceInfo deviceInfo) {
        //make api call
        //set in shared pref
        repository.registerDevice(deviceInfo,mMutableLiveData);
        return mMutableLiveData;
    }

    public LiveData<String> getPlayList(DeviceInfo deviceInfo) {
        //get playlist
        repository.getPlaylistURL(deviceInfo,mMutablePlaylist);
        return mMutablePlaylist;
    }

    public void setRegistered() {
        SharedPrefUtil.setRegistered(mContext);
    }
}