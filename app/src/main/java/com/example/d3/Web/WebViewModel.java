package com.example.d3.Web;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.example.d3.model.DeviceInfo;
import com.example.d3.repo.D3Repository;
import com.example.d3.util.AppConstants;
import com.example.d3.util.SharedPrefUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WebViewModel extends AndroidViewModel {
    WorkManager mWorkManager;
    private Context mContext;
    private LiveData<List<WorkInfo>> mSchedulerWorkInfo;

    private MutableLiveData<String> mMutablePlaylist;
    @Inject
    D3Repository repository;

    @Inject
    public WebViewModel(Application application) {
        super(application);
        mMutablePlaylist=new MutableLiveData<>();
        mContext=application.getApplicationContext();
        mWorkManager = WorkManager.getInstance(application);
        mSchedulerWorkInfo = mWorkManager.getWorkInfosForUniqueWorkLiveData(AppConstants.WORKER_TAG);

    }

    public LiveData<String> getPlayList(DeviceInfo deviceInfo) {
        //get playlist
        repository.getPlaylistURL(deviceInfo,mMutablePlaylist);
        return mMutablePlaylist;
    }

    public void saveLastUrl(String playlistUrl) {
        repository.saveLastURL(mContext,playlistUrl);
    }
    public void startScheduler(Integer dateTime)
    {
        //if date time is passed the consider default time
        OneTimeWorkRequest schedulerWorkRequest =
                new OneTimeWorkRequest.Builder(SchedulerWorker.class).setInitialDelay(dateTime, TimeUnit.MINUTES)
                        .build();
        mWorkManager.enqueueUniqueWork(AppConstants.WORKER_TAG,
                ExistingWorkPolicy.REPLACE,
                schedulerWorkRequest);

    }
    LiveData<List<WorkInfo>> getSchedulerWorkInfo() { return mSchedulerWorkInfo; }

    public String getLastUrl() {
        return repository.getLastUrl(mContext);
    }

    public void stopService() {
        //if service is running stop it
        mWorkManager.cancelUniqueWork(AppConstants.WORKER_TAG);
    }

    public void loadPlayList(DeviceInfo deviceInfo) {
        repository.getPlaylistURL(deviceInfo,mMutablePlaylist);
    }
}