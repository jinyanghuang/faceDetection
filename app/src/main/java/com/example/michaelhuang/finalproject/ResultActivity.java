package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Bitmap[]> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

//        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(this,videoUri);
//
//        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long durationInMillisec = Long.parseLong(duration );
//        int durationInSec = (int)(durationInMillisec/250);
//
//        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
//                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                .setTrackingEnabled(false)
//                .setMode(FaceDetector.FAST_MODE)
//                .build();
//
//        Bitmap selectedFrontFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//        Bitmap selectedLeftFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//        Bitmap selectedRightFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//
//        float dis=100;
//        boolean oneSide = false;
//        for(int i=0; i<durationInSec-1; i++){
//            Bitmap bitmap = retriever.getFrameAtTime(i*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//
//            SparseArray<Face> faces = detector.detect(frame);
//
//            if(bitmap != null && faces.size()!=0){
//                System.out.println("hehehere");
//                Face face = faces.valueAt(0);
//                List<Landmark> landmarks = face.getLandmarks();
//                if (landmarks.size() == 8) {
//                    float leftFaceLandmark = landmarks.get(4).getPosition().x;
//                    float noseLandmark = landmarks.get(2).getPosition().x;
//                    float rightLandmark = landmarks.get(3).getPosition().x;
//                    float diff = Math.abs((noseLandmark - leftFaceLandmark) - (rightLandmark - noseLandmark));
//
//                    if (diff < dis) {
//                        selectedFrontFace = bitmap;
//                        dis = diff;
//                    }
//                    System.out.println(dis);
//
//                }
//            }
//
//            Bitmap bitmap2 = retriever.getFrameAtTime((i+1)*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//            Frame frame2 = new Frame.Builder().setBitmap(bitmap2).build();
//
//            SparseArray<Face> faces2 = detector.detect(frame2);
//
//            if(bitmap != null ){
//                if(faces.size()!=0 && faces2.size()==0 && oneSide){
//                    selectedRightFace = bitmap2;
//                    System.out.println("right face selected");
//                }
//                if(faces.size()!=0 && faces2.size()==0 && !oneSide){
//                    selectedLeftFace = bitmap2;
//                    oneSide = true;
//                    System.out.println("left face selected");
//                }
//            }
//
//        }
//
//        Bitmap[] bitmaps = {selectedFrontFace,selectedLeftFace,selectedRightFace};
//
//        TempDatabase.addlist(bitmaps);


        imageList = TempDatabase.getImageList();

        ImageListAdapter imageAdapter = new ImageListAdapter(this,R.layout.image_list_view,imageList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(imageAdapter);
    }

    public void addMore(View view){
        Intent recordMore = new Intent(this, RecordingPageActivity.class);
        startActivity(recordMore);
    }

    public void terminate(View view){

    }

}
