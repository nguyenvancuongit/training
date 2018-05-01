package com.app.temp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class AppManagerUtil {

    public static List<ApplicationInfo> getApplicationInfos(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }
}
