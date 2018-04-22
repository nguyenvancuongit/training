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

public class PhotoListFragment extends BaseFragment {

    @BindView(R.id.gridview)
    GridView gridView;

    public static PhotoListFragment newInstance() {
        PhotoListFragment fragment = new PhotoListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isExternalStorageReadable()) {
            ArrayList<File> imageFiles = imageReader(Environment.getExternalStorageDirectory());
            for (File file : imageFiles) {
                printLog("image name = " + file.getName());
            }

            gridView.setAdapter(new PhotoAdapter(getContext(), imageFiles));
            gridView.setOnItemClickListener((parent, v, position, id) -> Toast.makeText(getActivity(), "" + position,
                    Toast.LENGTH_SHORT).show());
        }
    }

    ArrayList<File> imageReader(File root) {
        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                a.addAll(imageReader(file));
            } else {
                if (file.getName().toLowerCase().endsWith(".jpg")
                        || file.getName().toLowerCase().endsWith(".jpeg")
                        || file.getName().toLowerCase().endsWith(".png")
                        || file.getName().toLowerCase().endsWith(".bmp")
                        || file.getName().toLowerCase().endsWith(".svg")
                        ) {
                    a.add(file);
                }
            }
        }
        return a;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
