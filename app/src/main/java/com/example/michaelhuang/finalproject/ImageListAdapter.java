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

public class ImageListAdapter extends ArrayAdapter<Bitmap[]> {
    private Activity mContext;
    private ArrayList<Uri> mVideoList;
    private ArrayList<Bitmap[]> mImageList;
    private int resourceId;

    public ImageListAdapter(Activity context, int resource, ArrayList<Bitmap[]> imageList){
        super(context,resource,imageList);
        mContext = context;
        resourceId = resource;
        mImageList = imageList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View returnView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        Bitmap[] bitmaps = mImageList.get(position);

        ImageView imageView = returnView.findViewById(R.id.imageViewList);
        imageView.setImageBitmap(bitmaps[0]);
        ImageView imageView2 = returnView.findViewById(R.id.imageView2);
        imageView2.setImageBitmap(bitmaps[1]);
        ImageView imageView3 = returnView.findViewById(R.id.imageView3);
        imageView3.setImageBitmap(bitmaps[2]);



        return returnView;
    }
}
