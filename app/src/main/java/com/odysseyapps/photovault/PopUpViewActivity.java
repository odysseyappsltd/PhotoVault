package com.odysseyapps.photovault;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class PopUpViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        int border = (int) getResources().getDimension(R.dimen.aspect_controller);
        getWindow().setLayout((int)(width*1.0),(int)(border));

    }
    public void cancelAct(View view){

        finish();
    }
    public void galleryAct(View view){
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "gallery");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
    public void capturePhotoAct(View view){
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "capturePhoto");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
    public void captureVideoAct(View view){
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "captureVideo");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }
}
