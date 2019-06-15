package com.mobiweb.ibrahim.agenda.activities.director.annoucement;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_edit_announcement extends ActivityBase {
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


    private Dialog responseDialog;
    private Button btOk;
    private String id_announcement;
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

    }


    private void init() {


        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        linearDate=(LinearLayout)findViewById(R.id.linearDate);
        linearDate.setVisibility(View.GONE);

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



    private void editActivity(String title,String description,String id_announcement) {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .editAnnouncement(new JsonParameters("1",title,description,id_announcement,2,true));
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
