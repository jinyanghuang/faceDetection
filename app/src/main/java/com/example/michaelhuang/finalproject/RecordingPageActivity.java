package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RecordingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_page);
    }

    public void stop(View view){
        Intent resultPage = new Intent(this, ResultActivity.class);
        startActivity(resultPage);
    }
}
