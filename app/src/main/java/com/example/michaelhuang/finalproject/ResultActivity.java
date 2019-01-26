package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Bitmap> frameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this,videoUri);
        frameList = new ArrayList<Bitmap>();
        for(int i=0;i<3;i++) {
            Bitmap bitmap = retriever.getFrameAtTime(i*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            frameList.add(bitmap);
        }


    }

    public void addMore(View view){
        Intent recordMore = new Intent(this, RecordingPageActivity.class);
        startActivity(recordMore);
    }
}
