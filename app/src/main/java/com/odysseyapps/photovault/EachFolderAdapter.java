package com.odysseyapps.photovault;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by OdysseyApps on 4/22/18.
 */

public class EachFolderAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public EachFolderAdapter(Context c, ArrayList<Bitmap> array) {
        mContext = c;
        mThumbIds = array;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(MainActivity.dpToPx(90), MainActivity.dpToPx(90)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(mThumbIds.get(position));
        return imageView;
    }

    // Keep all Images in array
    /*public int[] mThumbIds = {
            R.drawable.ic_launcher_background,
            R.drawable.screen,
            R.drawable.ic_launcher_background,
            R.drawable.screen,
            R.drawable.ic_launcher_background,
            R.drawable.screen

    };*/
    ArrayList<Bitmap> mThumbIds = new ArrayList<Bitmap>();

}
