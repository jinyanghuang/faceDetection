package com.example.michaelhuang.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<Uri> {
    private Activity mContext;
//    private ArrayList<Bitmap> mImages;
    private ArrayList<Uri> mVideoList;
    private int resourceId;

    public ImageListAdapter(Activity context, int resource, ArrayList<Uri> videoList){
//        super(context, resource, images);
        super(context,resource,videoList);
        mContext = context;
        resourceId = resource;
//        mImages = images;
        mVideoList = videoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        View returnView = inflater.inflate(R.layout.result_page,null);
        View returnView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        Uri videoUri = mVideoList.get(position);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mContext,videoUri);

        ArrayList<Bitmap> frameList;
        frameList = new ArrayList<Bitmap>();
        for(int i=0;i<3;i++) {
            Bitmap bitmap = retriever.getFrameAtTime(i*1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            frameList.add(bitmap);
        }

        ImageView imageView = returnView.findViewById(R.id.imageViewList);
        imageView.setImageBitmap(frameList.get(0));

        ImageView imageView2 = returnView.findViewById(R.id.imageView2);
        imageView2.setImageBitmap(frameList.get(1));

        ImageView imageView3 = returnView.findViewById(R.id.imageView3);
        imageView3.setImageBitmap(frameList.get(2));

        return returnView;
    }
}
