package com.app.temp.features.home.app_manager.apk_list;

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
import com.app.temp.features.home.app_manager.installed_list.AppAdapter;
import com.app.temp.pojo.ApkFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListApkFragment extends BaseFragment {

    @BindView(R.id.rc_list)
    RecyclerView rcList;

    private ApkAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        if (isExternalStorageReadable()) {
            List<ApkFile> apkFiles = apkReader(Environment.getExternalStorageDirectory());

            rcList.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            rcList.setLayoutManager(mLayoutManager);

            mAdapter = new ApkAdapter(getContext(), apkFiles);
            rcList.setAdapter(mAdapter);
        }
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
