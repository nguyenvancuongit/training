package com.app.temp.features.home.imagelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

public class PhotoListFragment extends BaseFragment {

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    private PhotoRecyclerViewAdapter adapter;

    private ArrayList<File> mAllPhotoOnDevice;
    private int numberOfColumns = 3;
    private int index = 0;

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
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

        ArrayList<File> mAllPhotoWithFace = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new PhotoRecyclerViewAdapter(getContext(), mAllPhotoWithFace);
        recyclerView.setAdapter(adapter);

        if (isExternalStorageReadable()) {
            mAllPhotoOnDevice = imageReader(Environment.getExternalStorageDirectory());

            new Thread(this::checkContainFaceImageOnList).start();
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

    public boolean isContainFace(Bitmap bitmap) {
        SparseArray<Face> mFaces = new SparseArray<>();

        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setProminentFaceOnly(true)
//                .setTrackingEnabled(false)
//                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                .setMinFaceSize(0.3f)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }

        return (mFaces != null && mFaces.size() > 0);
    }

    public void checkContainFaceImageOnList() {
        Log.d("PhotoListFragment", "***** Checking face for image === " + index + " path = " + mAllPhotoOnDevice.get(index).getPath());
        try {
            // get bitmap from file Path
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(mAllPhotoOnDevice.get(index).getPath(), options);

            // is there a face ?
            boolean isContainFace = isContainFace(bitmap);
            bitmap.recycle();

            // finish
            if (isContainFace) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    Log.d("PhotoListFragment", "***** Face is detected === " + index + " path = " + mAllPhotoOnDevice.get(index).getPath());
                    adapter.addData(mAllPhotoOnDevice.get(index));
                    recyclerView.requestLayout();
                });
            }

            if (index < mAllPhotoOnDevice.size() - 1) {
                index++;
                checkContainFaceImageOnList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
