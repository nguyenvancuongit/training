package com.app.temp.features.home.app_manager.apk_list;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.temp.R;
import com.app.temp.pojo.ApkFile;

import java.util.List;

public class ApkAdapter extends RecyclerView.Adapter<ApkAdapter.ViewHolder> {

    private List<ApkFile> mApkFiles;

    public ApkAdapter(List<ApkFile> mApkFiles) {
        this.mApkFiles = mApkFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rc_apk, parent, false);
        ApkAdapter.ViewHolder viewHolder = new ApkAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApkFile apkFile = mApkFiles.get(position);
        if (position % 2 == 0) {
            holder.container.setBackgroundColor(Color.GRAY);
        } else {
            holder.container.setBackgroundColor(Color.WHITE);
        }

        holder.tvName.setText(apkFile.getFile().getName());
        holder.tvPath.setText(apkFile.getFile().getAbsolutePath());
    }

    @Override
    public int getItemCount() {
        return mApkFiles == null ? 0 : mApkFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private TextView tvName;
        private TextView tvPath;

        public ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.container);
            tvName = v.findViewById(R.id.tv_name);
            tvPath = v.findViewById(R.id.tv_path);
        }
    }
}
