package com.app.temp.features.home.imagelist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends BaseAdapter {
    private Context mContext;

    ArrayList<File> photoFiles;

    public PhotoAdapter(Context c, ArrayList<File> files) {
        mContext = c;
        photoFiles = files;
    }

    public int getCount() {
        return photoFiles.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView photoView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            photoView = new ImageView(mContext);
            photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photoView.setPadding(8, 8, 8, 8);
        } else {
            photoView = (ImageView) convertView;
        }

        Glide.with(mContext)
                .load(photoFiles.get(position))
                .into(photoView);
        return photoView;
    }
}
