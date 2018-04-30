package com.odysseyapps.photovault;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class EachFolder extends AppCompatActivity {

    private DatabaseHelper mydb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_folder);
        //Database
        mydb = new DatabaseHelper(this);
        ArrayList<Bitmap> bmp = mydb.getImageAlbum("0");

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new EachFolderAdapter(this, bmp));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
