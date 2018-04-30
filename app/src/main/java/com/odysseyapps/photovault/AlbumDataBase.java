package com.odysseyapps.photovault;

/**
 * Created by OdysseyApps on 4/23/18.
 */

public class AlbumDataBase {
    public String folder_name;
    public String folder_date;
    public String folder_time;
    public Integer ID;

    public AlbumDataBase(String folder_name, String folder_date, String folder_time,Integer id) {
        this.folder_name = folder_name;
        this.folder_date = folder_date;
        this.folder_time = folder_time;
        this.ID = id;
    }
}
