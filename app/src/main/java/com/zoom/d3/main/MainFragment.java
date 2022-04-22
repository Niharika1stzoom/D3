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

import com.zoom.d3.R;
import com.zoom.d3.Web.WebActivity;
import com.zoom.d3.databinding.MainFragmentBinding;
import com.zoom.d3.util.AppConstants;
import com.zoom.d3.util.AppUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    private MainViewModel mViewModel;
    MainFragmentBinding mBinding;
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
        if(AppUtil.isNetworkAvailableAndConnected(getActivity().getApplicationContext()))
        getDeviceDetails();
        else
        if (mViewModel.checkDeviceRegister()) {
            goToWebActivity();
        } else {
            setRegisterUI();
        }
    }


    void getDeviceDetails(){
    mViewModel.getDeviceDetails(AppUtil.getDeviceInfo(getContext())).
            observe(getViewLifecycleOwner(), deviceInfo -> {
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
        mViewModel.registerDevice(AppUtil.getDeviceInfo(getContext())).
                observe(getViewLifecycleOwner(), deviceInfo -> {
                    if (deviceInfo == null) {
                        if (AppUtil.isNetworkAvailableAndConnected(getContext()))
                            AppUtil.showSnackbar(getView(), getString(R.string.network_err));
                        //Refresh option or retro option show a refresh button
                    } else {
                        mViewModel.setRegistered(deviceInfo);
                        Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                        goToWebActivity();
                    }
                });
    }
    private void setRegisterUI() {
        registerUser();
    }
    private void goToWebActivity() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        //intent.putExtra(EXTRA_URL, "");
        startActivity(intent);
    }
}