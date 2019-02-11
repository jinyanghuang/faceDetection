package com.example.michaelhuang.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends ArrayAdapter<Uri> {
    private Activity mContext;
    private ArrayList<Uri> mVideoList;
    private int resourceId;

    public ImageListAdapter(Activity context, int resource, ArrayList<Uri> videoList){
        super(context,resource,videoList);
        mContext = context;
        resourceId = resource;
        mVideoList = videoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View returnView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        Uri videoUri = mVideoList.get(position);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext,videoUri);

        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationInMillisec = Long.parseLong(duration );
        int durationInSec = (int)(durationInMillisec/250);

        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        Bitmap selectedFrontFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Bitmap selectedLeftFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Bitmap selectedRightFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        float dis=100;
        boolean oneSide = false;
        for(int i=0; i<durationInSec-1; i++){
            Bitmap bitmap = retriever.getFrameAtTime(i*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<Face> faces = detector.detect(frame);

            if(bitmap != null && faces.size()!=0){
                System.out.println("hehehere");
                Face face = faces.valueAt(0);
                List<Landmark> landmarks = face.getLandmarks();
                if (landmarks.size() == 8) {
                    float leftFaceLandmark = landmarks.get(4).getPosition().x;
                    float noseLandmark = landmarks.get(2).getPosition().x;
                    float rightLandmark = landmarks.get(3).getPosition().x;
                    float diff = Math.abs((noseLandmark - leftFaceLandmark) - (rightLandmark - noseLandmark));

                    if (diff < dis) {
                        selectedFrontFace = bitmap;
                        dis = diff;
                    }
                    System.out.println(dis);

                }
            }

            Bitmap bitmap2 = retriever.getFrameAtTime((i+1)*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Frame frame2 = new Frame.Builder().setBitmap(bitmap2).build();

            SparseArray<Face> faces2 = detector.detect(frame2);

            if(bitmap != null ){
                if(faces.size()!=0 && faces2.size()==0 && !oneSide){
                    selectedLeftFace = bitmap2;
                    oneSide = true;
                    System.out.println("left face selected");
                }
                if(faces.size()!=0 && faces2.size()==0 && oneSide){
                    selectedRightFace = bitmap2;
                    System.out.println("right face selected");
                }
            }

        }

        ImageView imageView = returnView.findViewById(R.id.imageViewList);
        imageView.setImageBitmap(selectedFrontFace);
        ImageView imageView2 = returnView.findViewById(R.id.imageView2);
        imageView2.setImageBitmap(selectedRightFace);
        ImageView imageView3 = returnView.findViewById(R.id.imageView3);
        imageView3.setImageBitmap(selectedLeftFace);



        return returnView;
    }
}
