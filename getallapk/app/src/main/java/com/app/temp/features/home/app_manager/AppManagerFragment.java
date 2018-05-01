package com.app.temp.features.home.app_manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

import butterknife.BindView;

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
    }
}
