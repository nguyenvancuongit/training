package com.app.temp.features.home.imagelist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.temp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.actions.ItemListIntents;

import java.io.File;
import java.util.ArrayList;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<File> mData;
    private LayoutInflater mInflater;
    private ItemLoadListener mLoadListener;

    // data is passed into the constructor
    PhotoRecyclerViewAdapter(Context context, ArrayList<File> data, ItemLoadListener loadListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        this.mLoadListener = loadListener;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the image view in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = mData.get(position);

        Glide.with(mContext)
                .load(file)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mLoadListener.onLoadItem(position);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mLoadListener.onLoadItem(position);
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(File file) {
        mData.add(0, file);
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

    // parent activity will implement this method to respond to click events
    public interface ItemLoadListener {
        void onLoadItem(int position);
    }
}
