package com.app.temp.features.home.app_manager.apk_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.temp.R;
import com.app.temp.pojo.ApkFile;

import java.util.List;

public class ApkAdapter extends RecyclerView.Adapter<ApkAdapter.ViewHolder> {

    private Context mContext;
    private List<ApkFile> mApkFiles;

    public ApkAdapter(Context mContext, List<ApkFile> mApkFiles) {
        this.mContext = mContext;
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
        holder.tvName.setText(apkFile.getFile().getName());
        holder.tvInstalled.setText(apkFile.isInstalled() ? "Installed" : "Not installed");
        holder.tvPath.setText(apkFile.getFile().getAbsolutePath());
    }

    @Override
    public int getItemCount() {
        return mApkFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        
        private TextView tvName;
        private TextView tvInstalled;
        private TextView tvPath;

        public ViewHolder(View v) {
            super(v);
            
            tvName = v.findViewById(R.id.tv_name);
            tvInstalled = v.findViewById(R.id.tv_installed);
            tvPath = v.findViewById(R.id.tv_path);
        }
    }
}
