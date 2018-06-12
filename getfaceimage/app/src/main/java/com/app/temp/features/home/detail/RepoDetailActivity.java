package com.app.temp.features.home.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.temp.R;
import com.app.temp.base.activity.BaseActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;

public class RepoDetailActivity extends BaseActivity {

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    String mAvatarUrl = "";

    public static void start(Context context, String urlImage) {
        Intent starter = new Intent(context, RepoDetailActivity.class);
        starter.putExtra("url_image", urlImage);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_detail);

        mAvatarUrl = getIntent().getStringExtra("url_image");
        Glide.with(this).load(mAvatarUrl).into(imgAvatar);
    }

    @Override
    public void initTagLog() {
        TAG = "RepoDetailActivity";
    }
}
