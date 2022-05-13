package com.zoom.d3.Web;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.WebSettings;

import com.zoom.d3.R;
import com.zoom.d3.databinding.ActivityWebBinding;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WebActivity extends AppCompatActivity {

    // Whether or not the system UI should be auto-hidden after
    private static final boolean AUTO_HIDE = true;
    //the number of milliseconds to wait after user interaction before hiding the system UI.
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    //Some older devices needs a small delay between UI widget updates and a change of the status and navigation bar.
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private ActivityWebBinding binding;
    private WebViewModel mViewModel;
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

    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        observeURL();
    }
    void displayLoader()
    {
        binding.progressBar.setVisibility(View.VISIBLE);
    }
    void hideLoader()
    {
        binding.progressBar.setVisibility(View.GONE);
    }
    void observeURL()
    {
        displayLoader();
        //if playlist found set time for another poll if not will keep playing the last playlist found
        mViewModel.getPlayList().observe(this, playlists -> {
        hideLoader();
            if(playlists==null ||  playlists.size()==0){
                mViewModel.createTimerTask(AppUtil.getPollTime(Calendar.getInstance()
                        .getTime(),AppConstants.DEFAULT_POLL_INTERVAL));
            }
            else {
                //save the last url in shared pref and showWeb content
                mViewModel.saveLastUrl(playlists.get(0).getPreviewUrl());
                mViewModel.createTimerTask(playlists.get(0).getEndDate());
            }
            showWebContent();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showWebContent() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        WebViewClientImpl webViewClient = new WebViewClientImpl(binding.progressBar);
        binding.webView.setWebViewClient(webViewClient);
        binding.webView.loadUrl(mViewModel.getLastUrl());
    }

    private void hideWebView() {
        binding.webView.setVisibility(View.GONE);
    }
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
    private final Runnable mShowPart2Runnable = () -> {
        // Delayed display of UI elements
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        //mControlsView.setVisibility(View.VISIBLE);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
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