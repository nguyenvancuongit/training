package com.app.temp.features.home.player_mp3.detail;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.app.temp.R;
import com.app.temp.base.constant.Constant;
import com.app.temp.pojo.Mp3File;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Windows 7 on 11/6/2015.
 */
public class BackgroundSoundService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer mMediaPlayer = null;
    private LocalBroadcastManager broadcaster;
    private Timer timer;
    private final IBinder mBinder = new LocalBinder();

    private List<Mp3File> mp3FileList;
    private int mCurrentIndex;
    private int mShuffleMode = Constant.FLAG_SHUFFLE_ALL_MUSIC;

    public void setMp3FileList(List<Mp3File> mp3FileList) {
        this.mp3FileList = mp3FileList;
    }

    public void setCurrentIndex(int mCurrentIndex) {
        this.mCurrentIndex = mCurrentIndex;
    }

    public void setShuffleMode(int mShuffleMode) {
        this.mShuffleMode = mShuffleMode;
    }

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

    public void changeUrl(String url) {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMusic(boolean isGoNext) {
        switch (mShuffleMode) {
            case Constant.FLAG_SHUFFLE_ALL_MUSIC:
                if (isGoNext) {
                    if (mCurrentIndex < mp3FileList.size() - 1) {
                        mCurrentIndex += 1;
                    }
                } else {
                    if (mCurrentIndex > 0) {
                        mCurrentIndex -= 1;
                    }
                }
                break;
            case Constant.FLAG_SHUFFLE_ONE_MUSIC:
                break;
            case Constant.FLAG_SHUFFLE_RANDOM:
                final int min = 0;
                final int max = mp3FileList.size() - 1;
                mCurrentIndex = new Random().nextInt((max - min) + 1) + min;
                break;
        }

        sendCurrentMusicName(mp3FileList.get(mCurrentIndex).getName());
        showNotification(mp3FileList.get(mCurrentIndex).getName());
        changeUrl(mp3FileList.get(mCurrentIndex).getPath());
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

    public void sendCurrentMusicName(String name) {
        Intent intent = new Intent(Constant.BIND_RESULT_MUSIC_NAME);
        if (!TextUtils.isEmpty(name)) {
            intent.putExtra(Constant.BLIND_MESSAGE, name);
        }
        broadcaster.sendBroadcast(intent);
    }

    public void sendCurrentMode(int mode) {
        Intent intent = new Intent(Constant.BIND_RESULT_CHANGE_MODE);
        intent.putExtra(Constant.BLIND_MESSAGE, mode);
        broadcaster.sendBroadcast(intent);
    }

    public void changeShuffleMode() {
        if (mShuffleMode == Constant.FLAG_SHUFFLE_ALL_MUSIC) {
            mShuffleMode = Constant.FLAG_SHUFFLE_ONE_MUSIC;
        } else if (mShuffleMode == Constant.FLAG_SHUFFLE_ONE_MUSIC) {
            mShuffleMode = Constant.FLAG_SHUFFLE_RANDOM;
        } else if (mShuffleMode == Constant.FLAG_SHUFFLE_RANDOM) {
            mShuffleMode = Constant.FLAG_SHUFFLE_ALL_MUSIC;
        }
        sendCurrentMode(mShuffleMode);
    }

    public void showNotification(String title) {
        Intent intent = new Intent(getApplicationContext(), ActionBroadcastReceiver.class);
        intent.setAction(Constant.BIND_STOP_MUSIC);
        PendingIntent pauseIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Intent intent2 = new Intent(getApplicationContext(), ActionBroadcastReceiver.class);
        intent2.setAction(Constant.BIND_NEXT_MUSIC);
        PendingIntent nextIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Objects.requireNonNull(getApplicationContext()), Constant.PUSH_CHANNEL_ID)
                .setSmallIcon(R.drawable.img_mp3)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.btn_pause, "PAUSE", pauseIntent)
                .addAction(R.drawable.btn_next, "NEXT", nextIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(0, mBuilder.build());
    }
}
