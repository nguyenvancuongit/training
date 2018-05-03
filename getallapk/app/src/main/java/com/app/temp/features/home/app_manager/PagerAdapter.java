package com.app.temp.features.home.app_manager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.temp.base.constant.Constant;
import com.app.temp.features.home.app_manager.apk_list.ListApkFragment;
import com.app.temp.features.home.app_manager.installed_list.AppInstalledFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == Constant.INDEX_FRAGMENT_APP_LIST) {
            return AppInstalledFragment.newInstance();
        } else if (position == Constant.INDEX_FRAGMENT_APK_INSTALLED_LIST) {
            return ListApkFragment.newInstance().setShowingInstalled(true);
        } else if (position == Constant.INDEX_FRAGMENT_APK_NOT_INSTALLED_LIST) {
            return ListApkFragment.newInstance().setShowingInstalled(false);
        }
        return null;
    }

    @Override
    public int getCount() {
        return Constant.NUMBER_PAGE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == Constant.INDEX_FRAGMENT_APP_LIST) {
            return "App Installed";
        } else if (position == Constant.INDEX_FRAGMENT_APK_INSTALLED_LIST) {
            return "Apk Installed";
        } else if (position == Constant.INDEX_FRAGMENT_APK_NOT_INSTALLED_LIST) {
            return "Not installed";
        }
        return "";
    }
}
