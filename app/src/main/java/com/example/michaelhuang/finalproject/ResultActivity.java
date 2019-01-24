package com.example.michaelhuang.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
    }

    public void addMore(View view){
        Intent recordMore = new Intent(this, RecordingPageActivity.class);
        startActivity(recordMore);
    }
}