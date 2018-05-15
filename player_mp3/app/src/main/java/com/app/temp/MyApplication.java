package com.app.temp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.app.temp.di.component.AppComponent;
import com.app.temp.di.component.DaggerAppComponent;
import com.app.temp.di.module.ApiModule;
import com.app.temp.di.module.AppModule;
import com.app.temp.di.module.NetModule;
import com.app.temp.features.home.player_mp3.detail.BackgroundSoundService;

/**
 * Created by Windows 7 on 7/5/2016.
 */
public class MyApplication extends Application {
    private static AppComponent mAppComponent;

    private Intent svc;
    private BackgroundSoundService mService;
    private ServiceConnection mConnection;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BuildConfig.HOST_URL))
                .apiModule(new ApiModule())
                .build();

        svc = new Intent(getBaseContext(), BackgroundSoundService.class);

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to BackgroundSoundService, cast the IBinder and get BackgroundSoundService instance
                BackgroundSoundService.LocalBinder binder = (BackgroundSoundService.LocalBinder) service;
                mService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {

            }
        };
    }


    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public Intent getSvc() {
        return svc;
    }

    public BackgroundSoundService getService() {
        return mService;
    }

    public ServiceConnection getConnection() {
        return mConnection;
    }
}
