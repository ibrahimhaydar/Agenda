package com.mobiweb.ibrahim.agenda.activities.director.annoucement;

import com.google.gson.Gson;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterAddImages;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterPopupClasses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.PermissionsActivity;
import com.mobiweb.ibrahim.agenda.models.entities.Allclass;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.PostClasses;
import com.mobiweb.ibrahim.agenda.models.entities.PostDeletedImages;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonGetAllClasses;
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

public class Activity_edit_announcement extends ActivityBase  implements RVOnItemClickListener {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private EditText edHwTitle,etHwDesc;
    private Button btAdd;
    private Activity activity;
    private CustomTextView ctvDialogMessage;
    private CustomTextViewBold ctvTitleLabel,ctvDescLabel;
    private LinearLayout linearDate;
    private ArrayList<PostDeletedImages> arrayPostDeleted=new ArrayList<>();


    private Dialog responseDialog;
    private Button btOk;
    private String id_announcement;

    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;
    private LinearLayout linearProgressDialog;
    private String isFilter="0";
    private ArrayList<Image> arrayImagePaths=new ArrayList<Image>();
    private ArrayList<Allclass> arrayChanged=new ArrayList<>();
    private ArrayList<Allclass> arrayAdapterClasses =new ArrayList<Allclass>();
    private File myFile;
    private RecyclerView rvPickedImages;
    private MultipartBody.Part body1,body2,body3,body4,body5;
    private AdapterAddImages adapterImages;
    private ImageView ivPickImage;
    private LinearLayout linearPickedImage;
    private String withImages="0";
    private boolean firstPopup=true;


