package com.app.temp.features.home.app_manager.installed_list;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;

public class AppInstalledFragment extends BaseFragment {

    @BindView(R.id.rc_list)
    RecyclerView rcList;

    private List<ApplicationInfo> mApplicationInfos;

    public List<ApplicationInfo> getApplicationInfos() {
        return mApplicationInfos;
    }

    public void setApplicationInfos(List<ApplicationInfo> mApplicationInfos) {
        this.mApplicationInfos = mApplicationInfos;
        setUpAdapter();
    }

    public static AppInstalledFragment newInstance() {
        AppInstalledFragment fragment = new AppInstalledFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_installed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        if (rcList != null) {
            rcList.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rcList.setLayoutManager(mLayoutManager);

            AppAdapter mAdapter = new AppAdapter(getContext(), mApplicationInfos);
            rcList.setAdapter(mAdapter);
        }
    }
}
