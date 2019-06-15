package com.mobiweb.ibrahim.agenda.activities.teachers;

import android.Manifest;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.PermissionsChecker;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_Add_homework extends ActivityBase {
    private CustomTextViewBold toolbarTitle,addImage;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private EditText edHwTitle,etHwDesc,etRemark;
    private Button btAdd;
    private CustomTextViewBold ctvdate;
    private ImageView ivDate,pickedImage1,pickedImage2,pickedImage3,pickedImage4,pickedImage5,ivPickImage,ivAddMore;
    private ImageView ivClose1,ivClose2,ivClose3,ivClose4,ivClose5;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dateNow;
    private Activity activity;
    private String id_course,id_class,id_section,id_teacher;
    private String hwImage="";
    private File myFile;
    private MultipartBody.Part body1,body2,body3,body4,body5;
    private String uploadedPath;

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;
    View parentView;
    ImageView imageView;
    TextView textView;
    private String imagePath1="",imagePath2="",imagePath3="",imagePath4="",imagePath5="";
    PermissionsChecker checker;
    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;
    private String selectedDate;
    private int pickedImageNbr=0;
    private RelativeLayout rlImage1,rlImage2,rlImage3,rlImage4,rlImage5;
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);
        init();
        toolbarTitle.setText(getString(R.string.add_hw));
        toolbarTitleAr.setText(getString(R.string.add_hw_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_Add_homework.super.onBackPressed();
            }
        });
        checker = new PermissionsChecker(this);
       // imageView = (ImageView) findViewById(R.id.imageView);
        mContext = getApplicationContext();
    }


    private void init() {


        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        pickedImage1=(ImageView)findViewById(R.id.pickedImage1);
        pickedImage2=(ImageView)findViewById(R.id.pickedImage2);
        pickedImage3=(ImageView)findViewById(R.id.pickedImage3);
        pickedImage4=(ImageView)findViewById(R.id.pickedImage4);
        pickedImage5=(ImageView)findViewById(R.id.pickedImage5);

        ivClose1=(ImageView)findViewById(R.id.ivClose1);
        ivClose2=(ImageView)findViewById(R.id.ivClose2);
        ivClose3=(ImageView)findViewById(R.id.ivClose3);
        ivClose4=(ImageView)findViewById(R.id.ivClose4);
        ivClose5=(ImageView)findViewById(R.id.ivClose5);




        rlImage1=(RelativeLayout)findViewById(R.id.rlImage1);
        rlImage2=(RelativeLayout)findViewById(R.id.rlImage2);
        rlImage3=(RelativeLayout)findViewById(R.id.rlImage3);
        rlImage4=(RelativeLayout)findViewById(R.id.rlImage4);
        rlImage5=(RelativeLayout)findViewById(R.id.rlImage5);

        ivClose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlImage1.setVisibility(View.GONE);
                imagePath1="";
            }
        });

        ivClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlImage2.setVisibility(View.GONE);
                imagePath2="";
            }
        });

        ivClose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlImage3.setVisibility(View.GONE);
                imagePath3="";
            }
        });

        ivClose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlImage4.setVisibility(View.GONE);
                imagePath4="";
            }
        });

        ivClose5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlImage5.setVisibility(View.GONE);
                imagePath5="";
            }
        });


        ivAddMore=(ImageView)findViewById(R.id.ivAddMore);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Add_homework.this,Activity_teacher.class));
            }
        });
        ivRight.setVisibility(View.GONE);
        edHwTitle=(EditText)findViewById(R.id.etTitle);
        edHwTitle.setEnabled(false);
        edHwTitle.setText(AppHelper.getCourseName());
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);
        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
       // textView=(TextView)findViewById(R.id.pickImage);
       // addImage=(CustomTextViewBold)findViewById(R.id.addImage);
        etRemark=(EditText)findViewById(R.id.etRemark);



 /*       addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(imagePath)) {
                    if (InternetConnection.checkConnection(mContext)) {
                        //  uploadImage();
                    } else {
                        Toast.makeText(getApplication(),"no connection",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(),"first attach file to upload",Toast.LENGTH_LONG).show();
                }
            }
        });*/






        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill title and desc",Toast.LENGTH_LONG).show();
                }
                else if(ctvdate.getText().toString().matches("Select Date")){
                    Toast.makeText(getApplication(),"Please choose date of homework",Toast.LENGTH_LONG).show();
                }
                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    addHw(edHwTitle.getText().toString(),etHwDesc.getText().toString(),selectedDate,etRemark.getText().toString());
                }
            }
        });

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                String dayOfWeek = simpledateformat.format(date);
                                //selectedDate=year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
              selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                                ctvdate.setText(dayOfWeek +" "+(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) +" "+MONTHS[monthOfYear]+" "+year);

                                //    ctvdate.setText((dayOfMonth<10?(dayOfMonth):(dayOfMonth))+" "+  (monthOfYear+1<10?(monthOfYear+1):(monthOfYear+1)) + " "+ year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        ivPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view);
            }
        });
        ivAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view);
            }
        });


    }



    private void addHw(String hw_title,String hw_desc,String date,String hw_info) {


        progress.setVisibility(View.VISIBLE);
        try{
            if(!imagePath1.matches("")){
                myFile = new File(imagePath1);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body1 = MultipartBody.Part.createFormData("uploaded_file1", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body1 = MultipartBody.Part.createFormData("uploaded_file1", "", requestFile);
            }}catch (Exception e){
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body1 = MultipartBody.Part.createFormData("uploaded_file1", "", requestFile);
        }

        try{
            if(!imagePath2.matches("")){
                myFile = new File(imagePath2);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body2 = MultipartBody.Part.createFormData("uploaded_file2", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body2 = MultipartBody.Part.createFormData("uploaded_file2", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body2 = MultipartBody.Part.createFormData("uploaded_file2", "", requestFile);
        }


        try{
            if(!imagePath3.matches("")){
                myFile = new File(imagePath3);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body3 = MultipartBody.Part.createFormData("uploaded_file3", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body3 = MultipartBody.Part.createFormData("uploaded_file3", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body3 = MultipartBody.Part.createFormData("uploaded_file3", "", requestFile);
        }


        try{
            if(!imagePath4.matches("")){
                myFile = new File(imagePath4);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body4 = MultipartBody.Part.createFormData("uploaded_file4", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body4 = MultipartBody.Part.createFormData("uploaded_file4", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body4 = MultipartBody.Part.createFormData("uploaded_file4", "", requestFile);
        }


        try{
            if(!imagePath5.matches("")){
                myFile = new File(imagePath5);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body5 = MultipartBody.Part.createFormData("uploaded_file5", myFile.getName(), requestFile);
            }else {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body5 = MultipartBody.Part.createFormData("uploaded_file5", "", requestFile);
            }}catch (Exception e){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body5 = MultipartBody.Part.createFormData("uploaded_file5", "", requestFile);
        }


        RequestBody req_id_class = RequestBody.create(MediaType.parse("text/plain"), id_class);
        RequestBody req_id_section = RequestBody.create(MediaType.parse("text/plain"), id_section);
        RequestBody req_date = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody req_id_teacher = RequestBody.create(MediaType.parse("text/plain"), id_teacher);
        RequestBody req_id_course = RequestBody.create(MediaType.parse("text/plain"), id_course);
        RequestBody req_hw_title = RequestBody.create(MediaType.parse("text/plain"), hw_title);
        RequestBody req_hw_desc = RequestBody.create(MediaType.parse("text/plain"), hw_desc);
        RequestBody req_hwImage = RequestBody.create(MediaType.parse("text/plain"), hwImage);
        RequestBody req_hwInfo = RequestBody.create(MediaType.parse("text/plain"), hw_info);




        Call<Result> resultCall = RetrofitClient.getClient().create(RetrofitInterface.class).uploadImage(
                body1,
                body2,
                body3,
                body4,
                body5,
                req_id_class,
                req_id_section,
                req_date,
                req_id_teacher,
                req_id_course,
                req_hw_title,
                req_hw_desc,
                req_hwImage,
                req_hwInfo


        );
        resultCall.enqueue(new Callback<Result>() {
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


                imagePath1 = "";
                imagePath2 = "";
                imagePath3 = "";
                imagePath4 = "";
                imagePath5 = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
    }




    public void showImagePopup(View view) {
        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
            startActivityForResult(chooserIntent, 1010);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
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


/*                Glide.with(this).load(new File(imagePath))
                        .into(imageView);*/



                  if(rlImage1.getVisibility()==View.GONE) {
                      imagePath1 = cursor.getString(columnIndex);
                      Glide.with(this)
                              .load(new File(imagePath1))
                              .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                              .into(pickedImage1);
                      rlImage1.setVisibility(View.VISIBLE);
                      ivAddMore.setVisibility(View.VISIBLE);
                      pickedImageNbr++;
                  }else if(rlImage2.getVisibility()==View.GONE) {
                      imagePath2 = cursor.getString(columnIndex);
                      Glide.with(this)
                              .load(new File(imagePath2))
                              .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                              .into(pickedImage2);
                      rlImage2.setVisibility(View.VISIBLE);
                      ivAddMore.setVisibility(View.VISIBLE);
                      pickedImageNbr++;
                  }else if(rlImage3.getVisibility()==View.GONE) {
                      imagePath3 = cursor.getString(columnIndex);
                      Glide.with(this)
                              .load(new File(imagePath3))
                              .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                              .into(pickedImage3);
                      rlImage3.setVisibility(View.VISIBLE);
                      ivAddMore.setVisibility(View.VISIBLE);
                      pickedImageNbr++;
                  }else if(rlImage4.getVisibility()==View.GONE) {
                      imagePath4 = cursor.getString(columnIndex);
                      Glide.with(this)
                              .load(new File(imagePath4))
                              .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                              .into(pickedImage4);
                      rlImage4.setVisibility(View.VISIBLE);
                      ivAddMore.setVisibility(View.VISIBLE);
                      pickedImageNbr++;
                  }else if(rlImage5.getVisibility()==View.GONE) {
                      imagePath5 = cursor.getString(columnIndex);
                      Glide.with(this)
                              .load(new File(imagePath5))
                              .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                              .into(pickedImage5);
                      rlImage5.setVisibility(View.VISIBLE);
                      ivAddMore.setVisibility(View.GONE);
                      pickedImageNbr++;
                  }else {
                      ivAddMore.setVisibility(View.GONE);
                  }







               // Toast.makeText(getApplication(),"reselect",Toast.LENGTH_LONG).show();
                cursor.close();

               // textView.setVisibility(View.GONE);
              //  imageView.setVisibility(View.VISIBLE);
            } else {
             //   textView.setVisibility(View.VISIBLE);
             //   imageView.setVisibility(View.GONE);
                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }
        }
    }



    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    @Override
    public void onBackPressed() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
        super.onBackPressed();
    }


    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                         startActivity(new Intent(Activity_Add_homework.this,Activity_all_teachers.class));
                    else
                         startActivity(new Intent(Activity_Add_homework.this,Activity_teacher.class));
                     finish();
                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


}
