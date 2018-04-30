package com.odysseyapps.photovault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.odysseyapps.photovault.photofinder.AlbumActivity;
import com.odysseyapps.photovault.videofinder.VideoAlbumActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goButton = findViewById(R.id.AMGoButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoAlbumActivity.class);
                startActivity ( intent );
            }
        });



    }
}
