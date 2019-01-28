package com.example.michaelhuang.finalproject;

import android.net.Uri;

import java.util.ArrayList;

public class TempDatabase {
    private static ArrayList<Uri> list = new ArrayList<Uri>();

    public static void addlist(Uri rec) {list.add(rec);}

    public static ArrayList<Uri> getList() {return list;}
}
