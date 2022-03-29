package com.example.d3.network;

import com.example.d3.model.APIResponse;
import com.example.d3.model.DeviceInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface D3ApiInterface {
       //pass the device id to retrieve the url
        @GET("e3fbe384-5ac4-4290-9127-611d5bde4454")
        Call<APIResponse> getURL();


        //register the device
        @POST("users")
        Call<DeviceInfo> createPost(@Body DeviceInfo deviceInfo);
}
