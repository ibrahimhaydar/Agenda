package com.mobiweb.ibrahim.agenda.activities.director.exams;

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
import android.widget.TextView;
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

public class Activity_exams_edit_general_info extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private EditText etTitle,etNote,etMore;
    private Button btAdd;
    private Activity activity;
    private CustomTextView ctvDialogMessage;
    private CustomTextViewBold ctvTitleLabel,ctvDescLabel;
    private String id_exam;
    private TextView tvCardClassName,tvCardExamName;


    private Dialog responseDialog;
    private Button btOk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditrector_exams_add_gen_info);
        init();



    }


    private void init() {
        tvCardClassName=findViewById(R.id.tvCardClassName);
        tvCardClassName.setText(AppHelper.getClass_name());
        tvCardExamName=findViewById(R.id.tvCardExamName);
        tvCardExamName.setText(AppHelper.getExamCategoryName());
        tvCardExamName.setVisibility(View.VISIBLE);

        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);


        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_exams_edit_general_info.this, Activity_direction_home.class));

            }
        });
        etTitle=(EditText)findViewById(R.id.etTitle);
        etNote=(EditText)findViewById(R.id.etNote);
        etMore=(EditText)findViewById(R.id.etMore);

        etTitle.setText(getIntent().getStringExtra(AppConstants.TITLE));
        etNote.setText(getIntent().getStringExtra(AppConstants.NOTE));
        etMore.setText(getIntent().getStringExtra(AppConstants.MORE));


        btAdd=(Button)findViewById(R.id.btAdd);
        btAdd.setText("Edit");
        ctvTitleLabel=(CustomTextViewBold)findViewById(R.id.ctvTitleLabel);
        ctvDescLabel=(CustomTextViewBold)findViewById(R.id.ctvDescLabel);




        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill title",Toast.LENGTH_LONG).show();
                }
                else if(
                        etTitle.getText().toString().matches(getIntent().getStringExtra(AppConstants.TITLE)) &&
                        etNote.getText().toString().matches(getIntent().getStringExtra(AppConstants.NOTE)) &&
                        etMore.getText().toString().matches(getIntent().getStringExtra(AppConstants.MORE))){
                    Toast.makeText(getApplication(),"You didn't make any change",Toast.LENGTH_LONG).show();
                }


                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    editExam(etTitle.getText().toString(),etNote.getText().toString(),etMore.getText().toString());
                }
            }
        });



        toolbarTitle.setText(getString(R.string.edit_exam_info));
        toolbarTitleAr.setText(getString(R.string.edit_exam_info_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_exams_edit_general_info.super.onBackPressed();
            }
        });


       // Toast.makeText(getApplication(),"class: "+id_class+"section "+id_section+"id_category :"+id_category,Toast.LENGTH_LONG).show();

     id_exam=getIntent().getStringExtra(AppConstants.ID_EXAM);
    }





    private void editExam(String title,String note,String more) {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_edit_general_info(new JsonParameters(2,title,note,more,id_exam));
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
        startActivity(new Intent(Activity_exams_edit_general_info.this,Activity_exams_main.class));
      //  super.onBackPressed();
    }


    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("Exam is Edited successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    //Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Activity_exams_edit_general_info.this,Activity_exams_main.class));

                }
                else {
                   // Toast.makeText(getApplication(),"fail",Toast.LENGTH_LONG).show();
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


}
