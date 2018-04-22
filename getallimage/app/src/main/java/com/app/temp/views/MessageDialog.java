package com.app.temp.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.app.temp.R;

/**
 * Created by nguyen_van_cuong on 10/10/2017.
 */

public class MessageDialog {

    public interface MessageDialogCallback {
        void onDismiss();
    }

    private MessageDialogCallback mCallback;

    private Context mContext;
    private TextView tvContent;
    private TextView btnOk;
    private Dialog mDialog;

    public MessageDialog(Context pContext) {
        this.mContext = pContext;
        mDialog = new Dialog(pContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_message);
        mDialog.setCancelable(false);

        tvContent = mDialog.findViewById(R.id.tv_content);
        btnOk = mDialog.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(view -> {
            if (mCallback != null) {
                mCallback.onDismiss();
            }
            mDialog.dismiss();
        });
    }

    public void showWith(String pMsg) {
        mCallback = null;
        tvContent.setText(pMsg);
        mDialog.show();
    }

    public void showWithCallback(String pMsg, MessageDialogCallback pCallback) {
        mCallback = pCallback;
        tvContent.setText(pMsg);
        mDialog.show();
    }
}
