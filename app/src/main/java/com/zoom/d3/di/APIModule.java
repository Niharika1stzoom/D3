package com.zoom.d3.di;

import com.zoom.d3.network.D3ApiInterface;
import com.zoom.d3.repo.D3Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class APIModule {
  //  String baseURL="http://20.204.52.253:3000/api/v0/";
    String baseURL="https://restaurants.happiestplaces.com/api/v0/d3/app/";


    @Singleton
    @Provides
    public D3ApiInterface getRestApiInterface(Retrofit retrofit) {
        return retrofit.create(D3ApiInterface.class);
    }
    @Singleton
    @Provides
    public Retrofit getRetroInstance() {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    @Singleton
    @Provides
    D3Repository provideRepository(D3ApiInterface apiInterface){
        return new D3Repository(apiInterface);
    }
}
