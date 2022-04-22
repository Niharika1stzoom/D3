package com.zoom.d3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.zoom.d3.Web.WebActivity;
import com.zoom.d3.main.MainFragment;
import com.zoom.d3.main.MainViewModel;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import dagger.hilt.android.AndroidEntryPoint;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
@AndroidEntryPoint
public class TVActivity extends FragmentActivity {

    private MainViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvactivity);
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if(AppUtil.isNetworkAvailableAndConnected(getApplicationContext()))
            getDeviceDetails();
        else
        if (mViewModel.checkDeviceRegister()) {
            goToWebActivity();
        } else {
            setRegisterUI();
        }
    }
    void getDeviceDetails(){
        mViewModel.getDeviceDetails(AppUtil.getDeviceInfo(getApplicationContext())).
                observe(this, deviceInfo -> {
                    if(deviceInfo==null)
                    {
                        registerUser();
                    }
                    else {
                        mViewModel.setRegistered(deviceInfo);
                        goToWebActivity();
                    }
                });

    }
    void registerUser() {
        mViewModel.registerDevice(AppUtil.getDeviceInfo(getApplicationContext())).
                observe(this, deviceInfo -> {
                    if (deviceInfo == null) {
                        if (!AppUtil.isNetworkAvailableAndConnected(getApplicationContext()))
                            Toast.makeText(this,getString(R.string.network_err),Toast.LENGTH_SHORT);

                    } else {
                        mViewModel.setRegistered(deviceInfo);
                        Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                        goToWebActivity();
                    }
                });
    }
    private void setRegisterUI() {
        //set the UI for registering
        //On click of the submit button register him and show the and if any error show the err msg
        //If the device code is wrong show him err msg a snackbar
        registerUser();
    }
    private void goToWebActivity() {
        Intent intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }
}