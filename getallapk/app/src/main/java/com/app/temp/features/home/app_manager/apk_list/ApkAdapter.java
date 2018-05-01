package com.app.temp.features.home.app_manager.apk_list;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    private Context mContext;
    private List<ApkFile> mApkFiles;
    private PackageManager mPackageManager;
    private List<ApplicationInfo> mApplicationInfos;

    public List<ApplicationInfo> getApplicationInfos() {
        return mApplicationInfos;
    }

    public void setApplicationInfos(List<ApplicationInfo> mApplicationInfos) {
        this.mApplicationInfos = mApplicationInfos;
    }

    public ApkAdapter(Context mContext, List<ApkFile> mApkFiles) {
        this.mContext = mContext;
        this.mApkFiles = mApkFiles;
        this.mPackageManager = mContext.getPackageManager();
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

        PackageInfo packageInfo = mPackageManager.getPackageArchiveInfo(apkFile.getFile().getPath(), 0);

        // check exist on installed list
        if (isExistOnInstalledList(packageInfo)) {
            apkFile.setInstalled(true);
        }

        holder.tvName.setText(apkFile.getFile().getName());
        holder.tvInstalled.setText(apkFile.isInstalled() ? "Installed" : "Not installed");
        holder.tvPath.setText(apkFile.getFile().getAbsolutePath());
    }

    private boolean isExistOnInstalledList(PackageInfo packageInfo) {
        if (mApplicationInfos != null) {
            for (ApplicationInfo applicationInfo : mApplicationInfos) {
                if (applicationInfo.packageName.equals(packageInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mApkFiles == null ? 0 : mApkFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private TextView tvName;
        private TextView tvInstalled;
        private TextView tvPath;

        public ViewHolder(View v) {
            super(v);

            container = v.findViewById(R.id.container);
            tvName = v.findViewById(R.id.tv_name);
            tvInstalled = v.findViewById(R.id.tv_installed);
            tvPath = v.findViewById(R.id.tv_path);
        }
    }
}
