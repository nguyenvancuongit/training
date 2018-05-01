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
import com.app.temp.utils.AppManagerUtil;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class AppInstalledFragment extends BaseFragment {

    @BindView(R.id.rc_list)
    RecyclerView rcList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        Observable<List<ApplicationInfo>> observable = Observable.create(emitter -> {
            try {
                List<ApplicationInfo> applicationInfos = AppManagerUtil.getApplicationInfos(getContext());
                emitter.onNext(applicationInfos);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        disposable = observable.subscribe(applicationInfos -> {
            rcList.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            rcList.setLayoutManager(mLayoutManager);

            mAdapter = new AppAdapter(getContext(), applicationInfos);
            rcList.setAdapter(mAdapter);
        });
    }
}
