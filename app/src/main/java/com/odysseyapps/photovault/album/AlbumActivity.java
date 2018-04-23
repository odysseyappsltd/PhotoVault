package com.odysseyapps.photovault.album;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.odysseyapps.photovault.R;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 100;
    boolean didPressedGalleryButton = false ;
    public static ArrayList<Model_images> al_images = new ArrayList<Model_images>();
    public static ArrayList<String> allImage = new ArrayList<String>();
    boolean boolean_folder ;

     ListView folderList ;
     GridView photoThumbnails ;
     Button galleryButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album);


        folderList = (ListView) findViewById(R.id.AAlistView);
        photoThumbnails = (GridView) findViewById(R.id.AAGridView);
        galleryButton = (Button) findViewById(R.id.AAGallery);

        folderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GridViewAdapter gridViewAdapter = new GridViewAdapter(AlbumActivity.this,AlbumActivity.al_images,position);
                photoThumbnails.setAdapter(gridViewAdapter);

                if (position == 0) {
                    galleryButton.setText(getString(R.string.CameraRoll));
                }
                else{
                    galleryButton.setText(al_images.get(position-1).getStr_folder());
                }
                folderList.setVisibility(View.INVISIBLE);
                didPressedGalleryButton = false;


            }
        });

        photoThumbnails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }


        });


        galleryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Perform action on click
                if (didPressedGalleryButton == false) {
                    folderList.setVisibility(View.VISIBLE);

                   ListViewAdapter obj_adapter = new ListViewAdapter(getApplicationContext(), al_images);
                    folderList.setAdapter(obj_adapter);
                    didPressedGalleryButton = true;
                }
                else {
                    folderList.setVisibility(View.INVISIBLE);
                    didPressedGalleryButton = false;
                }
            }
        });




        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(AlbumActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

        }else {
            Log.e("Else","Else");

            fn_imagespath();
            GridViewAdapter gridViewAdapter = new GridViewAdapter(AlbumActivity.this,AlbumActivity.al_images,0);
            photoThumbnails.setAdapter(gridViewAdapter);
            folderList.setVisibility(View.INVISIBLE);

            didPressedGalleryButton = false;

        }




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
//                for (int i = 0; i < grantResults.length; i++) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {


                    fn_imagespath();
                    GridViewAdapter gridViewAdapter = new GridViewAdapter(AlbumActivity.this,AlbumActivity.al_images,0);
                    photoThumbnails.setAdapter(gridViewAdapter);
                    folderList.setVisibility(View.INVISIBLE);
                    didPressedGalleryButton = false;

                }else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    //Toast.makeText(MainActivity.this, "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
                    // User selected the Never Ask Again Option
                    goSettingsPage();
                } else {
                    Toast.makeText(AlbumActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
//
            }
        }
    }

    public void goSettingsPage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(getString(R.string.GoSettingsMsg));
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                dialog.cancel();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }






    public ArrayList<Model_images> fn_imagespath() {
        al_images.clear();
        allImage.clear();

        int int_position = 0;
        int column_index_data, column_index_folder_name;
        String absolutePathOfImage = null;

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Log.e("projection", String.valueOf(projection));
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                java.util.ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);



            }


        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
                allImage.add(al_images.get(i).getAl_imagepath().get(j)) ;

//                File file = new File (al_images.get(i).getAl_imagepath().get(j));
//                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
//                allBitmap.add(bitmap);

            }
        }

//        obj_adapter = new ListViewAdapter(getApplicationContext(),al_images);
//        folderList.setAdapter(obj_adapter);
        if(al_images.size() != 0) {
            Button galleryButton = (Button)findViewById(R.id.AAGallery)  ;
            galleryButton.setText(getString(R.string.CameraRoll));
        }
        return al_images;
    }
}
