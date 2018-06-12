package com.app.temp.features.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

public class RegisterFragment extends BaseFragment {

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        printLog("newInstance");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_3st, container, false);
    }
}
