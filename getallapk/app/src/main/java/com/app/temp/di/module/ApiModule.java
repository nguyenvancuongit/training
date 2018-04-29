package com.app.temp.di.module;

import com.app.temp.network.API;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by nguyen_van_cuong on 23/11/2017.
 */

@Module
public class ApiModule {

    @Provides
    @Singleton
    API provideRetrofit(Retrofit retrofit) {
        return retrofit.create(API.class);
    }
}
