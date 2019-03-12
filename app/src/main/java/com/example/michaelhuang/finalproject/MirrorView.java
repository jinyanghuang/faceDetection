package com.example.michaelhuang.finalproject;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MirrorView extends SurfaceView implements
        SurfaceHolder.Callback,Camera.PictureCallback, Camera.ShutterCallback {

    public static String DEBUG_TAG = "debug";

    private SurfaceHolder mHolder;
    private Camera mCamera;
    Context mContext;
    private Bitmap photo;
    float dis=100;
    private Bitmap selectedFrontFace;
    private Bitmap selectedLeftFace;
    private Bitmap selectedRightFace;
    private int pictureIndex=0;
    boolean oneSide = false;
    private Bitmap previousPhoto;
    FaceDetector detector;

    public MirrorView(Context context, Camera camera) {
        super(context);
        this.mContext = context;
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception error) {
            Log.d(DEBUG_TAG,
                    "Error starting mPreviewLayout: " + error.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
        if (mHolder.getSurface() == null) {
            return;
        }

        // can't make changes while mPreviewLayout is active
        try {
            mCamera.stopPreview();
        } catch (Exception e) {

        }

        try {

            // start up the mPreviewLayout
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception error) {
            Log.d(DEBUG_TAG,
                    "Error starting mPreviewLayout: " + error.getMessage());
        }

    }



    public void setCameraDisplayOrientationAndSize(int mCameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        int rotation = ((Activity)mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        if (result == 90 || result == 270) {
            mHolder.setFixedSize(previewSize.height, previewSize.width);
        } else {
            mHolder.setFixedSize(previewSize.width, previewSize.height);

        }
    }

    public void takePicture() {
        mCamera.takePicture(this, null, this);
    }

    @Override
    public void onShutter() {
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
//        try {
        pictureIndex++;

        if(pictureIndex>1){
            previousPhoto=photo;
        }
            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
            photo = RotateBitmap(bitmap,270);

            new SelectFace().execute();
//            if(photo!=null){
//                File file=new File(Environment.getExternalStorageDirectory()+"/dirr");
//                if(!file.isDirectory()){
//                    file.mkdir();
//                }
//
//                file=new File(Environment.getExternalStorageDirectory()+"/dirr",System.currentTimeMillis()+".jpg");
//
//
//
//                FileOutputStream fileOutputStream=new FileOutputStream(file);
//                photo.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);
//
//                fileOutputStream.flush();
//                fileOutputStream.close();
//
//
//            }


            //重新启动预览
            mCamera.startPreview();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void continuousShooting(final int shootingTimes) {
        Executor executor = Executors.newSingleThreadExecutor();
        detector = new FaceDetector.Builder(getContext())
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < shootingTimes; i++) {
                    takePicture();
                    try {
                        SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap[] bitmaps = {selectedFrontFace,selectedLeftFace,selectedRightFace};
                TempDatabase.addlist(bitmaps);
                Intent resultPage = new Intent(getContext(), ResultActivity.class);
                mContext.startActivity(resultPage);
            }
        });
    }


    public class SelectFace extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent resultPage = new Intent(getContext(), ResultActivity.class);
//            getApplicationContext().getContentResolver().delete(videoUri, null, null);
//            startActivity(resultPage);

        }

        @Override
        protected String doInBackground(String... strings) {

            System.out.println("fuccccc");


            Frame frame = new Frame.Builder().setBitmap(photo).build();

            SparseArray<Face> faces = detector.detect(frame);

            System.out.println(photo);
            System.out.println(faces.size());

                if(photo != null && faces.size()!=0){
                    System.out.println("face is detected");
                    Face face = faces.valueAt(0);
                    List<Landmark> landmarks = face.getLandmarks();
                    if (landmarks.size() == 8) {
                        float leftFaceLandmark = landmarks.get(4).getPosition().x;
                        float noseLandmark = landmarks.get(2).getPosition().x;
                        float rightLandmark = landmarks.get(3).getPosition().x;
                        float diff = Math.abs((noseLandmark - leftFaceLandmark) - (rightLandmark - noseLandmark));
                        if (diff < dis) {
                            selectedFrontFace = photo;
                            dis = diff;
                        }

                        System.out.println("all 8 landmarks are detected");
                        System.out.println(dis);


                    }
                }

            if(pictureIndex > 1) {

                Frame frame2 = new Frame.Builder().setBitmap(previousPhoto).build();

                SparseArray<Face> faces2 = detector.detect(frame2);

                if (photo != null) {
                    if (faces2.size() != 0 && faces.size() == 0 && oneSide) {
                        selectedRightFace = previousPhoto;
                        System.out.println("right face selected");
                    }
                    if (faces2.size() != 0 && faces.size() == 0 && !oneSide) {
                        selectedLeftFace = previousPhoto;
                        oneSide = true;
                        System.out.println("left face selected");
                    }
                }
            }

            return null;
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
