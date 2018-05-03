package com.app.temp.features.home.app_manager.apk_list;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;
import com.app.temp.pojo.ApkFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListApkFragment extends BaseFragment {

    @BindView(R.id.rc_list)
    RecyclerView rcList;

    private boolean isShowingInstalled = true;
    private PackageManager mPackageManager;
    private List<ApplicationInfo> mApplicationInfos;
    private List<ApkFile> mApkFiles;

    public List<ApplicationInfo> getApplicationInfos() {
        return mApplicationInfos;
    }

    public void setApplicationInfos(List<ApplicationInfo> mApplicationInfos) {
        this.mApplicationInfos = mApplicationInfos;
        setUpAdapter(mApkFiles);
    }

    public boolean isShowingInstalled() {
        return isShowingInstalled;
    }

    public ListApkFragment setShowingInstalled(boolean showingInstalled) {
        this.isShowingInstalled = showingInstalled;
        return this;
    }

    public static ListApkFragment newInstance() {
        ListApkFragment fragment = new ListApkFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_apk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPackageManager = getContext().getPackageManager();
        mApkFiles = new ArrayList<>();

        if (isExternalStorageReadable()) {
            mApkFiles = apkReader(Environment.getExternalStorageDirectory());
            setUpAdapter(mApkFiles);
        }
    }

    private void setUpAdapter(List<ApkFile> apkFiles) {
        if (rcList != null) {
            // check if we need to show installed or not installed
            List<ApkFile> installedArray = new ArrayList<>();
            List<ApkFile> notInstalledArray = new ArrayList<>();
            for (ApkFile apkFile : apkFiles) {
                PackageInfo packageInfo = mPackageManager.getPackageArchiveInfo(apkFile.getFile().getPath(), 0);
                // check exist on installed list
                if (isExistOnInstalledList(packageInfo)) {
                    installedArray.add(apkFile);
                } else {
                    notInstalledArray.add(apkFile);
                }
            }

            rcList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rcList.setLayoutManager(mLayoutManager);

            ApkAdapter mAdapter;
            if (isShowingInstalled()) {
                mAdapter = new ApkAdapter(installedArray);
            } else {
                mAdapter = new ApkAdapter(notInstalledArray);
            }
            rcList.setAdapter(mAdapter);
        }
    }

    private boolean isExistOnInstalledList(PackageInfo packageInfo) {
        if (getApplicationInfos() != null) {
            for (ApplicationInfo applicationInfo : getApplicationInfos()) {
                if (applicationInfo.packageName.equals(packageInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    List<ApkFile> apkReader(File root) {
        List<ApkFile> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                a.addAll(apkReader(file));
            } else {
                if (file.getName().toLowerCase().endsWith(".apk")) {
                    a.add(new ApkFile(file));
                }
            }
        }
        return a;
    }
}
