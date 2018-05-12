package com.app.temp.features.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

public class Home2Fragment extends BaseFragment {

    public static Home2Fragment newInstance() {
        Home2Fragment fragment = new Home2Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        printLog("newInstance");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_2st, container, false);
    }
}
