package com.example.michaelhuang.finalproject;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class TempDatabase {

    private static ArrayList<Bitmap[]> imageList = new ArrayList<Bitmap[]>();

    public static void addlist(Bitmap[] bitmaps){imageList.add(bitmaps);}

    public static ArrayList<Bitmap[]> getImageList() {return imageList;}
}
