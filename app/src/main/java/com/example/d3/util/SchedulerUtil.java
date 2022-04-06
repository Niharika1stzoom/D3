package com.example.d3.util;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.d3.Web.SchedulerWorker;

public class SchedulerUtil {
    public static void scheduleService(Context context, String dataTime) {
        OneTimeWorkRequest schedulerWorkRequest =
                new OneTimeWorkRequest.Builder(SchedulerWorker.class)
                        .build();
        WorkManager
                .getInstance(context)
                .enqueueUniqueWork(AppConstants.WORKER_TAG,
                        ExistingWorkPolicy.REPLACE,
                        schedulerWorkRequest);


    }
}
