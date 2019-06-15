package com.mobiweb.ibrahim.agenda.activities.parents.agenda;

import android.Manifest;
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


public class ActivityImage extends ActivityBase implements ViewPager.OnPageChangeListener,IFragmentImages {

    private TouchImageView ivImage;
    private DownloadManager downloadManager;
    private ImageView ivDownload,ivBack;
    private ViewPager vpMedia;
    private ArrayList<String> imageUrl=new ArrayList<String>();
    private int currentPosition=0;
    private String imagesfolder;
    private String class_id,class_section_id,class_name;
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
                ActivityImage.super.onBackPressed();
            }
        });
        progress=(LinearLayout)findViewById(R.id.progress);
        if(!getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_PARENTS)) {
            imagesfolder = getIntent().getStringExtra(AppConstants.IMAGES_FOLDER);
            imageUrl = getIntent().getStringArrayListExtra(AppConstants.HW_IMAGE);
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
        }else {
            class_id=getIntent().getStringExtra(AppConstants.ClASS_ID);
            class_section_id=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
            class_name=getIntent().getStringExtra(AppConstants.CLASS_NAME);

            getSchedule();
        }




    }



    private void getSchedule() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getSchedule(new JsonParameters(class_id,class_section_id,1));
        call1.enqueue(new Callback<JsonSchedules>() {
            @Override
            public void onResponse(Call<JsonSchedules> call, Response<JsonSchedules> response) {

                try {
                    onScheduleRetrieved(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonSchedules> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onScheduleRetrieved(JsonSchedules result){
        noData.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        if(!result.getSchedules().get(0).getSchedule_url().isEmpty()){

            imagesfolder ="schedules";
            imageUrl.add(result.getSchedules().get(0).getSchedule_url());
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

        }else {
            noData.setVisibility(View.VISIBLE);
        }
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
