package com.example.d3.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.d3.R;
import com.example.d3.Web.WebActivity;
import com.example.d3.databinding.MainFragmentBinding;
import com.example.d3.util.AppUtil;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    MainFragmentBinding mBinding;
    public static final String EXTRA_URL = "com.example.d3.URL";
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding= MainFragmentBinding.inflate(inflater,container,false);
        View view=mBinding.getRoot();
        initViewModel();
        return view;
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
       if(mViewModel.checkDeviceRegister())
        goToWebActivity();
       else {
           registerUser();
           goToWebActivity();
       }

    }

    private void setRefreshButton() {
        mBinding.welcomeGroup.setVisibility(View.VISIBLE);
        mBinding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWebActivity();

            }
        });
    }
    void registerUser()
    {
        mViewModel.setRegistered();
        Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        //AppUtil.showSnackbar(getView(),getString(R.string.register_success));

               /* mViewModel.registerDevice(AppUtil.getDeviceInfo(getContext())).
                        observe(getViewLifecycleOwner(), deviceInfo -> {
                    if(deviceInfo==null)
                    {}
                    else {
                        mViewModel.setRegistered();
                        goToWebActivity();
                    }
                });*/
    }
    //get him registered
    //send to web activity
    //if not found show him refresh button

  /*  private void getPlayListURL()
    {
    mViewModel.getPlayList(AppUtil.getDeviceInfo(getContext())).observe(getViewLifecycleOwner(), playlistUrl -> {
       if(playlistUrl==null){
           Log.d("D3","playlist null");
           setRefreshButton();
       }
       else {
           Log.d("D3","playlist not null");
           showWebActivity(playlistUrl);
       }
    });
    }*/

    private void goToWebActivity() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(EXTRA_URL, "");
        startActivity(intent);
    }

}