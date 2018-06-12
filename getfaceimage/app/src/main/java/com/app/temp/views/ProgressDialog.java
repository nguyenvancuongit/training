package com.app.temp.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.app.temp.R;

/**
 * Created by nguyen_van_cuong on 10/10/2017.
 */

public class ProgressDialog {

    private android.app.ProgressDialog mProgressDialog;
    private ImageView gif;

    private Context mContext;

    public ProgressDialog(Context pContext) {
        this.mContext = pContext;
    }

    public void show() {
        if (mProgressDialog == null) {
            mProgressDialog = new android.app.ProgressDialog(mContext);
            mProgressDialog.show();
            if (mProgressDialog.getWindow() != null) {
                mProgressDialog.getWindow().setDimAmount(0.0f);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            mProgressDialog.setContentView(R.layout.dialog_progress);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);

            gif = mProgressDialog.findViewById(R.id.gif);
            gif.setBackgroundResource(R.drawable.loading_animation);
            AnimationDrawable animation = (AnimationDrawable) gif.getBackground();
            animation.start();
        } else {
            hide();
            mProgressDialog.show();
        }
    }

    public void hide() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
