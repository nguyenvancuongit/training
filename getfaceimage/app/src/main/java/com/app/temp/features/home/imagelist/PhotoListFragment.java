package com.app.temp.features.home.imagelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;

import com.app.temp.R;
import com.app.temp.base.fragment.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.Observable;

public class PhotoListFragment extends BaseFragment {

    @BindView(R.id.rvNumbers)
    RecyclerView recyclerView;

    private PhotoRecyclerViewAdapter adapter;

    private ArrayList<File> mAllPhotoWithFace;
    private ArrayList<File> mAllPhotoOnDevice;
    private int numberOfColumns = 4;
    private int indexAllPhotoOnDevice = 0;

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

        mAllPhotoWithFace = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new PhotoRecyclerViewAdapter(getContext(), mAllPhotoWithFace, position -> {
            if (position == 0) {
                goToNextFile();
            }
        });
        recyclerView.setAdapter(adapter);

        if (isExternalStorageReadable()) {
            mAllPhotoOnDevice = imageReader(new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/"));

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
        Log.d("PhotoListFragment", "***** Checking face for image === " + indexAllPhotoOnDevice + " path = " + mAllPhotoOnDevice.get(indexAllPhotoOnDevice).getPath());
        Observable<Boolean> observable = Observable.create(emitter -> {
            try {
                // get bitmap from file Path
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(mAllPhotoOnDevice.get(indexAllPhotoOnDevice).getPath(), options);

                // is there a face ?
                boolean isContainFace = isContainFace(bitmap);
                bitmap.recycle();

                // finish
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    emitter.onNext(isContainFace);
                    emitter.onComplete();
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        disposable = observable.subscribe(isContainFace -> {
            if (isContainFace) {
                Log.d("PhotoListFragment", "***** Face is detected === " + indexAllPhotoOnDevice + " path = " + mAllPhotoOnDevice.get(indexAllPhotoOnDevice).getPath());
                adapter.addData(mAllPhotoOnDevice.get(indexAllPhotoOnDevice));
                recyclerView.requestLayout();
            } else {
                goToNextFile();
            }
        });
    }

    private void goToNextFile() {
        if (indexAllPhotoOnDevice < mAllPhotoOnDevice.size() - 1) {
            indexAllPhotoOnDevice++;
            checkContainFaceImageOnList();
        }
    }
}
