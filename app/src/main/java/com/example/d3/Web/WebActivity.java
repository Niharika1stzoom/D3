package com.example.d3.Web;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.WebSettings;

import com.example.d3.R;
import com.example.d3.databinding.ActivityWebBinding;
import com.example.d3.main.MainFragment;
import com.example.d3.main.MainViewModel;
import com.example.d3.model.DeviceInfo;
import com.example.d3.util.AppConstants;
import com.example.d3.util.AppUtil;
import com.example.d3.util.SchedulerUtil;
import com.example.d3.util.SharedPrefUtil;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WebActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityWebBinding binding;
    private WebViewModel mViewModel;
    private WorkManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(getString(R.string.app_name));
        mVisible = true;
        mContentView = binding.webView;
        initViewModel();
        binding.webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                toggle();
                return true;
            }
        });
        //binding.dummyButton.setOnTouchListener(mDelayHideTouchListener);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(WebViewModel.class);
       observeURL();
    }
    void observeURL()
    {
        mViewModel.getPlayList(AppUtil.getDeviceInfo(getApplicationContext())).observe(this, playlistUrl -> {
           if(playlistUrl==null){
                Log.d("D3","playlist null");

            }
            else {
                //save the last url in shared pref and showWeb content
                Log.d("D3","Its not null");
                mViewModel.saveLastUrl(playlistUrl);
               //SchedulerUtil.scheduleService(this,"");
             //  scheduleService(2);
            }
            showWebContent();
        });
    }



    private void hideWebView() {
        binding.webView.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
       mViewModel.stopService();

        //SharedPrefUtil.unRegistered(getApplicationContext());
    }

    private void showWebContent() {
        //binding.webView.setVisibility(View.VISIBLE);
       // Intent intent = getIntent();
       //String url = intent.getStringExtra(MainFragment.EXTRA_URL);
        //url="https://samplelib.com/lib/preview/mp4/sample-5s.mp4";
      //url="http://20.204.52.253:3000/preview/6239b2c94417fbf1e6bfef96";
        //if(AppUtil.isNetworkAvailableAndConnected(this))
      //  url="http://www.google.com";

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
      webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebViewClientImpl webViewClient = new WebViewClientImpl(binding.progressBar);
        binding.webView.setWebViewClient(webViewClient);
        binding.webView.loadUrl(mViewModel.getLastUrl());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }
    public void scheduleService( Integer dateTime) {
      /*  Executors.newSingleThreadScheduledExecutor().schedule(
                mViewModel.loadPlayList(AppUtil.getDeviceInfo(getApplicationContext()))
        , 2, TimeUnit.SECONDS);*/
     /* mViewModel.startScheduler(dateTime);
        mViewModel.getSchedulerWorkInfo().observe(this,workInfos -> {
            if(workInfos!=null || !workInfos.isEmpty())
            {
                if(workInfos.get(0).getState().isFinished()) {
                    //mViewModel.stopService();
                    mViewModel.getPlayList(AppUtil.getDeviceInfo(this));

                    // mgr.cancelUniqueWork(AppConstants.WORKER_TAG);
                    // mgr.getWorkInfosForUniqueWorkLiveData(AppConstants.WORKER_TAG).removeObservers(this);
                    Log.d("Data", "Calling from wrk manager finish" + Calendar.getInstance().getTime());
                }
            }
        });
        /*  OneTimeWorkRequest schedulerWorkRequest =
                new OneTimeWorkRequest.Builder(SchedulerWorker.class).setInitialDelay(2,TimeUnit.MINUTES)
                        .build();
         mgr=WorkManager.getInstance(this);
              mgr .enqueueUniqueWork(AppConstants.WORKER_TAG,
                        ExistingWorkPolicy.REPLACE,
                        schedulerWorkRequest);
    mgr.getWorkInfosForUniqueWorkLiveData(AppConstants.WORKER_TAG).observe(this,workInfos -> {
    if(workInfos!=null || !workInfos.isEmpty())
    {
        if(workInfos.get(0).getState().isFinished()) {
            mViewModel.getPlayList(AppUtil.getDeviceInfo(this));
            // mgr.cancelUniqueWork(AppConstants.WORKER_TAG);
            // mgr.getWorkInfosForUniqueWorkLiveData(AppConstants.WORKER_TAG).removeObservers(this);
            Log.d("Data", "Calling from wrk manager finish" + Calendar.getInstance().getTime());
        }
    }
});
*/

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}