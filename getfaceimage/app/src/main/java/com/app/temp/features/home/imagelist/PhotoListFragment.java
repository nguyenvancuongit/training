package com.app.temp.features.home.imagelist;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
        adapter = new PhotoRecyclerViewAdapter(getContext(), mAllPhotoWithFace);
        recyclerView.setAdapter(adapter);

        if (isExternalStorageReadable()) {
//            mAllPhotoOnDevice = imageReader(new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/"));
            mAllPhotoOnDevice = getPhotoFromGallery();
            Log.d("PhotoFragment", "mAllPhotoOnDevice = " + mAllPhotoOnDevice.size());
            new Thread(this::checkContainFaceImageOnList).start();
        }
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

    public boolean isContainFace(Bitmap bitmap) {
        SparseArray<Face> mFaces = new SparseArray<>();

        FaceDetector detector = new FaceDetector.Builder(getContext())
//                .setProminentFaceOnly(true)
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
        Log.d("PhotoFragment", "checkContainFaceImageOnList = " + indexAllPhotoOnDevice);
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
                emitter.onNext(isContainFace);
                emitter.onComplete();

            } catch (Exception e) {
                emitter.onError(e);
            }
        });

        disposable = observable.subscribe(isContainFace -> {
            if (isContainFace) {
                Log.d("PhotoFragment", "isContainFace = " + indexAllPhotoOnDevice);
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    adapter.addData(mAllPhotoOnDevice.get(indexAllPhotoOnDevice));
                    recyclerView.requestLayout();

                    goToNextFile();
                });
            } else {
                goToNextFile();
            }
        });
    }

    private void goToNextFile() {
        if (indexAllPhotoOnDevice < mAllPhotoOnDevice.size() - 1) {
            indexAllPhotoOnDevice++;
            new Thread(this::checkContainFaceImageOnList).start();
        }
    }
}
