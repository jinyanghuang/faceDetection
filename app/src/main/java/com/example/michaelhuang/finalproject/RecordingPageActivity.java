package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;

public class RecordingPageActivity extends AppCompatActivity {

    private static int VIDEO_REQUEST =101;
    private Uri videoUri = null;
    private ProgressBar pgsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_page);

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(videoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(videoIntent,VIDEO_REQUEST);
        }

        pgsBar = (ProgressBar)findViewById(R.id.pBar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==VIDEO_REQUEST && resultCode ==RESULT_OK){
            videoUri = data.getData();
        }
    }

    public void result(View view){
//        Intent resultPage = new Intent(this, ResultActivity.class);
////        resultPage.putExtra("videoUri",videoUri.toString());
////        startActivity(resultPage);
        pgsBar.setVisibility(view.VISIBLE);
        new SelectFace().execute();
    }

    public void recordAgain(View view){
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(videoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(videoIntent,VIDEO_REQUEST);
        }
    }

    public class SelectFace extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent resultPage = new Intent(getApplicationContext(), ResultActivity.class);
            getApplicationContext().getContentResolver().delete(videoUri, null, null);
            startActivity(resultPage);

        }

        @Override
        protected String doInBackground(String... strings) {

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(),videoUri);

            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long durationInMillisec = Long.parseLong(duration );
            int durationInSec = (int)(durationInMillisec/250);

            FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .setTrackingEnabled(false)
                    .setMode(FaceDetector.FAST_MODE)
                    .build();

            Bitmap selectedFrontFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Bitmap selectedLeftFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Bitmap selectedRightFace = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            float dis=100;
            boolean oneSide = false;
            System.out.println("total iterations "+durationInSec);
            for(int i=0; i<durationInSec-1; i++){
                Bitmap bitmap = retriever.getFrameAtTime(i*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                SparseArray<Face> faces = detector.detect(frame);

                if(bitmap != null && faces.size()!=0){
                    System.out.println("face is detected");
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
                        System.out.println("all 8 landmarks are detected");
                        System.out.println(dis);

                    }
                }

                Bitmap bitmap2 = retriever.getFrameAtTime((i+1)*250000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                Frame frame2 = new Frame.Builder().setBitmap(bitmap2).build();

                SparseArray<Face> faces2 = detector.detect(frame2);

                if(bitmap != null ){
                    if(faces.size()!=0 && faces2.size()==0 && oneSide){
                        selectedRightFace = bitmap2;
                        System.out.println("right face selected");
                    }
                    if(faces.size()!=0 && faces2.size()==0 && !oneSide){
                        selectedLeftFace = bitmap2;
                        oneSide = true;
                        System.out.println("left face selected");
                    }
                }

            }

            Bitmap[] bitmaps = {selectedFrontFace,selectedLeftFace,selectedRightFace};

            TempDatabase.addlist(bitmaps);
            return null;
        }
    }

}
