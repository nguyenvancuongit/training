package com.app.temp;

import android.app.Application;

import com.app.temp.di.component.AppComponent;
import com.app.temp.di.component.DaggerAppComponent;
import com.app.temp.di.module.ApiModule;
import com.app.temp.di.module.AppModule;
import com.app.temp.di.module.NetModule;

/**
 * Created by Windows 7 on 7/5/2016.
 */
public class MyApplication extends Application {
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BuildConfig.HOST_URL))
                .apiModule(new ApiModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
