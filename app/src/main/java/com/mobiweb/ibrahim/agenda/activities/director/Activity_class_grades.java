package com.mobiweb.ibrahim.agenda.activities.director;


/**
 * Created by ibrahim on 2/16/2018.
 */

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;


import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.JavaScriptInterface;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;


public class Activity_class_grades extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private WebView wvClassGrades;
    private String web_url,web_userAgent,web_contentDisposition,web_mimeType;
    private long web_contentLength;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_grades);
        init();
    }
    private void init(){
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        wvClassGrades=(WebView)findViewById(R.id.wvClassGrades);

        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Activity_class_grades.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);

        //  toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));



        wvClassGrades.getSettings().setLoadWithOverviewMode(true);
        wvClassGrades.getSettings().setUseWideViewPort(true);
        wvClassGrades.getSettings().setBuiltInZoomControls(true);
        wvClassGrades.getSettings().setJavaScriptEnabled(true);
        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)) {
            toolbarTitle.setText(getString(R.string.grades));
            toolbarTitleAr.setText(getString(R.string.grades));
            wvClassGrades.loadUrl(RetrofitClient.getClient().baseUrl() + "show_student_results.php?id_student="+AppHelper.getStudentId()+"&id_class="+AppHelper.getId_class()+"&id_section="+AppHelper.getId_section());
            Log.wtf("url",RetrofitClient.getClient().baseUrl() + "show_student_results.php?id_student="+AppHelper.getStudentId()+"&id_class="+AppHelper.getId_class()+"&id_section="+AppHelper.getId_section());
        }
          else  if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))  {
            toolbarTitle.setText(getString(R.string.class_grades));
            toolbarTitleAr.setText(getString(R.string.class_grades_ar));
            wvClassGrades.loadUrl(RetrofitClient.getClient().baseUrl() + "show_all_student_results.php?id_exam_category=" + AppHelper.getId_category_exam() + "&id_class=" + AppHelper.getId_class() + "&id_section=" + AppHelper.getId_section()+"&admin=1");
           Log.wtf("url",RetrofitClient.getClient().baseUrl() + "show_all_student_results.php?id_exam_category=" + AppHelper.getId_category_exam() + "&id_class=" + AppHelper.getId_class() + "&id_section=" + AppHelper.getId_section()+"&admin=1");
        } else  if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER))  {
            toolbarTitle.setText(getString(R.string.class_grades));
            toolbarTitleAr.setText(getString(R.string.class_grades_ar));
            wvClassGrades.loadUrl(RetrofitClient.getClient().baseUrl() + "show_all_student_results.php?id_exam_category=" + AppHelper.getId_category_exam() + "&id_class=" + AppHelper.getId_class() + "&id_section=" + AppHelper.getId_section()+"&admin=0");
            Log.wtf("url",RetrofitClient.getClient().baseUrl() + "show_all_student_results.php?id_exam_category=" + AppHelper.getId_category_exam() + "&id_class=" + AppHelper.getId_class() + "&id_section=" + AppHelper.getId_section()+"&admin=0");
        }


        wvClassGrades.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                web_url=url;
                web_userAgent=userAgent;
                web_contentDisposition=contentDisposition;
                web_mimeType=mimeType;
                web_contentLength=contentLength;


                String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!AppHelper.hasPermissions(Activity_class_grades.this, permissions))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permissions, AppConstants.PERMISSION_WRITE);
                    }else
                        startDownload();
                else {
                    startDownload();

                }
            }
        });



    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == AppConstants.PERMISSION_WRITE && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startDownload();
            else
                Toast.makeText(getApplication(),"download failed you should allow permission to storage", Toast.LENGTH_LONG).show();
        }
    }

    private void startDownload(){
        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
        if (web_url.startsWith("data:")) {  //when url is base64 encoded data
            String path = createAndSaveFileFromBase64Url(web_url);
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(web_url));
        request.setMimeType(web_mimeType);
        String cookies = CookieManager.getInstance().getCookie(web_url);
        request.addRequestHeader("cookie", cookies);
        request.addRequestHeader("User-Agent", web_userAgent);
        request.setDescription("download complete");
        String filename = URLUtil.guessFileName(web_url, web_contentDisposition, web_mimeType);
        request.setTitle(filename);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);



    }



    public String createAndSaveFileFromBase64Url(String url) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filetype = url.substring(url.indexOf("/") + 1, url.indexOf(";"));
        String filename = System.currentTimeMillis() + "." + filetype;
        File file = new File(path, filename+".xls");
        try {
            if(!path.exists())
                path.mkdirs();
            if(!file.exists())
                file.createNewFile();

            String base64EncodedString = url.substring(url.indexOf(",") + 1);
            byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
            OutputStream os = new FileOutputStream(file);
            os.write(decodedBytes);
            os.close();

            //Tell the media scanner about the new file so that it is immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            //Set notification after download complete and add "click to view" action to that
            String mimetype = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            //intent.setDataAndType(Uri.fromFile(file), (mimetype + "/*"));
            intent.setDataAndType(FileProvider.getUriForFile(this, "com.mobiweb.ibrahim.fileProvider", file), (/*mimetype + */"*/*"));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);



            notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager);
                notificationBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id));
            } else
                notificationBuilder = new NotificationCompat.Builder(this);




           // Notification notification = new NotificationCompat.Builder(this)
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Download Complete")
                    .setContentTitle(filename)
                    .setContentIntent(pIntent)
                    .build();




           // notificationBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
            int notificationId = 85851;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
            Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_LONG).show();
        }

        return file.toString();
    }



    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String id=getResources().getString(R.string.default_notification_channel_id);
        String name = "Downloading report";
        String description = "grades report";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(getResources().getColor(R.color.colorPrimary));
        notificationManager.createNotificationChannel(mChannel);
    }



    @Override
    public void onBackPressed() {

      super.onBackPressed();
    }
}
