package com.odysseyapps.photovault;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.odysseyapps.photovault.Admobs.Advertisement;
import com.odysseyapps.photovault.IAP.IAPData;
import com.odysseyapps.photovault.Settings.SettingsActivity;
import com.odysseyapps.photovault.StaticClasses.CheckIf;

import java.io.ByteArrayOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements selectedFiles{

    //private RecyclerView recyclerView;
    private GridView gridview;
    //private AlbumsAdapter adapter;
    private AlbumsGridAdapter adapter;
    private List<Album> albumList;
    private DatabaseHelper mydb ;
    private Boolean isSelected=false;
    private Button renameBtn,deleteBtn,moreBtn,addBtn,settingBtn;
    List<Bitmap> imgList = new ArrayList<Bitmap>();
    SharedPreferences preferences;
    private byte[][] data;  // This is idiomatic Java

    final int[] covers = new int[]{
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_foreground};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        renameBtn = (Button) findViewById(R.id.renameBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        settingBtn = (Button) findViewById(R.id.settingBtn);
        moreBtn = (Button) findViewById(R.id.moreBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        preferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        albumList = new ArrayList<>();
        //adapter = new AlbumsAdapter(this, albumList, isSelected, this);
        adapter = new AlbumsGridAdapter(this,albumList);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, getColoumns());
        /*recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(getColoumns(), dpToPx(5), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/
        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(DataPassingSingelton.getInstance().getSelected()){
                    ImageView imageView = (ImageView) view.findViewById(R.id.selection);
                    Drawable dr = imageView.getBackground();
                    Drawable dr1 = getResources().getDrawable(R.drawable.selected);
                    Bitmap bitmap = ((BitmapDrawable)dr).getBitmap();
                    Bitmap bitmap1 = ((BitmapDrawable)dr1).getBitmap();
                    if(bitmap==bitmap1) {
                        imageView.setBackgroundResource(R.drawable.not_selected);
                        DataPassingSingelton.getInstance().folderSelectionArray[i]=false;
                    }
                    else {
                        imageView.setBackgroundResource(R.drawable.selected);
                        DataPassingSingelton.getInstance().folderSelectionArray[i]=true;
                    }
                    numberOffolderSelected();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, EachFolder.class);
                    startActivity(intent);
                }

            }
        });

        mydb = new DatabaseHelper(this);
        restoreAlbumData();


        // Admob

        MobileAds.initialize(this, Advertisement.getSharedInstance().getNativeAdvanceAdAppID());
        final AdLoader adLoader = new AdLoader.Builder(this, Advertisement.getSharedInstance().getNativeAdvanceAdUnitID())
                .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
                        // Show the app install ad.
                        //Toast.makeText(MainActivity.this, "Ad App Install loading", Toast.LENGTH_SHORT).show();;
                        FrameLayout frameLayout  = (FrameLayout)findViewById(R.id.AMAdmob);
                        frameLayout.setVisibility(View.VISIBLE);
                        NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
                                .inflate(R.layout.ad_app_install, null);
                        Advertisement.getSharedInstance().populateAppInstallAdView(appInstallAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd contentAd) {

                        // Show the content ad.
                        //Toast.makeText(MainActivity.this, "Ad Content loading", Toast.LENGTH_SHORT).show();
                        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.AMAdmob);
                        frameLayout.setVisibility(View.VISIBLE);
                        NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
                                .inflate(R.layout.ad_content, null);
                        Advertisement.getSharedInstance().populateContentAdView(contentAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();



                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        //Toast.makeText(MainActivity.this, "Failed Ad loading", Toast.LENGTH_SHORT).show();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();

        //Admob Visibility
        //findViewById(R.id.AMAdmob).setVisibility(View.GONE);
        if (!CheckIf.isPurchased(IAPData.getSharedInstance().ADMOB,this)){
            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            findViewById(R.id.AMAdmob).setVisibility(View.GONE);
        }


    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if(message.equals("gallery")){
                //System.out.println("gallery");
                galleryAct();
            }
            else if(message.equals("capturePhoto")){
                System.out.println("capturePhoto");
            }
            else if(message.equals("captureVideo")){
                System.out.println("captureVideo");

            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Arrays.fill(DataPassingSingelton.getInstance().folderSelectionArray,false);
        DataPassingSingelton.getInstance().setSelected(false);
    }

    private void restoreAlbumData(){
        ArrayList<AlbumDataBase> album = mydb.getAllAlbumData();
        for(int i=0;i<album.size();i++){
            Album a = new Album(album.get(i).folder_name, 15, covers[0],album.get(i).folder_time,album.get(i).folder_date,album.get(i).ID);
            albumList.add(a);
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteAlbum(Integer id){
        mydb.deleteAlbum(albumList.get(id-1).getId());
        albumList.remove(id-1);
        DataPassingSingelton.getInstance().folderSelectionArray[id-1]=false;

    }
    public void galleryAct(){


        /*for (int i=1;i<=4;i++)
        {
            imgList.add(BitmapFactory.decodeResource(getResources(), R.drawable.screen));
        }
        addImageAlbum("0");*/
    }
    public void addImageAlbum(String id){

        mydb.addImageAlbum(id, imgList);
    }

    private void addAlbum(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Album Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText("Untitled");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Add Album Name", Toast.LENGTH_SHORT).show();
                    addAlbum();
                }
                else {
                    int id = preferences.getInt("AlbumID", 1);
                    Album a = new Album(input.getText().toString(), 15, covers[0], "10:15", "10-8-18", id);
                    albumList.add(a);
                    mydb.addAlbum(a.getDate(), a.getTime(), a.getName());

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("AlbumID", id++);
                    editor.commit();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            }
        });
        builder.show();



    }

    public void numberOffolderSelected(){
        int count=0;
        for(int i=0;i<albumList.size();i++){
            if(DataPassingSingelton.getInstance().folderSelectionArray[i]==true)
                count++;
        }
        if(count==1)
            renameBtn.setEnabled(true);
        else
            renameBtn.setEnabled(false);

        if(count>=1)
            deleteBtn.setEnabled(true);
        else
            deleteBtn.setEnabled(false);
    }
    public void renameAlbum(Integer id){
        final Album al = albumList.get(id-1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Album Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(al.getName());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Add Album Name", Toast.LENGTH_SHORT).show();
                    addAlbum();
                }
                else {

                    //Album a = new Album(input.getText().toString(), 15, covers[0], "10:15", "10-8-18", id);
                    al.setName(input.getText().toString());
                    mydb.renameAlbum(al.getId(),al.getName());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            }
        });
        builder.show();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    @Override
    public void Fileselection(Integer indx) {
        System.out.println(indx);
    }


    public void addButtonAct(View view){
        //addAlbum();
        Intent popUp = new Intent(this,PopUpViewActivity.class);
        startActivity(popUp);
        //galleryAct();
    }
    public void selectButtonAct(View view){
        //deleteAlbum(2);
        if(DataPassingSingelton.getInstance().getSelected())
        {
            renameBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            addBtn.setVisibility(View.VISIBLE);
            moreBtn.setVisibility(View.VISIBLE);
            settingBtn.setVisibility(View.VISIBLE);
            isSelected=false;
            DataPassingSingelton.getInstance().setSelected(false);
            Arrays.fill(DataPassingSingelton.getInstance().folderSelectionArray,false);
        }
        else
        {
            renameBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.INVISIBLE);
            moreBtn.setVisibility(View.INVISIBLE);
            settingBtn.setVisibility(View.INVISIBLE);
            isSelected=true;
            DataPassingSingelton.getInstance().setSelected(true);
        }

        //adapter = new AlbumsAdapter(this, albumList, isSelected, this);
        //recyclerView.setAdapter(adapter);

        gridview.invalidateViews();

    }
    public void settingsBtnAct(View view){
        Intent setting = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(setting);

    }
    public void renameBtnAct(View view){

        int length = albumList.size();
        for(int i=length;i>=0;i--){
            if(DataPassingSingelton.getInstance().folderSelectionArray[i]==true){
                renameAlbum(i+1);
                break;
            }
        }
        //adapter.notifyDataSetChanged();
    }
    public void deleteBtnAct(View view){

        int length = albumList.size();
        for(int i=length;i>=0;i--){
            if(DataPassingSingelton.getInstance().folderSelectionArray[i]==true){
                deleteAlbum(i+1);
            }
        }
        adapter.notifyDataSetChanged();
        numberOffolderSelected();
    }
    private int getColoumns(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return (int)(pxToDp(width)/100);
    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
