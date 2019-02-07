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
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        View returnView = inflater.inflate(R.layout.result_page,null);
        View returnView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        Uri videoUri = mVideoList.get(position);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext,videoUri);

        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long durationInMillisec = Long.parseLong(duration );
        int durationInSec = (int)(durationInMillisec/1000);

        ArrayList<Bitmap> frameList;
        frameList = new ArrayList<Bitmap>();
//        for(int i=0;i<3;i++) {
//            Bitmap bitmap = retriever.getFrameAtTime(i*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//            frameList.add(bitmap);
//        }
//
//        ImageView imageView = returnView.findViewById(R.id.imageViewList);
//        imageView.setImageBitmap(frameList.get(0));
//
//        ImageView imageView2 = returnView.findViewById(R.id.imageView2);
//        imageView2.setImageBitmap(frameList.get(1));
//
//        ImageView imageView3 = returnView.findViewById(R.id.imageView3);
//        imageView3.setImageBitmap(frameList.get(2));

        FaceDetector detector = new FaceDetector.Builder(getContext())
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        Bitmap selectedFace = retriever.getFrameAtTime(0*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        float dis=100;
        for(int i=0; i<durationInSec; i++){
            Bitmap bitmap = retriever.getFrameAtTime(i*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<Face> faces = detector.detect(frame);

            if(bitmap != null && faces!=null){

                Face face = faces.valueAt(0);
                List<Landmark> landmarks = face.getLandmarks();
                float leftFaceLandmark = landmarks.get(4).getPosition().x ;
                float noseLandmark = landmarks.get(2).getPosition().x ;
                float rightLandmark = landmarks.get(3).getPosition().x;
                float diff = Math.abs((noseLandmark - leftFaceLandmark)-(rightLandmark - noseLandmark));
                if (diff < dis){
                    selectedFace = bitmap;
                    dis = diff;
                }
                System.out.println(dis);
            }
        }

        ImageView imageView = returnView.findViewById(R.id.imageViewList);
        imageView.setImageBitmap(selectedFace);


        return returnView;
    }
}
