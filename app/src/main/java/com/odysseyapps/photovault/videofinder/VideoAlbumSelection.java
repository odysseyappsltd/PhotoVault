package com.odysseyapps.photovault.videofinder;

import java.util.ArrayList;

/**
 * Created by admin on 2018-04-23.
 */

public class VideoAlbumSelection {

    // Singleton Structure
    private static final VideoAlbumSelection instance = new VideoAlbumSelection();
    //private constructor to avoid client applications to use constructor
    private VideoAlbumSelection(){}
    public static VideoAlbumSelection getSharedInstance(){

        return instance;
    }

     public  ArrayList<String> selectedImageURIs = new ArrayList<String>();
     public  ArrayList<String> selectedVideoURIs = new ArrayList<String>();



}
