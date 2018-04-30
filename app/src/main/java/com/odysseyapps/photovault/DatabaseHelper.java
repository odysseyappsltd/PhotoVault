package com.odysseyapps.photovault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OdysseyApps on 2/15/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PhotoVault.db";

    // Table Names
    private static final String DB_TABLE = "Album";
    private static final String DB_TABLE_MEDIA = "media";

    // column names
    private static final String ID = "folder_no";
    private static final String Folder_Date = "folder_date";
    private static final String Folder_Time = "folder_time";
    private static final String Folder_Name = "folder_name";


    private static final String Image_Data = "image_data";
    private static final String Image_Id = "image_id";

    // Table create statement
    private static final String CREATE_TABLE_ALBUM = "CREATE TABLE " + DB_TABLE + "("+
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Folder_Date + " TEXT," +
            Folder_Time + " TEXT," +
            Folder_Name + " TEXT);";
    private static final String CREATE_TABLE_MEDIA = "CREATE TABLE " + DB_TABLE_MEDIA + "("+
            Image_Id + " TEXT," +
            Image_Data + " BLOB);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating table
        db.execSQL(CREATE_TABLE_ALBUM);
        db.execSQL(CREATE_TABLE_MEDIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_MEDIA);

        // create new table
        onCreate(db);
    }
    public void addAlbum(String folder_date, String folder_time, String folder_name) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(ID,    String.valueOf(id));
        cv.put(Folder_Date,    folder_date);
        cv.put(Folder_Name,    folder_name);
        cv.put(Folder_Time,    folder_time);
        database.insert( DB_TABLE, null, cv );
    }
    public void deleteAlbum(Integer id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //int Id = getSpecId(id);
        database.delete(DB_TABLE, ID + "=" + String.valueOf(id), null);
    }
    public void renameAlbum(Integer id, String name)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        //int Id = getSpecId(id);
        String sql="update "+DB_TABLE+" set "+Folder_Name+" = '"+name+"' where "+ID+" = "+id;
        //Object[] bindArgs={"bar"};
        try{
            database.execSQL(sql);

        }catch(SQLException ex){

        }
    }
    public void addImageAlbum(String id, List<Bitmap> image){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i=0;i<4;i++) {
            cv.put(Image_Id, id);
            Bitmap bitmap = image.get(i);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            cv.put(Image_Data, stream.toByteArray());
            database.insert(DB_TABLE_MEDIA, null, cv);
        }
    }
    public ArrayList<Bitmap> getImageAlbum(String id) {
        ArrayList<Bitmap> array_list = new ArrayList<Bitmap>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DB_TABLE_MEDIA+" where "+Image_Id+" = "+id, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            byte image[] = res.getBlob(res.getColumnIndex(Image_Data));
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            array_list.add(bmp);
            res.moveToNext();
        }
        return array_list;
    }
    /*public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DB_TABLE);
        return numRows;
    }*/
    public ArrayList<AlbumDataBase> getAllAlbumData() {
        ArrayList<AlbumDataBase> array_list = new ArrayList<AlbumDataBase>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DB_TABLE, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String folder_name = res.getString(res.getColumnIndex(Folder_Name));
            String folder_time = res.getString(res.getColumnIndex(Folder_Time));
            String folder_date = res.getString(res.getColumnIndex(Folder_Date));
            Integer id = res.getInt(res.getColumnIndex(ID));
            array_list.add(new AlbumDataBase(folder_name,folder_date,folder_time,id));
            res.moveToNext();
        }
        return array_list;
    }
    /*public int getSpecId(Integer id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DB_TABLE, null );
        res.moveToFirst();
        int Id=0;
        int i = 1;
        while(res.isAfterLast() == false){
            if(id==i)
            {
                Id = res.getInt(res.getColumnIndex(ID));
                break;
            }
            res.moveToNext();
            i++;
        }
        return Id;
    }*/
}
