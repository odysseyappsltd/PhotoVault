package com.odysseyapps.photovault.photofinder;

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

     public  ArrayList<String> selectedImageURIs = new ArrayList<String>();
     public  ArrayList<String> selectedVideoURIs = new ArrayList<String>();



}
