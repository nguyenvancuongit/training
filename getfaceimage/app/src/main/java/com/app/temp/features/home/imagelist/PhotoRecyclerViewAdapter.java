package com.app.temp.features.home.imagelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.temp.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<File> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    PhotoRecyclerViewAdapter(Context context, ArrayList<File> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the image view in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = mData.get(position);

        Glide.with(mContext)
                .load(file)
                .into(holder.imageView);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(File file) {
        mData.add(file);
        notifyDataSetChanged();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.info_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
