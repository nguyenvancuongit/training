package com.app.temp.features.home.player_mp3.detail;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.temp.R;
import com.app.temp.base.constant.Constant;
import com.app.temp.base.fragment.BaseFragment;
import com.app.temp.pojo.Mp3File;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class DetailMp3Fragment extends BaseFragment {

    // UI
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnPlayPause)
    ImageView btnPlayPause;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.btnGoBack)
    ImageView btnGoBack;
    @BindView(R.id.btnGoForward)
    ImageView btnGoForward;
    @BindView(R.id.btnSave)
    ImageView btnSave;
    @BindView(R.id.circularprogressbar)
    CircularProgressBar circularprogressbar;

    private Mp3File mMp3File;
    private Intent svc;
    private long mDuration;
    private long mCurrentPosition;
    private BroadcastReceiver receiverDuration;
    private BroadcastReceiver receiverPosition;
    BackgroundSoundService mService;

    public static DetailMp3Fragment newInstance() {
        return new DetailMp3Fragment();
    }

    public DetailMp3Fragment setMp3File(Mp3File file) {
        this.mMp3File = file;
        return this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_mp3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadData();

        // listener
        btnPlayPause.setOnClickListener(v -> {
            if (mService.isPlaying()) {
                btnPlayPause.setImageResource(R.drawable.btn_play);
            } else {
                btnPlayPause.setImageResource(R.drawable.btn_pause);
            }
            mService.pauseOrResume();
        });
        btnGoBack.setOnClickListener(v -> goBack10Seconds());
        btnGoForward.setOnClickListener(v -> goForward10Seconds());

        // broadcast
        receiverDuration = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mDuration = intent.getLongExtra(Constant.BLIND_MESSAGE, 0);
                tvTime.setText(getMinuteSecondFromMiliSeconds(mDuration));
            }
        };
        receiverPosition = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCurrentPosition = intent.getLongExtra(Constant.BLIND_MESSAGE, 0);
                if (mCurrentPosition > 0) {
                    // update circle progress
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> setProgressListen(mCurrentPosition));
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (svc != null) {
            Objects.requireNonNull(getActivity()).stopService(svc);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver((receiverDuration), new IntentFilter(Constant.BIND_RESULT_DURATION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiverPosition), new IntentFilter(Constant.BIND_RESULT_POSITION));

        // Bind to BackgroundSoundService
        Intent intent = new Intent(getActivity(), BackgroundSoundService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).unregisterReceiver(receiverDuration);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverPosition);
        getActivity().unbindService(mConnection);
        super.onStop();
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

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

    public void LoadData() {
        // play audio
        try {
            svc = new Intent(getActivity(), BackgroundSoundService.class);
            svc.setAction(Constant.ACTION_PLAY);
            svc.putExtra("url", mMp3File.getPath());
            Objects.requireNonNull(getActivity()).startService(svc);

            btnSave.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProgressListen(long position) {
        circularprogressbar.setProgress((int) ((((float) position / (float) mDuration)) * 100));
    }

    private void goBack10Seconds() {
        if ((mService.getCurrentPosition() - Constant.s_TIME_STEP) > 0) {
            mService.seekTo(mService.getCurrentPosition() - Constant.s_TIME_STEP);
            setProgressListen(mService.getCurrentPosition());
        }
    }

    private void goForward10Seconds() {
        if ((mService.getCurrentPosition() + Constant.s_TIME_STEP) < mDuration) {
            mService.seekTo(mService.getCurrentPosition() + Constant.s_TIME_STEP);
            setProgressListen(mService.getCurrentPosition());
        }
    }

    /**
     * get mm:ss from milliseconds
     *
     * @param millis input file's length
     * @return file's length formatted
     */
    public static String getMinuteSecondFromMiliSeconds(long millis) {
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
