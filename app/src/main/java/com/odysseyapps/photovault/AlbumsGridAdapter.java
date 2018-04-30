package com.odysseyapps.photovault;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by OdysseyApps on 4/22/18.
 */

public class AlbumsGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Album> albumList;

    // Constructor
    public AlbumsGridAdapter(Context c, List<Album> albumList) {
        mContext = c;
        this.albumList = albumList;
    }

    public int getCount() {
        return albumList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView thImageView;
        ImageView selImageView;
        TextView title,count,date,time;

        Album album = albumList.get(position);
        View gridItem;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridItem = inflater.inflate(R.layout.album_card,null);

            //imageView = new ImageView(mContext);
            //gridItem.setLayoutParams(new GridView.LayoutParams(MainActivity.dpToPx(90), MainActivity.dpToPx(90)));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            gridItem = (View) convertView;

        }
        thImageView = (ImageView) gridItem
                .findViewById(R.id.thumbnail);
        if(DataPassingSingelton.getInstance().getSelected()) {
            selImageView = (ImageView) gridItem
                    .findViewById(R.id.selection);
            selImageView.setVisibility(View.VISIBLE);
            if(DataPassingSingelton.getInstance().folderSelectionArray[position]==false)
                selImageView.setBackgroundResource(R.drawable.not_selected);
            else{
                selImageView.setBackgroundResource(R.drawable.selected);
            }
        }
        else{
            selImageView = (ImageView) gridItem
                    .findViewById(R.id.selection);
            selImageView.setVisibility(View.INVISIBLE);
        }
        thImageView.setImageResource(album.getThumbnail());

        title = (TextView) gridItem.findViewById(R.id.title);
        count = (TextView) gridItem.findViewById(R.id.count);
        date = (TextView) gridItem.findViewById(R.id.date);
        time = (TextView) gridItem.findViewById(R.id.time);
        title.setText(album.getName());
        count.setText(String.valueOf(album.getNumOfSongs()));
        date.setText(album.getDate());
        time.setText(album.getTime());
        return gridItem;
    }

    // Keep all Images in array
    public int[] mThumbIds = {
            R.drawable.ic_launcher_background,
            R.drawable.screen,
            R.drawable.ic_launcher_background,
            R.drawable.screen,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.screen
            /*BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background)*/
    };
}
