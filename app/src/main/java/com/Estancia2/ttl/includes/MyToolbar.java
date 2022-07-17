package com.Estancia2.ttl.includes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Estancia2.ttl.R;

public class MyToolbar {
    public static void show(AppCompatActivity activity,String title,boolean upBotton){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upBotton);


    }


}
