package com.app.temp.features.home.player_mp3.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.temp.MyApplication;
import com.app.temp.R;
import com.app.temp.base.constant.Constant;
import com.app.temp.base.fragment.BaseFragment;
import com.app.temp.pojo.Mp3File;
import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class DetailMp3Fragment extends BaseFragment {

    // UI
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
    @BindView(R.id.circularprogressbar)
    CircularProgressBar circularprogressbar;
    @BindView(R.id.btn_next)
    ImageView btnNext;
    @BindView(R.id.btn_prev)
    ImageView btnPrev;
    @BindView(R.id.img_shuffle)
    ImageView imgShuffle;

    private long mDuration;
    private long mCurrentPosition;
    private BroadcastReceiver receiverDuration;
    private BroadcastReceiver receiverPosition;
    private BroadcastReceiver receiverMusicName;
    private BroadcastReceiver receiverChangeMode;

    // instance get from MyApplication
    private Intent svc;
    private BackgroundSoundService mService;

    private List<Mp3File> mp3FileList;
    private int mCurrentIndex;

    public static DetailMp3Fragment newInstance() {
        return new DetailMp3Fragment();
    }

    public DetailMp3Fragment setMp3File(List<Mp3File> file, int index) {
        this.mp3FileList = file;
        this.mCurrentIndex = index;
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

        // get intent player
        MyApplication myApplication = (MyApplication) Objects.requireNonNull(getActivity()).getApplication();
        svc = myApplication.getSvc();
        mService = myApplication.getService();

        // init list music for service
        mService.setMp3FileList(mp3FileList);
        mService.setCurrentIndex(mCurrentIndex);
        mService.setShuffleMode(Constant.FLAG_SHUFFLE_ALL_MUSIC);

        // play music
        loadData();

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
        btnNext.setOnClickListener(v -> changeMusic(true));
        btnPrev.setOnClickListener(v -> changeMusic(false));
        imgShuffle.setOnClickListener(v -> changeShuffleMode());

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
        receiverMusicName = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getContext(), "Playing music: " + intent.getStringExtra(Constant.BLIND_MESSAGE), Toast.LENGTH_SHORT).show();
                showNotification(intent.getStringExtra(Constant.BLIND_MESSAGE));
                tvTitle.setText(intent.getStringExtra(Constant.BLIND_MESSAGE));
            }
        };
        receiverChangeMode = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int mode = intent.getIntExtra(Constant.BLIND_MESSAGE, 0);
                if (mode == Constant.FLAG_SHUFFLE_ALL_MUSIC) {
                    Toast.makeText(getContext(), "Repeat all of the music.", Toast.LENGTH_SHORT).show();
                } else if (mode == Constant.FLAG_SHUFFLE_ONE_MUSIC) {
                    Toast.makeText(getContext(), "Repeat the current music.", Toast.LENGTH_SHORT).show();
                } else if (mode == Constant.FLAG_SHUFFLE_RANDOM) {
                    Toast.makeText(getContext(), "Showing the random music.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver((receiverDuration), new IntentFilter(Constant.BIND_RESULT_DURATION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiverPosition), new IntentFilter(Constant.BIND_RESULT_POSITION));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiverMusicName), new IntentFilter(Constant.BIND_RESULT_MUSIC_NAME));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiverChangeMode), new IntentFilter(Constant.BIND_RESULT_CHANGE_MODE));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).unregisterReceiver(receiverDuration);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverPosition);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverMusicName);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverChangeMode);
        super.onStop();
    }

    public void loadData() {
        // play audio
        try {
            if (!mService.isPlaying()) {
                svc.setAction(Constant.ACTION_PLAY);
                svc.putExtra("url", mp3FileList.get(mCurrentIndex).getPath());
                Objects.requireNonNull(getActivity()).startService(svc);
            } else {
                mService.changeUrl(mp3FileList.get(mCurrentIndex).getPath());
            }

            showNotification(mp3FileList.get(mCurrentIndex).getName());
            tvTitle.setText(mp3FileList.get(mCurrentIndex).getName());
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

    private void changeMusic(boolean isGoNext) {
        mService.changeMusic(isGoNext);
    }

    private void changeShuffleMode() {
        mService.changeShuffleMode();
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

    private void showNotification(String title) {
        mService.showNotification(title);
    }
}
