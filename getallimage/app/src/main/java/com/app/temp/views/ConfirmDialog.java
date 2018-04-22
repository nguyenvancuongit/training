package com.app.temp.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.app.temp.R;

/**
 * Created by nguyen_van_cuong on 10/10/2017.
 */

public class ConfirmDialog {

    public interface ConfirmDialogCallback {
        void onCancel();

        void onConfirm();
    }

    private ConfirmDialogCallback mCallback;

    private Context mContext;
    private TextView tvContent;
    private TextView btnOk;
    private TextView btnCancel;
    private TextView tvTitle;
    private Dialog mDialog;

    public ConfirmDialog(Context pContext) {
        init(pContext);
    }

    private void init(Context pContext) {
        this.mContext = pContext;
        mDialog = new Dialog(pContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_confirmation);
        mDialog.setCancelable(false);

        tvContent = mDialog.findViewById(R.id.tv_content);
        tvTitle = mDialog.findViewById(R.id.tv_title);
        btnOk = mDialog.findViewById(R.id.btn_ok);
        btnCancel = mDialog.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.onConfirm();
            }
            mDialog.dismiss();
        });

        btnCancel.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.onCancel();
            }
            mDialog.dismiss();
        });
    }

    public void showWithCallback(String pMsg, ConfirmDialogCallback pCallback) {
        resetTextButtonToDefault();
        mCallback = pCallback;
        tvContent.setText(pMsg);
        mDialog.show();
    }

    public void showWithCallback(String pMsg, @Nullable String pCancelText, @Nullable String pConfirmText, ConfirmDialogCallback pCallback) {
        tvTitle.setVisibility(View.GONE);
        if (pCancelText == null || pCancelText.isEmpty()) {
            btnCancel.setText(mContext.getResources().getString(R.string.cancel));
        } else {
            btnCancel.setText(pCancelText);
            btnCancel.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

        if (pConfirmText == null || pConfirmText.isEmpty()) {
            btnOk.setText(mContext.getResources().getString(R.string.ok));
        } else {
            btnOk.setText(pConfirmText);
        }
        mCallback = pCallback;
        tvContent.setText(pMsg);
        mDialog.show();
    }

    public void showWithStyleCallback(String pTitle,
                                      String pMsg,
                                      @Nullable String pCancelText,
                                      @Nullable String pConfirmText,
                                      ConfirmDialogCallback pCallback) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(pTitle);

        if (pCancelText == null || pCancelText.isEmpty()) {
            btnCancel.setText(mContext.getResources().getString(R.string.cancel));
        } else {
            btnCancel.setText(pCancelText);
            btnCancel.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

        if (pConfirmText == null || pConfirmText.isEmpty()) {
            btnOk.setText(mContext.getResources().getString(R.string.ok));
        } else {
            btnOk.setText(pConfirmText);
        }
        mCallback = pCallback;
        tvContent.setText(pMsg);
        mDialog.show();
    }

    private void resetTextButtonToDefault() {
        btnCancel.setText(mContext.getResources().getString(R.string.cancel));
        btnOk.setText(mContext.getResources().getString(R.string.ok));
    }
}
