package com.app.temp.features.home.app_manager;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;
import com.app.temp.features.home.app_manager.apk_list.ListApkFragment;
import com.app.temp.features.home.app_manager.installed_list.AppInstalledFragment;
import com.app.temp.utils.AppManagerUtil;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class AppManagerFragment extends BaseFragment {

    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private android.support.v4.view.PagerAdapter mPagerAdapter;

    public static AppManagerFragment newInstance() {
        AppManagerFragment fragment = new AppManagerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mPager);

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
            // update for current fragment
            updateListAppInstalledForAllFragment(applicationInfos);

            // update for all fragment
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    updateListAppInstalledForAllFragment(applicationInfos);
                }

                @Override
                public void onPageSelected(int position) {
                    updateListAppInstalledForAllFragment(applicationInfos);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    updateListAppInstalledForAllFragment(applicationInfos);
                }
            });

        });
    }

    private void updateListAppInstalledForAllFragment(List<ApplicationInfo> applicationInfos) {
        if (mPager.getCurrentItem() == 0) {
            AppInstalledFragment frag1 = (AppInstalledFragment) mPager
                    .getAdapter()
                    .instantiateItem(mPager, mPager.getCurrentItem());
            if (frag1.getApplicationInfos() == null || frag1.getApplicationInfos().size() == 0) {
                frag1.setApplicationInfos(applicationInfos);
            }
        } else if (mPager.getCurrentItem() == 1) {
            ListApkFragment frag2 = (ListApkFragment) mPager
                    .getAdapter()
                    .instantiateItem(mPager, mPager.getCurrentItem());
            if (frag2.getApplicationInfos() == null || frag2.getApplicationInfos().size() == 0) {
                frag2.setApplicationInfos(applicationInfos);
            }
        }
    }
}
