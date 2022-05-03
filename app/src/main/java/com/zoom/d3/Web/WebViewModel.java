package com.zoom.d3.Web;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zoom.d3.model.DeviceInfo;
import com.zoom.d3.model.Playlist;
import com.zoom.d3.repo.D3Repository;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.SharedPrefUtil;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WebViewModel extends AndroidViewModel {
    private Context mContext;
    private MutableLiveData<List<Playlist>> mMutablePlaylist;
    Timer mTimer;
    @Inject
    D3Repository repository;
    public void cancelTimer() {
        mTimer.cancel();
    }


    void createTimerTask(Date date) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                repository.getPlaylistURL(SharedPrefUtil.getDevice(mContext), mMutablePlaylist);
            }
        }, date);
    }

    @Inject
    public WebViewModel(Application application) {
        super(application);
        mMutablePlaylist = new MutableLiveData<>();
        mContext = application.getApplicationContext();
        mTimer = new Timer();
    }

    public LiveData<List<Playlist>> getPlayList() {
        //get playlist
        repository.getPlaylistURL(SharedPrefUtil.getDevice(mContext), mMutablePlaylist);
        return mMutablePlaylist;
    }

    public void saveLastUrl(String playlistUrl) {
        repository.saveLastURL(mContext, playlistUrl);
    }

    public String getLastUrl() {
        return repository.getLastUrl(mContext);
    }

    public void loadPlayList(DeviceInfo deviceInfo) {
        repository.getPlaylistURL(deviceInfo, mMutablePlaylist);
    }
}