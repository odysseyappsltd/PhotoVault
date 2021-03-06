package com.odysseyapps.photovault;

import android.graphics.Bitmap;

/**
 * Created by OdysseyApps on 2/12/18.
 */

public class DataPassingSingelton {
    private Bitmap Image;
    private Bitmap Image2;
    private Bitmap BGImage;
    private Bitmap[] AllImageArray;
    boolean[] folderSelectionArray = new boolean[100000];
    private Boolean isSelected=false;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Bitmap[] getAllImageArray() {
        return AllImageArray;
    }

    public void setAllImageArray(Bitmap[] allImageArray) {
        AllImageArray = allImageArray;
    }

    private static final DataPassingSingelton ourInstance = new DataPassingSingelton();

    public static DataPassingSingelton getInstance() {
        return ourInstance;
    }

    private DataPassingSingelton() {
    }
    public Bitmap getImage() {
        return this.Image;
    }

    public void setImage(Bitmap ImageIn) {
        this.Image = ImageIn;
    }
    public Bitmap getImage2() {
        return this.Image2;
    }

    public void setImage2(Bitmap ImageIn) {
        this.Image2 = ImageIn;
    }

    public Bitmap getBGImage() {
        return this.BGImage;
    }

    public void setBGImage(Bitmap ImageIn) {
        this.BGImage = ImageIn;
    }

}
