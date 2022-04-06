package com.example.d3.Web;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.d3.repo.D3Repository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class SchedulerWorker extends Worker {
    @Inject
    D3Repository repository;
    public SchedulerWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        //Take parameter from construct
        //repository.getPlaylistURL();
        Log.d("Data","From the worker");
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}
