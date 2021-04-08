package com.mobiweb.ibrahim.agenda.activities.teachers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterFiles;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Files;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.PermissionsChecker;
import com.mobiweb.ibrahim.agenda.utils.ProgressRequestBody;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;
import com.mobiweb.ibrahim.agenda.utils.TelephonyHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

public class Activity_edit_hw_files_inside extends ActivityBase implements RVOnItemClickListener, ProgressRequestBody.UploadCallbacks  {
    private CustomTextViewBold toolbarTitle,addImage;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress,linearProgress;
    private EditText edHwTitle,etHwDesc,etRemark;;
    private Button btAdd;
    private CustomTextViewBold ctvdate;
    private ImageView ivDate,ivPickImage;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dateNow;
    private Activity activity;
    private String id_agenda,id_class,id_section,id_teacher,id_course;
    private String deletedImageId="";

    private MultipartBody.Part body;
    private String uploadedPath;

    private List<MultipartBody.Part> myImageList=new ArrayList<>();
    private ArrayList<Files> arrayFiles=new ArrayList<Files>();
    private AdapterFiles adapterFiles;
    private RecyclerView rvFiles;
    TextView tvFileTotalSize;
    private File myFile;

    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;
    private String selectedDate;
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView tvCardClassName;

    ProgressBar progressBar;

    TextView tvResizeFile;
    Long totalFilesSize=0L;



    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;
    View parentView;
    ImageView imageView;
    TextView textView;
    String imagePath="";
    PermissionsChecker checker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework_files);
        init();
        toolbarTitle.setText(getString(R.string.add_hw));
        toolbarTitleAr.setText(getString(R.string.add_hw_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_edit_hw_files_inside.super.onBackPressed();
            }
        });
        checker = new PermissionsChecker(this);
        // imageView = (ImageView) findViewById(R.id.imageView);
        mContext = getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 111 && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                editClick();
            else
                Toast.makeText(getApplication(),"Please enable permission to add homework", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        tvCardClassName=findViewById(R.id.tvCardClassName);
        tvCardClassName.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));

        //  Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.HW_IMAGE),Toast.LENGTH_LONG).show();
        id_agenda=getIntent().getStringExtra(AppConstants.HW_ID);
        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        tvFileTotalSize=findViewById(R.id.tvFileTotalSizes);
        tvResizeFile=findViewById(R.id.btResizeFile);
        tvResizeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.startNewActivity(Activity_edit_hw_files_inside.this,"com.pandavideocompressor");
            }
        });

        linearProgress=findViewById(R.id.linearProgress);
        progressBar=findViewById(R.id.progressBar);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);

        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);
        rvFiles=(RecyclerView) findViewById(R.id.rvFiles);
        arrayFiles.addAll(AppHelper.getHwFiles()) ;
        adapterFiles=new AdapterFiles(arrayFiles, Activity_edit_hw_files_inside.this);
        GridLayoutManager glm=new GridLayoutManager(Activity_edit_hw_files_inside.this,4);
        rvFiles.setLayoutManager(glm);
        rvFiles.setAdapter(adapterFiles);

        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);



        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_edit_hw_files_inside.this, Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_edit_hw_files_inside.this, Activity_teacher.class));
            }
        });


        edHwTitle=(EditText)findViewById(R.id.etTitle);
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        edHwTitle.setEnabled(false);
        try{
            edHwTitle.setText(AppHelper.getCourseName());}
        catch (Exception e){
            edHwTitle.setText("");}



        try {
            etHwDesc.setText(getIntent().getStringExtra(AppConstants.HW_DESC));
        }catch (Exception e){
            etHwDesc.setText("");
        }


        etRemark=(EditText)findViewById(R.id.etRemark);
        try {
            etRemark.setText(getIntent().getStringExtra(AppConstants.HW_INFO));
        }catch (Exception e){
            etRemark.setText("");
        }






        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setText("Edit");
        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
        // textView=(TextView)findViewById(R.id.pickImage);
        //addImage=(CustomTextViewBold)findViewById(R.id.addImage);

