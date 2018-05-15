package com.app.temp.base.constant;

/**
 * Created by Windows 7 on 7/5/2016.
 */
public class Constant {
    public static final int s_TIME_STEP = 10000;
    public static final String ACTION_PLAY = "BackgroundSoundService.PLAY";
    static final public String BIND_RESULT_DURATION = "BackgroundSoundService.REQUEST_DURATION";
    static final public String BIND_RESULT_POSITION = "BackgroundSoundService.REQUEST_POSITION";
    static final public String BIND_RESULT_MUSIC_NAME = "BackgroundSoundService.REQUEST_MUSIC_NAME";
    static final public String BIND_RESULT_CHANGE_MODE = "BackgroundSoundService.REQUEST_CHANGE_MODE";
    static final public String BLIND_MESSAGE = "BackgroundSoundService.COPA_MSG";

    public static final int FLAG_SHUFFLE_ALL_MUSIC = 0;
    public static final int FLAG_SHUFFLE_ONE_MUSIC = 1;
    public static final int FLAG_SHUFFLE_RANDOM = 2;

    public static final String PUSH_CHANNEL_ID = "CHANNEL_MUSIC";

    static final public String BIND_STOP_MUSIC = "BackgroundSoundService.REQUEST_STOP_MUSIC";
    static final public String BIND_NEXT_MUSIC = "BackgroundSoundService.REQUEST_NEXT_MUSIC";
}
