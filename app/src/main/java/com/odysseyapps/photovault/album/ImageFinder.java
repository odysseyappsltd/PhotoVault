package com.odysseyapps.photovault.album;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by admin on 2018-04-18.
 */

public class ImageFinder {


    // Singleton Structure
    private static final ImageFinder instance = new ImageFinder();
    //private constructor to avoid client applications to use constructor
    private ImageFinder(){}
    public static ImageFinder getSharedInstance(){
        return instance;
    }




}
