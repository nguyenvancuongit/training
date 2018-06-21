package com.app.temp.features.home.imagelist;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

public class PhotoListFragment extends BaseFragment {

    public static final int NUMBER_OF_THREADS = 5;

    public static boolean isGetFeaturePhoto = false;

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;
    @BindView(R.id.sw_feature_image)
    Switch swFeaturePhoto;

    private PhotoRecyclerViewAdapter adapter;

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();
        scanPhoto();

        // switch is changed listener
        swFeaturePhoto.setOnClickListener(v -> {
            isGetFeaturePhoto = !isGetFeaturePhoto;
            setUpRecyclerView();
            scanPhoto();
        });
    }

    private void setUpRecyclerView() {
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new PhotoRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<File> getPhotoFromGallery() {
        long time = System.currentTimeMillis();
        String[] columns = {
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_ADDED
        };
        ArrayList<File> files = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                File newFile = new File(cursor.getString(1));
                files.add(new File(newFile.getPath()));
//                Log.d("PhotoFragment", "getPhotoFromGallery = " + newFile.getPath());
            } while (cursor.moveToNext());
        }

        Log.d("PhotoFragment", "getPhotoFromGallery time spend = " + (System.currentTimeMillis() - time));
        return files;
    }

    ArrayList<File> imageReader(File root) {
        long time = System.currentTimeMillis();
        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                a.addAll(imageReader(file));
            } else {
                if (file.getName().toLowerCase().endsWith(".jpg")
                        || file.getName().toLowerCase().endsWith(".jpeg")
                        || file.getName().toLowerCase().endsWith(".png")
                        ) {
                    a.add(file);
                }
            }
        }

        Log.d("PhotoFragment", "imageReader time spend = " + (System.currentTimeMillis() - time));
        return a;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private void scanPhoto() {
        if (isExternalStorageReadable()) {
            ArrayList<File> mAllPhotoOnDevice = imageReader(new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/"));
//           ArrayList<File> mAllPhotoOnDevice = getPhotoFromGallery();
            Log.d("PhotoFragment", "mAllPhotoOnDevice = " + mAllPhotoOnDevice.size());

            int pointStarting = mAllPhotoOnDevice.size() / NUMBER_OF_THREADS;
            Log.d("PhotoFragment", "pointStarting = " + pointStarting);

            ArrayList<MyThread> myThreads = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                myThreads.add(new MyThread(getContext(),
                        "Thread number = " + i,
                        mAllPhotoOnDevice,
                        i * pointStarting,
                        ((i + 1) * pointStarting) - 1,
                        file -> {
                            adapter.addData(file);
                            recyclerView.requestLayout();
                        }));
            }
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                myThreads.get(i).getThread().start();
            }
        }
    }
}
