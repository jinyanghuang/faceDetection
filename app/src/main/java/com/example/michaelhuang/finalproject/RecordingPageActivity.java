package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RecordingPageActivity extends AppCompatActivity {

    private static int VIDEO_REQUEST =101;
    private Uri videoUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_page);

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(videoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(videoIntent,VIDEO_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==VIDEO_REQUEST && resultCode ==RESULT_OK){
            videoUri = data.getData();
        }
    }

    public void result(View view){
        Intent resultPage = new Intent(this, ResultActivity.class);
        resultPage.putExtra("videoUri",videoUri.toString());
        startActivity(resultPage);
    }

    public void recordAgain(View view){
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(videoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(videoIntent,VIDEO_REQUEST);
        }
    }
}
