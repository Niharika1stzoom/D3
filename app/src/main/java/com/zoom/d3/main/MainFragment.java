package com.zoom.d3.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.zoom.d3.R;
import com.zoom.d3.Web.WebActivity;
import com.zoom.d3.databinding.MainFragmentBinding;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private MainViewModel mViewModel;
    MainFragmentBinding mBinding;
    Boolean registerUI = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = MainFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        initViewModel();
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (mViewModel.checkDeviceRegister()) {
            displayloader();
            //again checking whether device details found
            if (AppUtil.isNetworkAvailableAndConnected(getActivity().getApplicationContext()))
                getDeviceDetails();
            else
            goToWebActivity();
        } else {
            setRegisterUI();
        }
    }

    private void displayloader() {
        mBinding.welcomeGroup.setVisibility(View.GONE);
        mBinding.viewLoader.rootView.setVisibility(View.VISIBLE);
    }
    private void hideloader() {
        mBinding.viewLoader.rootView.setVisibility(View.GONE);
        mBinding.welcomeGroup.setVisibility(View.VISIBLE);

    }

    void getDeviceDetails() {
        mViewModel.getDeviceDetails(AppUtil.getDeviceInfo(getContext())).
                observe(getViewLifecycleOwner(), deviceInfo -> {
                    if (deviceInfo == null) {
                        //if device is not found then show registerui
                        if (!registerUI) {
                            setRegisterUI();
                            return;
                        }
                        if (!AppUtil.isNetworkAvailableAndConnected(getContext())) {
                            Toast.makeText(getContext(), getString(R.string.network_err), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Polling again and again to see whether device registered
                        mViewModel.createTimerTask(AppUtil.getPollTime(Calendar.getInstance()
                                .getTime(), AppConstants.REGISTER_POLL_INTERVAL));
                    } else {
                        mViewModel.cancelTimer();
                        mViewModel.setRegistered(deviceInfo);
                        goToWebActivity();
                    }
                });
    }


    private void setRegisterUI() {
        registerUI = true;
        hideloader();
       /* Glide.with(getContext())
                .load(AppUtil.generateQR
                        (AppUtil.getDeviceInfo(getContext()).getDeviceId(), AppConstants.SIZE_MOBILE))
                .apply(new RequestOptions().override(AppConstants.SIZE_MOBILE, AppConstants.SIZE_MOBILE)
                        .transform(new RoundedCorners(20)))
                .into(mBinding.imageView);*/
        mBinding.imageView.setImageBitmap(AppUtil.generateQR
                (AppUtil.getDeviceInfo(getContext()).getDeviceId(), AppConstants.SIZE_MOBILE));
        getDeviceDetails();
    }

    private void goToWebActivity() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}