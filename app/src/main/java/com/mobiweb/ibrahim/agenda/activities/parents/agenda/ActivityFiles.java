package com.mobiweb.ibrahim.agenda.activities.parents.agenda;

import android.Manifest;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterFiles;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterPagerFiles;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterImages;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.IFragmentImages;
import com.mobiweb.ibrahim.agenda.Custom.TouchImageView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Files;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonSchedules;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;


import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityFiles extends ActivityBase implements ViewPager.OnPageChangeListener,IFragmentImages {

    private TouchImageView ivImage;
    private DownloadManager downloadManager;
    private ImageView ivDownload,ivBack;
    private ViewPager vpMedia;
    private ArrayList<Files> arrayFiles=new ArrayList<Files>();
    private int currentPosition=0;
    private String imagesfolder;
    private String class_id,class_section_id,class_name;
    private LinearLayout progress;
    private LinearLayout noData;
    private  AdapterPagerFiles adapterFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ivImage = (TouchImageView) findViewById(R.id.ivImage);
        ivDownload=(ImageView)findViewById(R.id.ivDownload);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        noData=(LinearLayout)findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{pauseAll();}catch (Exception e){}
                ActivityFiles.super.onBackPressed();

            }
        });
        progress=(LinearLayout)findViewById(R.id.progress);

        imagesfolder = getIntent().getStringExtra(AppConstants.IMAGES_FOLDER);
        arrayFiles.clear();
        arrayFiles.addAll(AppHelper.getHwFiles());
        vpMedia = (ViewPager) findViewById(R.id.vpMedia);
        adapterFiles = new AdapterPagerFiles(arrayFiles, this, imagesfolder,false,getIntent().getLongExtra(AppConstants.SEEK_POSITION,-1L));
        vpMedia.setAdapter(adapterFiles);
        vpMedia.setOffscreenPageLimit(arrayFiles.size()-1);
        //    vpMedia.addOnPageChangeListener(this);
        vpMedia.setCurrentItem(getIntent().getIntExtra(AppConstants.IMAGE_POSITION, 0));
        ivDownload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(arrayFiles.get(currentPosition).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                    imagesfolder="uploads/videos";
                }else {
                    imagesfolder="uploads/images";
                }
                startDownload(RetrofitClient.BASE_URL + imagesfolder + "/" + arrayFiles.get(currentPosition).getFileName());
            }
        });

        vpMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                adapterFiles.currentPosition=0l;
                currentPosition=position;
                Log.wtf("page_scrolled",position+"");
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition=position;
                try{pauseAll();}catch (Exception e){}
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }






    @Override
    public void onResume() {
        super.onResume();
        downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
        this.registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        this.registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }



    @Override
    protected void onStop() {
        try{
            pauseAll();
        }catch (Exception e){
            Log.wtf("exception_pause",e.toString());
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        try{
            pauseAll();
        }catch (Exception e){
            Log.wtf("exception_stop",e.toString());
        }
        super.onPause();
        this.unregisterReceiver(onComplete);
        this.unregisterReceiver(onNotificationClick);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == AppConstants.PERMISSION_WRITE && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(arrayFiles.get(currentPosition).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                    imagesfolder="uploads/videos";
                }else {
                    imagesfolder="uploads/images";
                }

                startDownload(RetrofitClient.BASE_URL + imagesfolder + "/" + arrayFiles.get(currentPosition).getFileName());
            }
            else
                Toast.makeText(getApplication(),"download failed you should allow permission to storage", Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startDownload(String url) {
        Log.wtf("download_url",url);
        String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!AppHelper.hasPermissions(this, permissions))
            requestPermissions(permissions, AppConstants.PERMISSION_WRITE);
        else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
            String[] urlSplit = url.trim().split("/");
            Toast.makeText(getApplication(), "Downloading...", Toast.LENGTH_LONG).show();
            //lastDownload =
            downloadManager.enqueue(new DownloadManager.Request(Uri.parse(url))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                            DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(urlSplit[urlSplit.length - 1])
                    //.setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                            urlSplit[urlSplit.length - 1]));
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "DownloadComplete", Toast.LENGTH_LONG).show();
        }
    };

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Downloading...", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        adapterFiles.currentPosition=0l;
        currentPosition=position;
        Log.wtf("page_scrolled2",position+"");
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition=position;
        try{pauseAll();}catch (Exception e){}
        Log.wtf("page_selected2",position+"");

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageClicked(View v) {

    }

    @Override
    public void onBackPressed() {
        try{pauseAll();}catch (Exception e){}
        super.onBackPressed();
    }

    private void pauseAll(){
  /*      for(int i=0;i<arrayFiles.size();i++){
            if(arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                ((SimpleExoPlayerView) vpMedia.getChildAt(i).findViewById(R.id.epView)).getPlayer().setPlayWhenReady(false);
                ((SimpleExoPlayerView) vpMedia.getChildAt(i).findViewById(R.id.epView)).getPlayer().getPlaybackState();
            }
        }*/



        try {
            final int currentItem = vpMedia.getCurrentItem();
            for (int i = 0; i < vpMedia.getChildCount(); i++) {
                final View child = vpMedia.getChildAt(i);
                final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();

                Field f = layoutParams.getClass().getDeclaredField("position"); //NoSuchFieldException
                f.setAccessible(true);
                int position = (Integer) f.get(layoutParams); //IllegalAccessException

                if (!layoutParams.isDecor && currentItem == position) {
                    // return child;
                }
                try {
                    ((SimpleExoPlayerView) vpMedia.getChildAt(i).findViewById(R.id.epView)).getPlayer().setPlayWhenReady(false);
                    ((SimpleExoPlayerView) vpMedia.getChildAt(i).findViewById(R.id.epView)).getPlayer().getPlaybackState();
                }catch (Exception e){}
            }
        } catch (NoSuchFieldException e) {
            Log.e("exception", e.toString());
        } catch (IllegalArgumentException e) {
            Log.e("exception", e.toString());
        } catch (IllegalAccessException e) {
            Log.e("exception", e.toString());
        }

    }



}
