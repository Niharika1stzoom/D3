package com.zoom.d3.network;

import com.zoom.d3.model.DeviceInfo;
import com.zoom.d3.model.Playlist;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface D3ApiInterface {

    @POST("device")
    Call<DeviceInfo> registerDevice(@Body DeviceInfo deviceInfo);

    @GET("device/{deviceId}/")
    Call<List<Playlist>> getPlaylists(@Path("deviceId") String deviceId,
                                      @Query("endDate") String endDate);
    @GET("device/")
    Call<DeviceInfo> getDevice(@Query("token") String token);




}