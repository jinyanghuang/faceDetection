package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.vision.face.FaceDetector;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Bitmap> frameList;
    ArrayList<Uri> videoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

//        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));

        videoList = TempDatabase.getList();

        ImageListAdapter imageAdapter = new ImageListAdapter(this,R.layout.image_list_view,videoList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(imageAdapter);
    }

    public void addMore(View view){
        Intent recordMore = new Intent(this, RecordingPageActivity.class);
        startActivity(recordMore);
    }

}
