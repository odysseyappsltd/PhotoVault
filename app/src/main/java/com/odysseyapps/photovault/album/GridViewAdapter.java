package com.odysseyapps.photovault.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.odysseyapps.photovault.R;

import java.util.ArrayList;

/**
 * Created by macbookpro on 3/6/18.
 */

public class GridViewAdapter extends ArrayAdapter<Model_images> {


    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();
    int int_position;
    private String selectedPath ;
    private String a;



    public GridViewAdapter(Context context, ArrayList<Model_images> al_menu, int int_position) {
        super(context, R.layout.album_grid_view_adapter, al_menu);
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;



    }


    @Override
    public int getCount() {


        if(int_position==0){
            return  AlbumActivity.allImage.size();
        }
        else {
            return al_menu.get(int_position-1).getAl_imagepath().size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if(int_position==0){
            if(AlbumActivity.allImage.size()>0) {
                return AlbumActivity.allImage.size();
            }
            else {
                return 1;
            }
        }
        else {
            if (al_menu.get(int_position-1).getAl_imagepath().size() > 0) {
                return al_menu.get(int_position-1).getAl_imagepath().size();
            } else {
                return 1;
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectedPath(String path) {
        selectedPath = path;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_grid_view_adapter, parent, false);

            viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            viewHolder.imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        if(int_position==0){
            a=AlbumActivity.allImage.get(position);

        }
        else{
            a=al_menu.get(int_position-1).getAl_imagepath().get(position);

        }

        Glide.with(context).load("file://" + a)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView2);

        /*
        if (AlbumActivity.selection.contains(a)) {
            viewHolder.imageView3.setImageResource(R.drawable.tick);
        }
        else{
            viewHolder.imageView3.setImageDrawable(null);
        }*/

        return convertView;



    }




    private static class ViewHolder {
        ImageView imageView2;
        ImageView imageView3;


    }

}
