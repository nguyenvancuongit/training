package com.app.temp.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.temp.base.activity.BaseActivity;
import com.app.temp.network.API;
import com.app.temp.views.ConfirmDialog;
import com.app.temp.views.MessageDialog;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by nguyen_van_cuong on 10/10/2017.
 */

public abstract class BaseFragment extends Fragment {

    public Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTagLog();
        setDisposable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    /**
     * setup for log
     */
    public void setTagLog() {
        ((BaseActivity) getActivity()).TAG = this.getClass().getName();
    }

    public void printLog(String log) {
        ((BaseActivity) getActivity()).printLog(log);
    }

    /**
     * setup for dialog
     */
    public void showMessage(String pMsg) {
        ((BaseActivity) getActivity()).showMessage(pMsg);
    }

    public void showMessageWithCallback(String pMsg, MessageDialog.MessageDialogCallback pCallback) {
        ((BaseActivity) getActivity()).showMessageWithCallback(pMsg, pCallback);
    }

    public void showConfirmWithCallback(String pMsg, ConfirmDialog.ConfirmDialogCallback pCallback) {
        ((BaseActivity) getActivity()).showConfirmWithCallback(pMsg, pCallback);
    }

    public void showConfirmWithCustomTextAndCallback(String pMsg, String pCancelText, String pConfirmText, ConfirmDialog.ConfirmDialogCallback pCallback) {
        ((BaseActivity) getActivity()).showConfirmWithCustomTextAndCallback(pMsg, pCancelText, pConfirmText, pCallback);
    }

    public void showConfirmWithTitleAndCallback(String pTitle, String pMsg, String pCancelText, String pConfirmText, ConfirmDialog.ConfirmDialogCallback pCallback) {
        ((BaseActivity) getActivity()).showConfirmWithTitleAndCallback(pTitle, pMsg, pCancelText, pConfirmText, pCallback);
    }

    /**
     * setup for Toast
     */
    public void showToast(String pMsg) {
        ((BaseActivity) getActivity()).showToast(pMsg);
    }

    public void hideToast() {
        ((BaseActivity) getActivity()).hideToast();
    }

    /**
     * setup for loading
     */
    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    public void hideLoading() {
        ((BaseActivity) getActivity()).hideLoading();
    }

    /**
     * setup for Api
     */
    public API getApi() {
        return ((BaseActivity) getActivity()).getApi();
    }

    public void setDisposable() {
        disposable = ((BaseActivity) getActivity()).getDisposable();
    }

    /**
     * hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        try {
            getActivity().getCurrentFocus().post(() -> {
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity()
                                    .getCurrentFocus().getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
