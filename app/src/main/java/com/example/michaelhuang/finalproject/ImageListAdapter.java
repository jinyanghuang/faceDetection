package com.example.michaelhuang.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<Bitmap> {
//    private Activity mContext;
    private ArrayList<Bitmap> mImages;
    private int resourceId;

    public ImageListAdapter(Activity context, int resource, ArrayList<Bitmap> images){
        super(context, resource, images);
//        mContext = context;
        resourceId = resource;
        mImages = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        View returnView = inflater.inflate(R.layout.result_page,null);
        View returnView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView = returnView.findViewById(R.id.imageViewList);
        imageView.setImageBitmap(mImages.get(position));
        return returnView;
    }
}
