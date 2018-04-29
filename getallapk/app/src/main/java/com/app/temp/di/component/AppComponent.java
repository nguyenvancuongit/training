package com.app.temp.di.component;

import com.app.temp.base.activity.BaseActivity;
import com.app.temp.di.module.ApiModule;
import com.app.temp.di.module.AppModule;
import com.app.temp.di.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by nguyen_van_cuong on 08/11/2017.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class, ApiModule.class})
public interface AppComponent {
    void inject(BaseActivity activity);
}
