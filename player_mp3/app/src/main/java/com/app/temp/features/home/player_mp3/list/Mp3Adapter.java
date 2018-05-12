package com.app.temp.features.home.player_mp3.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.temp.R;
import com.app.temp.pojo.Mp3File;

import java.text.DecimalFormat;
import java.util.List;

public class Mp3Adapter extends RecyclerView.Adapter<Mp3Adapter.ViewHolder> {

    private List<Mp3File> mApkFiles;
    private OnItemSelected onItemSelected;

    public Mp3Adapter(List<Mp3File> mApkFiles, OnItemSelected onItemSelected) {
        this.mApkFiles = mApkFiles;
        this.onItemSelected = onItemSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rc_mp3, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mp3File mp3File = mApkFiles.get(position);
        holder.tvName.setText(mp3File.getName());
        holder.tvSize.setText(getFileSize(Integer.parseInt(String.valueOf(mp3File.length()))));

        holder.itemView.setOnClickListener(v -> onItemSelected.selectItem(mp3File));
    }

    @Override
    public int getItemCount() {
        return mApkFiles == null ? 0 : mApkFiles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvSize;

        ViewHolder(View v) {
            super(v);

            tvName = v.findViewById(R.id.tv_name);
            tvSize = v.findViewById(R.id.tv_size);
        }
    }

    private static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public interface OnItemSelected {
        void selectItem(Mp3File file);
    }
}
