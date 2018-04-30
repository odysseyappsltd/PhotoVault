package com.odysseyapps.photovault;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by OdysseyApps on 4/18/18.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    private Boolean isSelected;
    selectedFiles selectFile;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, date, time;
        public ImageView thumbnail, selection;//, overflow;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
            cardView = (CardView) view.findViewById(R.id.card_view);
            selection = (ImageView) view.findViewById(R.id.selection);
            System.out.println("asdf");
            if(isSelected){
                selection.setVisibility(View.VISIBLE);
            }
            else{
                selection.setVisibility(View.INVISIBLE);
            }
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList,Boolean isSelected, selectedFiles selectFile) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.isSelected = isSelected;
        this.selectFile = selectFile;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(String.valueOf(album.getNumOfSongs()));
        holder.date.setText(album.getDate());
        holder.time.setText(album.getTime());
        holder.thumbnail.setImageResource(album.getThumbnail());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isSelected){
                    Drawable dr = holder.selection.getBackground();
                    Drawable dr1 = mContext.getResources().getDrawable(R.drawable.selected);
                    Bitmap bitmap = ((BitmapDrawable)dr).getBitmap();
                    Bitmap bitmap1 = ((BitmapDrawable)dr1).getBitmap();
                    if(bitmap==bitmap1)
                        holder.selection.setBackgroundResource(R.drawable.not_selected);
                    else {
                        selectFile.Fileselection(position);
                        holder.selection.setBackgroundResource(R.drawable.selected);
                    }
                }
                else {
                    Intent i = new Intent(mContext, EachFolder.class);
                    //i.putExtra("Squad",mData.get(position).getTitle());
                    mContext.startActivity(i);
                }
            }
        });

        /*holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
