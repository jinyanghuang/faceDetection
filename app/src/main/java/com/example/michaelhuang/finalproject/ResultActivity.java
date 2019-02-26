package com.example.michaelhuang.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Bitmap[]> imageList;
    ImageListAdapter imageAdapter;
    int inversePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        imageList = TempDatabase.getImageList();

        imageAdapter = new ImageListAdapter(this,R.layout.image_list_view,imageList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(imageAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //define the object of AlertDialog.Builder, display the delete dialog when long click the item from the list
                AlertDialog.Builder builder=new AlertDialog.Builder(ResultActivity.this);
                builder.setMessage("sure delete?");
                builder.setTitle("remainder");

                // reverse the position order
                int lengthOfBitmap = imageList.size();
                inversePosition = lengthOfBitmap - position -1; //show the latest image on the top
                // add setPositiveButton method for builder
                builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(imageList.remove(inversePosition)!=null){
                            System.out.println("success");
                        }else {
                            System.out.println("failed");
                        }
                        imageAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "delete item", Toast.LENGTH_SHORT).show();
                    }
                });

                //add setNegativeButton() method
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return false;
            }
        });
    }

    public void addMore(View view){
        Intent recordMore = new Intent(this, RecordingPageActivity.class);
        startActivity(recordMore);
    }

    public void terminate(View view){

    }

}
