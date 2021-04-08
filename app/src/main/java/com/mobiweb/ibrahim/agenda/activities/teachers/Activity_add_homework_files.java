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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class Activity_add_homework_files extends ActivityBase implements RVOnItemClickListener, ProgressRequestBody.UploadCallbacks {
    private CustomTextViewBold toolbarTitle,addImage;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress,linearProgress;
    private EditText edHwTitle,etHwDesc,etRemark;
    private Button btAdd;
    private CustomTextViewBold ctvdate;
    private ImageView ivDate;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dateNow;
    private Activity activity;
    private String id_course,id_class,id_section,id_teacher;
    private String hwImage="";
    private int currentPickedFileType=0;
    private File myFile;
    private MultipartBody.Part body1;
    private String uploadedPath;
    private ArrayList<Files> arrayFiles=new ArrayList<Files>();
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    Context mContext;
    View parentView;
    ImageView imageView;
    TextView textView;

    PermissionsChecker checker;
    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;
    private String selectedDate;
    private int pickedImageNbr=0;
    private List<MultipartBody.Part> myImageList=new ArrayList<>();
    private ImageView ivPickImage;

    private AdapterFiles adapterFiles;
    private RecyclerView rvFiles;
    ProgressBar progressBar;
    TextView tvResizeFile;
    TextView tvFileTotalSize;
    Long totalFilesSize=0L;
    Uri compressUri = null;
    private TextView tvCardClassName;

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
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
                Activity_add_homework_files.super.onBackPressed();
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
                addClick();
            else
                Toast.makeText(getApplication(),"Please enable permission to add homework", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {

        tvCardClassName=findViewById(R.id.tvCardClassName);
        tvCardClassName.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));

        tvFileTotalSize=findViewById(R.id.tvFileTotalSizes);
        progressBar=findViewById(R.id.progressBar);
        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);
        progress=(LinearLayout)findViewById(R.id.progress);
        linearProgress=findViewById(R.id.linearProgress);
        rvFiles=(RecyclerView) findViewById(R.id.rvFiles);
        tvResizeFile=findViewById(R.id.btResizeFile);
        tvResizeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.startNewActivity(Activity_add_homework_files.this,"com.pandavideocompressor");
            }
        });
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_add_homework_files.this, Activity_teacher.class));
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
        adapterFiles=new AdapterFiles(arrayFiles,this);
        GridLayoutManager glm=new GridLayoutManager(this,4);
        rvFiles.setLayoutManager(glm);
        rvFiles.setAdapter(adapterFiles);


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

           /*     if (ContextCompat.checkSelfPermission(Activity_add_homework_files.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_add_homework_files.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            111);
                }else {


                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();

                    File f = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/lass/videos");

                    if (f.mkdirs() || f.isDirectory()) {
                        //compress and output new video specs
                        //new VideoCompressAsyncTask(this).execute("true", mCurrentPhotoPath, f.getPath());

                        for (int i = 0; i < arrayFiles.size(); i++) {
                            if (arrayFiles.get(i).getFileType() == AppConstants.FILE_TYPE_VIDEO) {

                                new VideoCompressAsyncTask(Activity_add_homework_files.this).execute("false", arrayFiles.get(i).getUri(), f.getPath());
                            }
                        }
                    }

                }*/

                if (ContextCompat.checkSelfPermission(Activity_add_homework_files.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity_add_homework_files.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            111);}
                else {

                   addClick();
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
                showPicker();
            }
        });

    }

    private void addClick(){
        if (etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplication(), "Please fill title and desc", Toast.LENGTH_LONG).show();
        } else if (ctvdate.getText().toString().matches("Select Date")) {
            Toast.makeText(getApplication(), "Please choose date of homework", Toast.LENGTH_LONG).show();
        } else {
            //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
             addHw(edHwTitle.getText().toString(), etHwDesc.getText().toString(), selectedDate, etRemark.getText().toString());
             //  compressBeforeUpload();

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



    public static String resizeAndCompressImageBeforeSend(Context context,String filePath,String fileName){
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap","cacheDir: "+context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir()+fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return  context.getCacheDir()+fileName;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }


    private void compressBeforeUpload(){

        for (int i=0;i<arrayFiles.size();i++) {

            if(arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                try {
                    myFile= AppHelper.getCompressed(this,arrayFiles.get(i).getFileName(),i);
                    Log.wtf("myfile_compress","compress"+i+"----"+myFile.getName());
                } catch (IOException e) {

                }

            }
        }

    }


    private void addHw(String hw_title,String hw_desc,String date,String hw_info) {
        TelephonyHelper telephonyHelper = new TelephonyHelper();
        String deviceId = telephonyHelper.getDeviceID(this);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

    linearProgress.setVisibility(View.VISIBLE);

       // progress.setVisibility(View.VISIBLE);
        try{
            if(arrayFiles.size()>0){

                myImageList=new ArrayList<>();
             //   progress.setVisibility(View.VISIBLE);

                for (int i=0;i<arrayFiles.size();i++) {

                    if(arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                        myFile = new File(arrayFiles.get(i).getFileName());
                         //if(myFile.length()/1024>1)
                           myFile= AppHelper.getCompressed(this,arrayFiles.get(i).getFileName(),i);
                    }
                    else  if(arrayFiles.get(i).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
                        //  String filePath = SiliCompressor.with(this).compressVideo(arrayFiles.get(i).getFileName(), destinationDirectory);
                         myFile = new File(arrayFiles.get(i).getFileName());

                    }


                    ProgressRequestBody fileBody = new ProgressRequestBody(myFile,"*", Activity_add_homework_files.this);
                    //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                    ProgressRequestBody requestFile = new ProgressRequestBody(myFile,"multipart/form-data", Activity_add_homework_files.this);

                    body1 = MultipartBody.Part.createFormData("file[]","file_"+i+deviceId+"_"+ts+myFile.getName().substring(myFile.getName().lastIndexOf(".")) /*myFile.getName()*/, requestFile);
                    myImageList.add(body1);
                }


            }else {
                progress.setVisibility(View.VISIBLE);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                body1 = MultipartBody.Part.createFormData("file[]", "", requestFile);
            }}catch (Exception e){
            progress.setVisibility(View.VISIBLE);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body1 = MultipartBody.Part.createFormData("file[]", "", requestFile);
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




        Call<Result> resultCall = RetrofitClient.getClient().create(RetrofitInterface.class).addHwFiles(
                myImageList,
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
                linearProgress.setVisibility(View.GONE);
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

    public static String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;

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
                        startActivity(new Intent(Activity_add_homework_files.this, Activity_all_teachers.class));
                    else
                        startActivity(new Intent(Activity_add_homework_files.this, Activity_teacher.class));

                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }



 /*   class VideoCompressAsyncTask extends AsyncTask<String, String, Sftring> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            Log.wtf("compress_file","video start");
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
            try {

                //This bellow is just a temporary solution to test that method call works
                boolean b = Boolean.parseBoolean(paths[0]);
                if (b) {
                    filePath = SiliCompressor.with(mContext).compressVideo(paths[1], paths[2]);
                } else {
                    Uri videoContentUri = Uri.parse(paths[1]);
                    // Example using the bitrate and video size parameters
                    filePath = SiliCompressor.with(mContext).compressVideo(
                            videoContentUri,
                            paths[2],
                            1280,
                            720,
                            1500000);
               *//*     filePath = SiliCompressor.with(mContext).compressVideo(
                            videoContentUri,
                            paths[2]);*//*
                }


            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            progress.setVisibility(View.GONE);
            File imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            String text = String.format(Locale.US, "%s\nName: %s\nSize: %s", "complete", imageFile.getName(), value);

            Log.wtf("compress_file", "video end : " + compressedFilePath);
        }
    }
*/




    @Override
    public void onItemClicked(View view, int position) {
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
     Log.wtf("error","error");
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