    private LinearLayout linearFilterClasses;
    private RadioButton rdAll, rdFilter;
    private RadioGroup rgFilterClasses;
    private Dialog studentsDialog;
    private RecyclerView rvClasses;
    private AdapterPopupClasses adapterClasses;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activites);
        init();
        toolbarTitle.setText(getString(R.string.edit_announcement));
        toolbarTitleAr.setText(getString(R.string.edit_announcement_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_edit_announcement.super.onBackPressed();
            }
        });
        checker = new PermissionsChecker(this);


        arrayImagePaths=AppHelper.getActivityImages();
        rvPickedImages=(RecyclerView)findViewById(R.id.rvPickedImages);
        adapterImages=new AdapterAddImages(arrayImagePaths,this,"announcement");
        GridLayoutManager glm=new GridLayoutManager(this,4);
        rvPickedImages.setLayoutManager(glm);
        rvPickedImages.setAdapter(adapterImages);
        ivPickImage=(ImageView)findViewById(R.id.ivPickImage);
        linearPickedImage=(LinearLayout)findViewById(R.id.linearPickedImage);
        linearPickedImage.setVisibility(View.VISIBLE);
        ivPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePopup(view);
            }
        });
    }


    private void init() {
        isFilter=getIntent().getStringExtra(AppConstants.IS_FILTER);

        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        linearDate=(LinearLayout)findViewById(R.id.linearDate);
        linearFilterClasses=(LinearLayout)findViewById(R.id.linearFilterClasses);
        linearDate.setVisibility(View.GONE);
        linearFilterClasses.setVisibility(View.VISIBLE);


        rgFilterClasses =(RadioGroup)findViewById(R.id.rgFilter);
        rdAll =(RadioButton) rgFilterClasses.findViewById(R.id.rdAll);
        rdFilter =(RadioButton) rgFilterClasses.findViewById(R.id.rdFilter);
        if(isFilter.matches("0"))
           rdAll.setChecked(true);
        else
            rdFilter.setChecked(true);
/*        rgFilterClasses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               if(radioGroup.getCheckedRadioButtonId()==R.id.rdFilter){
                   isFilter="1";
                   shoPopupClasses();

               }

            }
        });*/


        RadioButton rb = (RadioButton) findViewById(R.id.rdFilter);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilter="1";
                shoPopupClasses();
            }
        });
        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_edit_announcement.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        edHwTitle=(EditText)findViewById(R.id.etTitle);
       // edHwTitle.setText(AppHelper.getCourseName());
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setText("Edit");
        id_announcement=getIntent().getStringExtra(AppConstants.ID);
        edHwTitle.setText(getIntent().getStringExtra(AppConstants.TITLE));
        etHwDesc.setText(getIntent().getStringExtra(AppConstants.DESCRIPTION));



        ctvTitleLabel=(CustomTextViewBold)findViewById(R.id.ctvTitleLabel);
        ctvDescLabel=(CustomTextViewBold)findViewById(R.id.ctvDescLabel);

        ctvTitleLabel.setText("Announcement title");
        ctvDescLabel.setText("Announcement Description");

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill title and desc",Toast.LENGTH_LONG).show();
                }

                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    editActivity(edHwTitle.getText().toString(),etHwDesc.getText().toString(),id_announcement);
                }
            }
        });





    }
    private void shoPopupClasses(){

        studentsDialog = new Dialog(this);
        studentsDialog.setContentView(R.layout.popup_classes);
        rvClasses =(RecyclerView) studentsDialog.findViewById(R.id.rvCourses);
        linearProgressDialog=(LinearLayout)studentsDialog.findViewById(R.id.linearProgressDialog);
        linearProgressDialog.setVisibility(View.VISIBLE);
        studentsDialog.show();

        arrayChanged=new ArrayList<Allclass>();

        Button btDone=(Button)studentsDialog.findViewById(R.id.btDone);
        Button btCancel=(Button)studentsDialog.findViewById(R.id.btCancel);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapterHasSelected()) {
                    isFilter = "1";
                    firstPopup = false;
                    studentsDialog.dismiss();
                }else {
                    isFilter="0";
                    firstPopup=true;
                    rdAll.setChecked(true);
                    studentsDialog.dismiss();
                }

            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    for(int i=0;i<arrayChanged.size();i++)
                        Log.wtf("array_changed",arrayChanged.get(i).getClassName());
                }catch (Exception e){}


                if(firstPopup){
                    isFilter = "0";
                    rdAll.setChecked(true);
                    uncheckClasses();
                    studentsDialog.dismiss();
                    firstPopup=true;
                }

                else if(arrayChanged.size()>0 && adapterHasSelected()){
                    firstPopup=false;
                    isFilter="1";
                    resetChangedRecords();
                    studentsDialog.dismiss();
                }
                else if(arrayChanged.size()==0 && adapterHasSelected()){
                    firstPopup=false;
                    isFilter="1";
                    studentsDialog.dismiss();
                }
                else {
                    firstPopup=true;
                    isFilter = "0";
                    uncheckClasses();
                    rdAll.setChecked(true);
                    studentsDialog.dismiss();
                }
            }
        });

        if(adapterClasses==null)
            retreiveClasses();
        else{
            try{linearProgressDialog.setVisibility(View.GONE);
                GridLayoutManager glm=new GridLayoutManager(this,1);
                rvClasses.setLayoutManager(glm);
                rvClasses.setAdapter(adapterClasses);
            }catch (Exception e){}
        }




        studentsDialog.setCanceledOnTouchOutside(false);
        studentsDialog.setCancelable(false);

    }

    private void uncheckClasses(){
        try{
            for(int i=0;i<arrayAdapterClasses.size();i++){
                arrayAdapterClasses.get(i).setSelected(false);

            }
            adapterClasses.notifyDataSetChanged();
        }catch (Exception e){}
    }


    private boolean adapterHasSelected(){
        boolean hasSelected=false;
        try {
            for (int i = 0; i < arrayAdapterClasses.size(); i++) {
                if (arrayAdapterClasses.get(i).getSelected()) {
                    hasSelected = true;
                    break;
                }
            }
        }catch (Exception e){
            hasSelected=false;
        }
        return hasSelected;
    }


    private void addRemoveArrayChanged(Allclass myClass){
        boolean inArray=false;
        int position=-1;
        for(int i=0;i<arrayChanged.size();i++){
            if(arrayChanged.get(i).getIdClass().matches(myClass.getIdClass()) && arrayChanged.get(i).getIdSection().matches(myClass.getIdSection())){
                inArray=true;
                position=i;
                break;
            }
        }

        if(inArray && position!=-1){
            arrayChanged.remove(position);
        }else {
            arrayChanged.add(myClass);
        }


    }


    private void resetChangedRecords(){
        try{
            for(int i=0;i<arrayChanged.size();i++){
                for (int j=0;j<arrayAdapterClasses.size();j++){
                    if(arrayChanged.get(i).getIdSection().matches(arrayAdapterClasses.get(j).getIdSection())
                            && arrayChanged.get(i).getIdClass().matches(arrayAdapterClasses.get(j).getIdClass())){
                        arrayAdapterClasses.get(j).setSelected(!arrayAdapterClasses.get(j).getSelected());
                        Log.wtf("return_change",arrayAdapterClasses.get(j).getClassName());
                    }
                }
            }
            adapterClasses.notifyDataSetChanged();
        }catch (Exception e){
            Log.wtf("exception",e.toString());
        }
    }



    @Override
    public void onItemClicked(View view, int position) {
        if (view.getId() == R.id.ivClose) {

            if(adapterImages.getimages().get(position).getIdImage().matches("-1")) {
                arrayImagePaths.remove(position);
                adapterImages.notifyDataSetChanged();
            }else {

                PostDeletedImages p=new PostDeletedImages(arrayImagePaths.get(position).getIdImage());
                arrayPostDeleted.add(p);
                arrayImagePaths.remove(position);
                adapterImages.notifyDataSetChanged();
               // AppHelper.setActivityImages(arrayImagePaths);
            }


        } else {
            addRemoveArrayChanged(arrayAdapterClasses.get(position));
            arrayAdapterClasses.get(position).setSelected(!arrayAdapterClasses.get(position).getSelected());
            adapterClasses.notifyItemChanged(position);
        }

    }
    private ArrayList<PostClasses> getArraySelected(){
        ArrayList<PostClasses> arrayClasses =new ArrayList();
        PostClasses p = null;

        try{
            if(adapterClasses!=null) {
                adapterClasses.notifyDataSetChanged();
                for (int i = 0; i < arrayAdapterClasses.size(); i++) {
                    if(arrayAdapterClasses.get(i).getSelected()!=null) {
                        if (arrayAdapterClasses.get(i).getSelected()) {
                            p = new PostClasses(arrayAdapterClasses.get(i).getIdClass(), arrayAdapterClasses.get(i).getIdSection());
                            arrayClasses.add(p);
                        }
                    }


                }
            }}catch (Exception e){
            Log.wtf("exception",e.toString());
            arrayClasses=new ArrayList<>();
        }
        return arrayClasses;
    }






    private void editActivity(String title,String description,String id_announcement) {



        progress.setVisibility(View.VISIBLE);
        List<MultipartBody.Part> myImageList=new ArrayList<>();
        if(arrayImagePaths.size()>0) {
            withImages="1";
            for (int i = 0; i < arrayImagePaths.size(); i++) {
                if(arrayImagePaths.get(i).getIdImage().matches("-1")) {
                    myFile = new File(arrayImagePaths.get(i).getImageName());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
                    body1 = MultipartBody.Part.createFormData("file[]", myFile.getName(), requestFile);
                    myImageList.add(body1);
                }
            }
        }else {
            withImages="0";
            body1 = MultipartBody.Part.createFormData("file[]", "");
            myImageList.add(body1);
        }

        String json = new Gson().toJson(new JsonParameters(id_announcement,"1",title,description,isFilter,getArraySelected(),withImages,arrayPostDeleted));
        RequestBody req_json = RequestBody.create(MediaType.parse("application/json"),json);


        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .editAnnouncementImage(myImageList,req_json);
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



    private void retreiveClasses() {
        linearProgressDialog.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getClasses(new JsonParameters(
                        "",false)
                );
        call1.enqueue(new Callback<JsonGetAllClasses>() {
            @Override
            public void onResponse(Call<JsonGetAllClasses> call, Response<JsonGetAllClasses> response) {

                try {
                    onDataRetrieved(response.body());
                    Log.wtf("className", response.body().getAllclasses().get(1).getClassName());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonGetAllClasses> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onDataRetrieved(JsonGetAllClasses AllClasses){



        arrayAdapterClasses=AllClasses.getAllclasses();
        linearProgressDialog.setVisibility(View.GONE);

        if(isFilter.matches("1")) {
            firstPopup=false;
            setSelectedClasses();
        }

        adapterClasses=new AdapterPopupClasses(arrayAdapterClasses,this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvClasses.setLayoutManager(glm);
        rvClasses.setAdapter(adapterClasses);
    }

    private void setSelectedClasses(){



        try{
            for(int i=0;i<arrayAdapterClasses.size();i++){

                for (int j=0;j<AppHelper.getSelectedClasses().size();j++){
                    if(AppHelper.getSelectedClasses().get(j).getIdClass().matches(arrayAdapterClasses.get(i).getIdClass()) &&
                            AppHelper.getSelectedClasses().get(j).getIdSection().matches(arrayAdapterClasses.get(i).getIdSection()))
                        arrayAdapterClasses.get(i).setSelected(true);
                }
            }

        }
        catch (Exception e){

        }
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
                arrayImagePaths.add(selectedImage);
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
            ctvDialogMessage.setText("Edited successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                   // Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Activity_edit_announcement.this,Activity_view_announcement.class));

                }
                else {
                    Toast.makeText(getApplication(),"fail",Toast.LENGTH_LONG).show();
                    //responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


}
