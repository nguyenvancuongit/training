package com.app.temp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Windows 7 on 1/4/2016.
 */
public class CommonUtil {
    /**
     * convert pixel and dp
     */
    public static int getDpFromPixel(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return ((int) (dp * scale + 0.5f));
    }

    public static float getPixedFromDp(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}
