package com.zoom.d3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zoom.d3.Web.TVWebActivity;
import com.zoom.d3.Web.WebActivity;
import com.zoom.d3.main.MainFragment;
import com.zoom.d3.main.MainViewModel;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
@AndroidEntryPoint
public class TVActivity extends FragmentActivity {

    private MainViewModel mViewModel;
    private ImageView imageview;
    Boolean registerUI = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvactivity);
        imageview = findViewById(R.id.imageView);
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (mViewModel.checkDeviceRegister()) {
            displayloader();
            if (AppUtil.isNetworkAvailableAndConnected(getApplicationContext()))
                getDeviceDetails();
            else
                goToWebActivity();
        } else {
            setRegisterUI();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.cancelTimer();
    }

    void getDeviceDetails() {

        mViewModel.getDeviceDetails(AppUtil.getDeviceInfo(getApplicationContext())).
                observe(this, deviceInfo -> {
                    if (deviceInfo == null) {
                        if (!registerUI) {
                            setRegisterUI();
                            return;
                        }
                        if (!AppUtil.isNetworkAvailableAndConnected(getApplicationContext())) {
                            Toast.makeText(this, getString(R.string.network_err),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mViewModel.createTimerTask(AppUtil.getPollTime(Calendar.getInstance()
                                .getTime(), AppConstants.REGISTER_POLL_INTERVAL));
                    } else {
                        mViewModel.cancelTimer();
                        mViewModel.setRegistered(deviceInfo);
                        goToWebActivity();
                    }
                });
    }

    private void displayloader() {
        findViewById(R.id.view_loader).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView).setVisibility(View.GONE);
        findViewById(R.id.scan_text).setVisibility(View.GONE);
        findViewById(R.id.welcome_msg).setVisibility(View.GONE);
        findViewById(R.id.welcome_subtitle).setVisibility(View.GONE);
    }

    private void hideloader() {
        findViewById(R.id.view_loader).setVisibility(View.GONE);
        findViewById(R.id.imageView).setVisibility(View.VISIBLE);
        findViewById(R.id.scan_text).setVisibility(View.VISIBLE);
        findViewById(R.id.welcome_msg).setVisibility(View.VISIBLE);
        findViewById(R.id.welcome_subtitle).setVisibility(View.VISIBLE);
    }

    private void setRegisterUI() {
        registerUI = true;
        hideloader();
        /*Glide.with(this)
                .load(AppUtil.generateQR
                        (AppUtil.getDeviceInfo(this).getDeviceId(), AppConstants.SIZE_MOBILE))
                .apply(new RequestOptions().override(AppConstants.SIZE_MOBILE, AppConstants.SIZE_MOBILE)
                        .transform(new RoundedCorners(20)))
                .into(imageview);*/
        imageview.setImageBitmap(AppUtil.generateQR
                (AppUtil.getDeviceInfo(this).getDeviceId(), AppConstants.SIZE_TV));
        getDeviceDetails();
    }

    private void goToWebActivity() {
        Intent intent = new Intent(this, TVWebActivity.class);
        startActivity(intent);
        finish();
    }
}