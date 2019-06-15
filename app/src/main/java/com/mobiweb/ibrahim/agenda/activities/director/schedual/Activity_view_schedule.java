package com.mobiweb.ibrahim.agenda.activities.director.schedual;


import android.Manifest;

import com.mobiweb.ibrahim.agenda.Custom.TouchImageView;
import com.mobiweb.ibrahim.agenda.Custom.TouchImageViewDialog;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_login;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonSchedules;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
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

public class Activity_view_schedule extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight,ivPickSchedule;
    private LinearLayout progressAdd;
    private Button btAdd;
    private ImageView pickedSchedule;
    private boolean ischedulePicked=false;
    private  File myFile;
    private Activity activity;
    PermissionsChecker checker;
    private MultipartBody.Part body;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private String schedulePath="";
    private LinearLayout linearPickSchedule;
    private String class_id,class_section_id,class_name;
    private boolean isDelete=false;

    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;
    private LinearLayout progress;
    private ImageView ivClose;


    private String intent_title,intent_desc,intent_url,intent_thumb,intent_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        init();
        getVideoIntent();
       // toolbarTitle.setText(getString(R.string.schedual));
        toolbarTitleAr.setText(getString(R.string.schedualAr));
        toolbarTitleAr.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_view_schedule.super.onBackPressed();
            }
        });
        getSchedule();


    }


    private void init() {

        ivClose=(ImageView)findViewById(R.id.ivClose);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        pickedSchedule=(ImageView)findViewById(R.id.pickedImage);
        linearPickSchedule=(LinearLayout)findViewById(R.id.linearPickSchedule);
        checker = new PermissionsChecker(this);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progressAdd=(LinearLayout)findViewById(R.id.progressAdd);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_view_schedule.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivPickSchedule=(ImageView)findViewById(R.id.ivPickSchedule);
        btAdd=(Button)findViewById(R.id.btAdd);



        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ischedulePicked) {
                    isDelete=false;
                    addSchedule();
                }
                else
                    Toast.makeText(getApplication(),"Please choose Image",Toast.LENGTH_LONG).show();
               
            }
        });



        ivPickSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view);
            }
        });



    }
    private void getVideoIntent(){

        class_id=getIntent().getStringExtra(AppConstants.ClASS_ID);
        class_section_id=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        class_name=getIntent().getStringExtra(AppConstants.CLASS_NAME);

     //   Toast.makeText(getApplication(),class_id+" aa "+class_section_id+ " aa "+class_name,Toast.LENGTH_LONG).show();
        toolbarTitle.setText(class_name);
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
                }
            }

            @Override
            public void onFailure(Call<JsonSchedules> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onScheduleRetrieved(final JsonSchedules result){
        progress.setVisibility(View.GONE);
     if(!result.getSchedules().get(0).getSchedule_url().isEmpty()){
         AppHelper.setImage(this,pickedSchedule,RetrofitClient.BASE_URL+"schedules/"+result.getSchedules().get(0).getSchedule_url());

         pickedSchedule.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openImage(RetrofitClient.BASE_URL+"schedules/"+result.getSchedules().get(0).getSchedule_url(),false);
             }
         });

         ivClose.setVisibility(View.VISIBLE);
         ivClose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 isDelete=true;                                                                                                                                                                                                                             
                 schedulePath="";
                 addSchedule();

             }
         });
     }
    }


    private void openImage(String path,boolean isFile){

       // Dialog imageDialog = new Dialog(this);

        final Dialog imageDialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imageDialog.setContentView(R.layout.popup_image);
        TouchImageViewDialog ivPopupFull=(TouchImageViewDialog) imageDialog.findViewById(R.id.ivPopupFull);

        if(isFile){
            Glide.with(this)
                    .load(new File(path))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                    .into(ivPopupFull);
        }else
            AppHelper.setImage(this,ivPopupFull,path);

        imageDialog.show();
    }

    private void addSchedule() {


        progress.setVisibility(View.VISIBLE);


        try{
            if(!schedulePath.matches("")){
                myFile = new File(schedulePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body = MultipartBody.Part.createFormData("uploaded_file", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body = MultipartBody.Part.createFormData("uploaded_file", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("uploaded_file", "", requestFile);
        }

        RequestBody req_id_class = RequestBody.create(MediaType.parse("text/plain"),class_id);
        RequestBody req_id_section = RequestBody.create(MediaType.parse("text/plain"), class_section_id);


        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addSchedule(
                        body,
                        req_id_class,
                        req_id_section
                );
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ivClose.setVisibility(View.GONE);


                 if(!isDelete) {
                     try {
                         if (response.body().getResult().equals("success")) {

                             popUpMessage(true);

                         } else
                             popUpMessage(false);
                     } catch (Exception e) {
                         popUpMessage(false);

                     }

                 }else {
                     if (response.body().getResult().equals("success")) {
                         ischedulePicked=false;
                         pickedSchedule.setImageDrawable(null);

                     } else{
                         Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();
                     }

                 }


                } else {
                    if(!isDelete) {
                        popUpMessage(false);
                    }else
                        Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


                }




            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
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
                   // Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Activity_view_schedule.this, Activity_direction_home.class));

                }
                else {
                    //Toast.makeText(getApplication(),"failed",Toast.LENGTH_LONG).show();
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.


            final Intent galleryIntent = new Intent();


                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                startActivityForResult(chooserIntent, 1020);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1020) {

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
                schedulePath = cursor.getString(columnIndex);
                Glide.with(this)
                        .load(new File(schedulePath))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                        .into(pickedSchedule);
                ischedulePicked = true;

                pickedSchedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImage(schedulePath,true);
                    }
                });

                cursor.close();
            } else {
                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }


        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }


}