/*        addImage.setOnClickListener(new View.OnClickListener() {
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

                if (ContextCompat.checkSelfPermission(Activity_edit_hw_files_inside.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_edit_hw_files_inside.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            111);}
                else {

                    editClick();
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
                                // selectedDate=year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                                selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));

                                ctvdate.setText(dayOfWeek +" "+(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) +" "+MONTHS[monthOfYear]+" "+year);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        ivPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker();
            }
        });





    }

    private void editClick(){
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

    private void showPicker(){
        final CharSequence[] options = {"Images", "Videos", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select From...");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                final Intent galleryIntent = new Intent();
                if (options[item].equals("Images")) {
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_PICK);
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                    startActivityForResult(chooserIntent, 1020);
                } else if (options[item].equals("Videos")) {
                    galleryIntent.setType("video/*");
                    galleryIntent.setAction(Intent.ACTION_PICK);
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.string_choose_image));
                    startActivityForResult(chooserIntent, 1010);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void addHw(String hw_title,String hw_desc,String date,String hw_info) {

        TelephonyHelper telephonyHelper = new TelephonyHelper();
        String deviceId = telephonyHelper.getDeviceID(this);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        progress.setVisibility(View.VISIBLE);
        linearProgress.setVisibility(View.VISIBLE);
        try{
            if(arrayFiles.size()>0){
                myImageList=new ArrayList<>();
               // progress.setVisibility(View.VISIBLE);
                for (int i=0;i<arrayFiles.size();i++) {

                   if(arrayFiles.get(i).getIdFile().matches("-1")) {
                       progress.setVisibility(View.GONE);
                       linearProgress.setVisibility(View.VISIBLE);
                       if (arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                          // myFile = new File(arrayFiles.get(i).getFileName());
                          // if(myFile.length()/1024>1)
                               myFile= AppHelper.getCompressed(this,arrayFiles.get(i).getFileName(),i);
                       } else if (arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                           //  String filePath = SiliCompressor.with(this).compressVideo(arrayFiles.get(i).getFileName(), destinationDirectory);
                           myFile = new File(arrayFiles.get(i).getFileName());
                       }
                       ProgressRequestBody fileBody = new ProgressRequestBody(myFile,"*", Activity_edit_hw_files_inside.this);
                       //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                       ProgressRequestBody requestFile = new ProgressRequestBody(myFile,"multipart/form-data", Activity_edit_hw_files_inside.this);


                      // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                       body = MultipartBody.Part.createFormData("file[]", "file_" + i + deviceId + "_" + ts + myFile.getName().substring(myFile.getName().lastIndexOf(".")) /*myFile.getName()*/, requestFile);
                       myImageList.add(body);
                   }
                }

            }else {
                linearProgress.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body = MultipartBody.Part.createFormData("uploaded_file1", "", requestFile);
            }}catch (Exception e){
            linearProgress.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("uploaded_file1", "", requestFile);
        }

        RequestBody req_date = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody req_id_agenda = RequestBody.create(MediaType.parse("text/plain"), id_agenda);
        RequestBody req_hw_title = RequestBody.create(MediaType.parse("text/plain"), hw_title);
        RequestBody req_hw_desc = RequestBody.create(MediaType.parse("text/plain"), hw_desc);
        RequestBody req_deleted_id = RequestBody.create(MediaType.parse("text/plain"), deletedImageId);
        RequestBody req_hwInfo = RequestBody.create(MediaType.parse("text/plain"), hw_info);

        Call<Result> resultCall = RetrofitClient.getClient().create(RetrofitInterface.class).editHwFiles(
               myImageList,
                req_id_agenda,
                req_hw_title,
                req_hw_desc,
                req_date,
                req_deleted_id,
                req_hwInfo


        );
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progress.setVisibility(View.GONE);
                linearProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    // Toast.makeText(getApplication(),"response: "+response.body().getResult(),Toast.LENGTH_LONG).show();
                    try {
                        Log.wtf("result",response.body().getResult());
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


                imagePath = "";

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
                linearProgress.setVisibility(View.GONE);
                popUpMessage(false);
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

    public static String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;

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
                String videoPath = cursor.getString(columnIndex);
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
                        arrayFiles.add(new Files("-1",videoPath, AppConstants.FILE_TYPE_VIDEO,selectedImageUri.toString()));
                        adapterFiles.notifyDataSetChanged();
                        setFilesSize();

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
                String imagePath = cursor.getString(columnIndex);
                arrayFiles.add(new Files("-1",imagePath, AppConstants.FILE_TYPE_IMAGE,selectedImageUri.toString()));
                adapterFiles.notifyDataSetChanged();
                setFilesSize();

                cursor.close();
            } else {
                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }


        }
    }



    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("Homework is edited successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                        startActivity(new Intent(Activity_edit_hw_files_inside.this, Activity_all_teachers.class));
                    else
                        startActivity(new Intent(Activity_edit_hw_files_inside.this, Activity_teacher.class));

                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
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


    @Override
    public void onItemClicked(View view, int position) {
        if(deletedImageId.isEmpty() && !arrayFiles.get(position).getIdFile().matches("-1"))
            deletedImageId=arrayFiles.get(position).getIdFile();
        else if(!deletedImageId.isEmpty() && !arrayFiles.get(position).getIdFile().matches("-1"))
            deletedImageId+=","+arrayFiles.get(position).getIdFile();
        arrayFiles.remove(position);
        adapterFiles.notifyItemRemoved(position);
        setFilesSize();

    }




    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        Log.wtf("percentage",percentage+"aa");
        progressBar.setProgress(percentage);
    }

    @Override
    public void onError() {
        // do something on error
    }

    @Override
    public void onFinish() {
        // do something on upload finished,
        // for example, start next uploading at a queue
        progressBar.setProgress(100);
    }

    private void setFilesSize(){
        try {
            File myFile = null;
            float totalFilesSize = 0f;
            for (int i = 0; i < arrayFiles.size(); i++) {
                if (arrayFiles.get(i).getIdFile().matches("-1")) {
                    if (arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                        myFile = new File(arrayFiles.get(i).getFileName());
                    } else if (arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                        //  String filePath = SiliCompressor.with(this).compressVideo(arrayFiles.get(i).getFileName(), destinationDirectory);
                        myFile = new File(arrayFiles.get(i).getFileName());
                    }
                    totalFilesSize =totalFilesSize+ Float.parseFloat(myFile.length()+"") / (1024.0f*1024.0f);
                }
            }
            tvFileTotalSize.setText("total picked files size : " + String.format("%.2f", totalFilesSize) + " MB");
        }catch (Exception e){
            Log.wtf("size_exception",e.toString());
        }
    }
}
