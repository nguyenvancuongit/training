package com.app.temp.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.temp.MyApplication;
import com.app.temp.network.API;
import com.app.temp.views.ConfirmDialog;
import com.app.temp.views.MessageDialog;
import com.app.temp.views.ProgressDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by Windows 7 on 7/11/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = "";

    MessageDialog mMessageDialog;
    ConfirmDialog mConfirmDialog;
    ProgressDialog mProgressDialog;
    Toast mToast;

    @Inject API api;
    Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTagLog();
        MyApplication.getAppComponent().inject(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * setup for log
     */
    public abstract void initTagLog();

    public void printLog(String log) {
        Log.d(TAG, log);
    }

    /**
     * setup for dialog
     */
    public void showMessage(String pMsg) {
        mMessageDialog.showWith(pMsg);
    }

    public void showMessageWithCallback(String pMsg, MessageDialog.MessageDialogCallback pCallback) {
        mMessageDialog.showWithCallback(pMsg, pCallback);
    }

    public void showConfirmWithCallback(String pMsg, ConfirmDialog.ConfirmDialogCallback pCallback) {
        mConfirmDialog.showWithCallback(pMsg, pCallback);
    }

    public void showConfirmWithCustomTextAndCallback(String pMsg, String pCancelText, String pConfirmText, ConfirmDialog.ConfirmDialogCallback pCallback) {
        mConfirmDialog.showWithCallback(pMsg, pCancelText, pConfirmText, pCallback);
    }

    public void showConfirmWithTitleAndCallback(String pTitle, String pMsg, String pCancelText, String pConfirmText, ConfirmDialog.ConfirmDialogCallback pCallback) {
        mConfirmDialog.showWithStyleCallback(pTitle, pMsg, pCancelText, pConfirmText, pCallback);
    }

    /**
     * setup for Toast
     */
    public void showToast(String pMsg) {
        mToast.setText(pMsg);
        mToast.show();
    }

    public void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
    }

    /**
     * setup for loading
     */
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.show();
        }
    }

    public void hideLoading() {
        mProgressDialog.hide();
    }

    /**
     * setup for Api
     */
    public API getApi() {
        return api;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unsubscribe to avoid memory leaks
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
