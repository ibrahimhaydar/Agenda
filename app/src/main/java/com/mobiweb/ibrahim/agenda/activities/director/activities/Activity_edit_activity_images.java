package com.mobiweb.ibrahim.agenda.activities.director.activities;

/**
 * Created by ibrahim on 2/18/2018.
 */


import android.Manifest;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterAddImages;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.PermissionsChecker;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_edit_activity_images extends ActivityBase implements RVOnItemClickListener{
    private CustomTextViewBold toolbarTitle,addImage,ctvActivityTitle;
    private CustomTextViewBoldAr toolbarTitleAr;

    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private Button btAdd;
    private ImageView ivDate,pickedImage1,pickedImage2,pickedImage3,pickedImage4,pickedImage5,ivPickImage,ivAddMore;
    private ImageView ivClose1,ivClose2,ivClose3,ivClose4,ivClose5;
    private Activity activity;
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
    private int pickedImageNbr=0;
    private RelativeLayout rlImage1,rlImage2,rlImage3,rlImage4,rlImage5;
    private AdapterAddImages adapterImages;
    private ArrayList<String> arrayImagePaths=new ArrayList<String>();
    private RecyclerView rvPickedImages;
    private String idActivity;
    private ArrayList<Image> arrayImages=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_add_activity_images);
        init();
        toolbarTitle.setText(getString(R.string.addImage));
        toolbarTitleAr.setText(getString(R.string.addImageAr));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_edit_activity_images.super.onBackPressed();
            }
        });
        checker = new PermissionsChecker(this);
        mContext = getApplicationContext();
    }


    private void init() {

        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_edit_activity_images.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setText("Edit");
        ctvActivityTitle=(CustomTextViewBold)findViewById(R.id.ctvActivityTitle);
        ctvActivityTitle.setText(getIntent().getStringExtra(AppConstants.TITLE));
        idActivity=getIntent().getStringExtra(AppConstants.ID);
        // Toast.makeText(getApplication(),idActivity,Toast.LENGTH_LONG).show();

        ivPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view);
            }
        });
arrayImages=AppHelper.getActivityImages();


        rvPickedImages=(RecyclerView)findViewById(R.id.rvPickedImages);
        adapterImages=new AdapterAddImages(arrayImages,this);
        GridLayoutManager glm=new GridLayoutManager(this,4);
        rvPickedImages.setLayoutManager(glm);
        rvPickedImages.setAdapter(adapterImages);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages();
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
                Image selectedImage=new Image("-1",cursor.getString(columnIndex));
                arrayImages.add(selectedImage);
                adapterImages.notifyDataSetChanged();

                cursor.close();

            } else {

                Toast.makeText(getApplication(),"unable to load",Toast.LENGTH_LONG).show();
            }
        }
    }



    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
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
            ctvDialogMessage.setText("uploaded successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    startActivity(new Intent(Activity_edit_activity_images.this,Activity_view_activities.class));

                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }




    private void uploadImages() {

        List<MultipartBody.Part> myImageList=new ArrayList<>();
        progress.setVisibility(View.VISIBLE);


        for (int i=0;i<arrayImages.size();i++) {
            if(arrayImages.get(i).getIdImage().matches("-1")) {
                myFile = new File(arrayImages.get(i).getImageName());
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                body1 = MultipartBody.Part.createFormData("file[]", myFile.getName(), requestFile);
                myImageList.add(body1);
            }

        }

        RequestBody req_id_activity = RequestBody.create(MediaType.parse("text/plain"), idActivity);
        Call<Result> resultCall = RetrofitClient.getClient().create(RetrofitInterface.class).addActivityImages(
                myImageList,
                req_id_activity




        );
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(),response.body().getResult(),Toast.LENGTH_LONG).show();
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





    public void deleteImage(String idImage, final int position) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .deleteActivityImage(new JsonParameters(idImage,1,true));
        call1.enqueue(new Callback<JsonAddHw>() {
            @Override
            public void onResponse(Call<JsonAddHw> call, Response<JsonAddHw> response) {

                try {
                    onImageDelete(response.body(),position);
                    // Log.wtf("className", response.body().getAgenda().get(1).getHwDesc());
                } catch (Exception e) {
                    Log.wtf("exception","exception");

                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                call.cancel();

            }
        });
    }


    private void onImageDelete(JsonAddHw status,int position){
        progress.setVisibility(View.GONE);
        if(status.getStatus().equals("success")) {
            Toast.makeText(getApplication(), "deleted successfully", Toast.LENGTH_LONG).show();
            arrayImages.remove(position);
            adapterImages.notifyDataSetChanged();
            AppHelper.setActivityImages(arrayImages);



        }
        else
            Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onItemClicked(View view, int position) {
        if(adapterImages.getimages().get(position).getIdImage().matches("-1")) {
            arrayImages.remove(position);
            adapterImages.notifyDataSetChanged();
        }else {
            deleteImage(adapterImages.getimages().get(position).getIdImage(), position);
        }

    }
}
