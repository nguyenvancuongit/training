package com.app.temp.features.home.app_manager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.temp.features.home.app_manager.apk_list.ListApkFragment;
import com.app.temp.features.home.app_manager.installed_list.AppInstalledFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUMBER_PAGE = 2;
    private static final int INDEX_FRAGMENT_APP_LIST = 0;
    private static final int INDEX_FRAGMENT_APK_LIST = 1;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == INDEX_FRAGMENT_APP_LIST) {
            return AppInstalledFragment.newInstance();
        } else {
            return ListApkFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_PAGE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == INDEX_FRAGMENT_APP_LIST) {
            return "Installed";
        } else {
            return "Apk";
        }
    }
}
