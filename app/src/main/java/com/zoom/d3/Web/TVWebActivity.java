package com.zoom.d3.Web;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zoom.d3.R;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TVWebActivity extends FragmentActivity {
    private WebViewModel mViewModel;
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvweb);
        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progress_bar);
        initViewModel();
    }
    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(WebViewModel.class);
        observeURL();
    }
    void observeURL()
    {
        //if playlist found set time for another poll if not will keep playing the last playlist found
        mViewModel.getPlayList().observe(this, playlists -> {
            if(playlists==null ||  playlists.size()==0){
                mViewModel.createTimerTask(AppUtil.getPollTime(Calendar.getInstance()
                        .getTime(), AppConstants.DEFAULT_POLL_INTERVAL));
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
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        WebViewClientImpl webViewClient = new WebViewClientImpl(progressBar);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(mViewModel.getLastUrl());
    }

}