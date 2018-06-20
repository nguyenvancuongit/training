package com.app.temp.features.home.imagelist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MyThread {

    private Context context;
    private String threadName;
    private Runnable runnable;
    private Thread thread;

    private ArrayList<File> allPhotoOnDevice;
    private int numberAtStarting;
    private int numberAtEnding;
    private int currentIndex;

    private OnAddNewFaceImage onAddNewFaceImage;

    public MyThread(Context context, String threadName, ArrayList<File> allPhotoWithFace, int starting, int ending,
                    OnAddNewFaceImage onAddNewFaceImage) {
        this.context = context;
        this.threadName = threadName;
        this.runnable = this::checkContainFaceImageOnList;
        this.thread = new Thread(runnable);

        this.allPhotoOnDevice = allPhotoWithFace;
        this.numberAtStarting = starting;
        this.numberAtEnding = ending;
        this.currentIndex = numberAtStarting;

        this.onAddNewFaceImage = onAddNewFaceImage;

        Log.d("PhotoFragment", "threadName = " + threadName + "\n"
                + "numberAtStarting = " + numberAtStarting + "\n"
                + "numberAtEnding = " + numberAtEnding + "\n"
                + "currentIndex = " + currentIndex
        );
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public int getNumberAtStarting() {
        return numberAtStarting;
    }

    public void setNumberAtStarting(int numberAtStarting) {
        this.numberAtStarting = numberAtStarting;
    }

    public int getNumberAtEnding() {
        return numberAtEnding;
    }

    public void setNumberAtEnding(int numberAtEnding) {
        this.numberAtEnding = numberAtEnding;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private boolean isContainFace(Bitmap bitmap) {
        SparseArray<Face> mFaces = new SparseArray<>();

        FaceDetector detector;
        if (PhotoListFragment.isGetFeaturePhoto) {
            detector = new FaceDetector.Builder(context).setProminentFaceOnly(true).build();
        } else {
            detector = new FaceDetector.Builder(context).build();
        }

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }

        return (mFaces != null && mFaces.size() > 0);
    }

    private void checkContainFaceImageOnList() {
        Log.d("PhotoFragment", getThreadName() + ", checkContainFaceImageOnList = " + currentIndex);
        try {
            // get bitmap from file Path
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(allPhotoOnDevice.get(currentIndex).getPath(), options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 512, 384);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(allPhotoOnDevice.get(currentIndex).getPath(), options);

            // is there a face ?
            boolean isContainFace = isContainFace(bitmap);
            bitmap.recycle();

            if (isContainFace) {
                int finalIndex = currentIndex;
                Objects.requireNonNull((Activity) context).runOnUiThread(() -> {
                    onAddNewFaceImage.addNewFaceImage(allPhotoOnDevice.get(finalIndex));
                });
                goToNextFile();
            } else {
                goToNextFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToNextFile() {
        if (currentIndex < numberAtEnding
                && currentIndex < allPhotoOnDevice.size() - 1) {
            currentIndex++;
            checkContainFaceImageOnList();
        }
    }

    public interface OnAddNewFaceImage {
        void addNewFaceImage(File file);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
