package com.mobiweb.ibrahim.agenda.activities.teachers;


import android.Manifest;

import com.mobiweb.ibrahim.agenda.Agenda;
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
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonSchedules;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityImageTeacher extends ActivityBase implements ViewPager.OnPageChangeListener,IFragmentImages {

    private TouchImageView ivImage;
    private DownloadManager downloadManager;
    private ImageView ivDownload,ivBack;
    private ViewPager vpMedia;
    private ArrayList<String> imageUrl=new ArrayList<String>();
    private int currentPosition=0;
    private String imagesfolder;
    private LinearLayout progress;
    private LinearLayout noData;

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
              ActivityImageTeacher.super.onBackPressed();
            }
        });
        progress=(LinearLayout)findViewById(R.id.progress);

            imagesfolder = "schedules";
            ArrayList<String> arraySchedule=new ArrayList<>();
            arraySchedule.add(((Agenda)getApplication()).getCashedUser().getSchedule_teacher());
            imageUrl = arraySchedule;
            vpMedia = (ViewPager) findViewById(R.id.vpMedia);
            AdapterImages adapterImages = new AdapterImages(imageUrl, this, imagesfolder);
            vpMedia.setAdapter(adapterImages);
            vpMedia.addOnPageChangeListener(this);
            vpMedia.setCurrentItem(getIntent().getIntExtra(AppConstants.IMAGE_POSITION, 0));
            ivDownload.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    startDownload(RetrofitClient.BASE_URL + imagesfolder + "/" + imageUrl.get(currentPosition));
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
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(onComplete);
        this.unregisterReceiver(onNotificationClick);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == AppConstants.PERMISSION_WRITE && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startDownload(RetrofitClient.BASE_URL+imagesfolder+"/"+imageUrl);
            else
                Toast.makeText(getApplication(),"download failed you should allow permission to storage", Toast.LENGTH_LONG).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startDownload(String url) {
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

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition=position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageClicked(View v) {

    }
}
