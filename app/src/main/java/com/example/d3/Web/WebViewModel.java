package com.example.d3.Web;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.d3.model.DeviceInfo;
import com.example.d3.repo.D3Repository;
import com.example.d3.util.SharedPrefUtil;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WebViewModel extends ViewModel {
    private Context mContext;
    private MutableLiveData<String> mMutablePlaylist;
    @Inject
    D3Repository repository;

    @Inject
    public WebViewModel() {
        mMutablePlaylist=new MutableLiveData<>();
    }

    public LiveData<String> getPlayList(DeviceInfo deviceInfo) {
        //get playlist
        repository.getPlaylistURL(deviceInfo,mMutablePlaylist);
        return mMutablePlaylist;
    }

}