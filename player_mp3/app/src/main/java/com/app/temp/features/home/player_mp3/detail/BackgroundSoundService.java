package com.app.temp.features.home.player_mp3.detail;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.app.temp.base.constant.Constant;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Windows 7 on 11/6/2015.
 */
public class BackgroundSoundService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mMediaPlayer = null;
    private LocalBroadcastManager broadcaster;
    Timer timer;
    private final IBinder mBinder = new LocalBinder();

    public BackgroundSoundService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && Objects.requireNonNull(intent.getAction()).equals(Constant.ACTION_PLAY)) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(intent.getStringExtra("url"));
                mMediaPlayer.setVolume(100, 100);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.prepareAsync(); // prepare async to not block main thread
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Service.START_STICKY;
    }

    /**
     * for media connection with activity
     */
    public int getCurrentPosition() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void pauseOrResume() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }
    }

    public void seekTo(int time) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(time);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public BackgroundSoundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BackgroundSoundService.this;
        }
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        mMediaPlayer.start();
        sendDuration(mMediaPlayer.getDuration());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        sendCurrentPosition(mMediaPlayer.getCurrentPosition());
                    } else {
                        timer.cancel();
                        timer.purge();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    public void sendDuration(long duration) {
        Intent intent = new Intent(Constant.BIND_RESULT_DURATION);
        if (duration > 0) {
            intent.putExtra(Constant.BLIND_MESSAGE, duration);
        }
        broadcaster.sendBroadcast(intent);
    }

    public void sendCurrentPosition(long position) {
        Intent intent = new Intent(Constant.BIND_RESULT_POSITION);
        if (position > 0) {
            intent.putExtra(Constant.BLIND_MESSAGE, position);
        }
        broadcaster.sendBroadcast(intent);
    }
}
