package com.app.temp.features.home.app_manager.installed_list;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.temp.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private List<ApplicationInfo> mApps;
    private Context context;
    private PackageManager packageManager;

    public AppAdapter(Context context, List<ApplicationInfo> applicationInfos) {
        this.mApps = applicationInfos;
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rc_app_installed, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo app = mApps.get(position);
        holder.tvName.setText(app.loadLabel(packageManager));

        File file = new File(app.publicSourceDir);
        holder.tvSize.setText(String.valueOf(getFileSize(file.length())));
        Glide.with(context).load(app.loadIcon(packageManager)).into(holder.imgThumbnail);
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public int getItemCount() {
        return mApps == null ? 0 : mApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;
        private TextView tvName;
        private TextView tvSize;

        public ViewHolder(View v) {
            super(v);

            imgThumbnail = v.findViewById(R.id.img_thumbnail);
            tvName = v.findViewById(R.id.tv_name);
            tvSize = v.findViewById(R.id.tv_size);
        }
    }
}
