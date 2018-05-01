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
        Glide.with(context).load(app.loadIcon(packageManager)).into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgThumbnail;
        private TextView tvName;

        public ViewHolder(View v) {
            super(v);

            imgThumbnail = v.findViewById(R.id.img_thumbnail);
            tvName = v.findViewById(R.id.tv_name);
        }
    }
}
