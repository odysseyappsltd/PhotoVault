package com.odysseyapps.photovault.album;

import java.util.ArrayList;

/**
 * Created by admin on 2018-04-23.
 */

public class AlbumSelection {

    // Singleton Structure
    private static final AlbumSelection instance = new AlbumSelection();
    //private constructor to avoid client applications to use constructor
    private AlbumSelection(){}
    public static AlbumSelection getSharedInstance(){

        return instance;
    }

    public static ArrayList<String> selectedImageURIs = new ArrayList<String>();



}
