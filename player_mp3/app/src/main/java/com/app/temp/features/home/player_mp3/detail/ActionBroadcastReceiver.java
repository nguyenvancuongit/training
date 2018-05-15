package com.app.temp.features.home.player_mp3.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.temp.MyApplication;
import com.app.temp.base.constant.Constant;

import java.util.Objects;

public class ActionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();
        BackgroundSoundService service = myApplication.getService();
        if (Objects.requireNonNull(intent.getAction()).equals(Constant.BIND_STOP_MUSIC)) {
            service.pauseOrResume();
        }
        if (Objects.requireNonNull(intent.getAction()).equals(Constant.BIND_NEXT_MUSIC)) {
            service.changeMusic(true);
        }
    }
}
