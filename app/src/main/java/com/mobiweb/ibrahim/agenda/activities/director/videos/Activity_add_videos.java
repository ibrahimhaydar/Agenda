package com.mobiweb.ibrahim.agenda.activities.director.videos;

import android.Manifest;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.PermissionsChecker;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_add_videos extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight,ivPickVideo,ivPickThumb;
    private LinearLayout progress;
    private EditText edHwTitle,etHwDesc;
    private Button btAdd;
    private ImageView ivClose,ivClosethumb,pickedVideo,pickedThumb;
    private boolean isVideoPicked=false;
    private boolean isThumbPicked=false;
    private  File myFile;

    private Activity activity;
    private CustomTextView ctvDialogMessage;
    private CustomTextViewBold ctvTitleLabel,ctvDescLabel;
    PermissionsChecker checker;
    private MultipartBody.Part body,body2;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


    private Dialog responseDialog;
    private Button btOk;
    private RelativeLayout rlVideo,rlthumb;
    private String videoPath="",thumbPath="";
    private LinearLayout linearPickVideo,linearDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activites);
        init();
        toolbarTitle.setText(getString(R.string.add_video));
        toolbarTitleAr.setText(getString(R.string.add_video_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_add_videos.super.onBackPressed();
            }
        });

    }


    private void init() {

        linearDate=(LinearLayout)findViewById(R.id.linearDate);
        linearDate.setVisibility(View.GONE);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        rlVideo=(RelativeLayout)findViewById(R.id.rlVideo);
        rlthumb=(RelativeLayout)findViewById(R.id.rlthumb);
        ivClose=(ImageView)findViewById(R.id.ivClose);
        ivClosethumb=(ImageView)findViewById(R.id.ivClosethumb);
        pickedVideo=(ImageView)findViewById(R.id.pickedVideo);
        pickedThumb=(ImageView)findViewById(R.id.pickedImage);

        linearPickVideo=(LinearLayout)findViewById(R.id.linearPickVideo);
        linearPickVideo.setVisibility(View.VISIBLE);

        checker = new PermissionsChecker(this);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_add_videos.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        edHwTitle=(EditText)findViewById(R.id.etTitle);
        ivPickVideo=(ImageView)findViewById(R.id.ivPickVideo);
        ivPickThumb=(ImageView)findViewById(R.id.ivPickThumb);

      //  edHwTitle.setText(AppHelper.getCourseName());
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);
        ctvTitleLabel=(CustomTextViewBold)findViewById(R.id.ctvTitleLabel);
        ctvDescLabel=(CustomTextViewBold)findViewById(R.id.ctvDescLabel);

        ctvTitleLabel.setText("Video title");
        ctvDescLabel.setText("Video Description");

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlVideo.setVisibility(View.GONE);
                videoPath="";
                isVideoPicked=false;
            }
        });


        ivClosethumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlthumb.setVisibility(View.GONE);
                thumbPath="";
                isThumbPicked=false;
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edHwTitle.getText().toString().isEmpty() || etHwDesc.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill empty fields",Toast.LENGTH_LONG).show();
                }else if(!isVideoPicked){
                    Toast.makeText(getApplication(),"Please Pick a Video",Toast.LENGTH_LONG).show();
                }else if(!isThumbPicked){
                    Toast.makeText(getApplication(),"Please Pick a Thumbnail",Toast.LENGTH_LONG).show();
                }

                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    addActivity(edHwTitle.getText().toString(),etHwDesc.getText().toString());
                }
            }
        });

        ivPickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view,1);
            }
        });

        ivPickThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view,2);
            }
        });



    }



    private void addActivity(String title,String description) {


        progress.setVisibility(View.VISIBLE);


        try{
            if(!videoPath.matches("")){
               myFile = new File(videoPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body = MultipartBody.Part.createFormData("uploaded_file", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body = MultipartBody.Part.createFormData("uploaded_file", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("uploaded_file", "", requestFile);
        }



        try{
            if(!thumbPath.matches("")){
                myFile = new File(thumbPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body2 = MultipartBody.Part.createFormData("uploaded_file_thumb", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body2 = MultipartBody.Part.createFormData("uploaded_file_thumb", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body2 = MultipartBody.Part.createFormData("uploaded_file_thumb", "", requestFile);
        }


        RequestBody req_title = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody req_desc = RequestBody.create(MediaType.parse("text/plain"), description);

        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addvideo(
                        body,
                        body2,
                        req_title,
                        req_desc
                );
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getResult().equals("success")){
                            popUpMessage(true);

                        }
                        else
                            popUpMessage(false);
                    }catch (Exception e){
                        popUpMessage(false);

                    }

                } else {
                    popUpMessage(false);
                }




            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("Added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    startActivity(new Intent(Activity_add_videos.this, Activity_view_videos.class));
                    //Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplication(),"fail",Toast.LENGTH_LONG).show();
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


    public void showImagePopup(View view,int type) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.


            final Intent galleryIntent = new Intent();

            if(type==1) {
                galleryIntent.setType("video/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                startActivityForResult(chooserIntent, 1010);
            }else {
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                startActivityForResult(chooserIntent, 1020);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data == null) {
                Toast.makeText(getApplication(),"Unable to pick video ",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    videoPath = cursor.getString(columnIndex);
                    Log.wtf("pickedVideo",getMimeType(videoPath));
                    try {
                     if (getMimeType(videoPath).toLowerCase().matches("video/mp4") ||
                             getMimeType(videoPath).toLowerCase().matches("video/m4a") ||
                             getMimeType(videoPath).toLowerCase().matches("video/fmp4") ||
                             getMimeType(videoPath).toLowerCase().matches("video/webm") ||
                             getMimeType(videoPath).toLowerCase().matches("video/mp3") ||
                             getMimeType(videoPath).toLowerCase().matches("video/ogg") ||
                             getMimeType(videoPath).toLowerCase().matches("video/wav") ||
                             getMimeType(videoPath).toLowerCase().matches("video/3gpp") ||
                             getMimeType(videoPath).toLowerCase().matches("video/flv")) {
                         pickedVideo.setImageBitmap(ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                         rlVideo.setVisibility(View.VISIBLE);
                         isVideoPicked = true;
                     } else {
                         Toast.makeText(getApplication(), "picked video is not supported", Toast.LENGTH_LONG).show();
                     }
                 }catch (Exception e){
                        Log.wtf("exception",e);
                    }
                cursor.close();
            } else {
                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 1020) {

            if (data == null) {
                Toast.makeText(getApplication(),"Unable to pick image ",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                thumbPath = cursor.getString(columnIndex);
                Glide.with(this)
                        .load(new File(thumbPath))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                        .into(pickedThumb);
                         rlthumb.setVisibility(View.VISIBLE);
                         isThumbPicked = true;

                cursor.close();
            } else {
                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }


        }
    }

    public static String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;



     /*
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;*/
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }




}
