package com.app.temp.features.home.imagelist;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

public class ImageListFragment extends BaseFragment {

    @BindView(R.id.gridview)
    GridView gridView;

    public static ImageListFragment newInstance() {
        ImageListFragment fragment = new ImageListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView.setAdapter(new ImageAdapter(getContext()));
        gridView.setOnItemClickListener((parent, v, position, id) -> Toast.makeText(getActivity(), "" + position,
                Toast.LENGTH_SHORT).show());

        ArrayList<File> imageFiles = imageReader(Environment.getExternalStorageDirectory());
        for (File file : imageFiles) {
            printLog("image name = " + file.getName());
        }
    }

    ArrayList<File> imageReader(File root) {
        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                a.addAll(imageReader(files[i]));
            } else {
                if (files[i].getName().toLowerCase().endsWith(".jpg")
                        || files[i].getName().toLowerCase().endsWith(".jpeg")
                        || files[i].getName().toLowerCase().endsWith(".png")
                        || files[i].getName().toLowerCase().endsWith(".bmp")
                        || files[i].getName().toLowerCase().endsWith(".svg")
                        ) {
                    a.add(files[i]);
                }
            }
        }
        return a;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
