package com.mobiweb.ibrahim.agenda.activities.director.activities;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_add_activities extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private EditText edHwTitle,etHwDesc,etDate;
    private Button btAdd;
    private Activity activity;
    private CustomTextView ctvDialogMessage;
    private LinearLayout linearPickDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate;


    private Dialog responseDialog;
    private Button btOk;
@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activites);
        init();
        toolbarTitle.setText(getString(R.string.add_activity));
        toolbarTitleAr.setText(getString(R.string.add_activity_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_add_activities.super.onBackPressed();
            }
        });

    }


    private void init() {

        linearPickDate=(LinearLayout)findViewById(R.id.linearPickDate);
        linearPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDatePicker();
            }
        });
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);


        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_add_activities.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        edHwTitle=(EditText)findViewById(R.id.etTitle);
      //  edHwTitle.setText(AppHelper.getCourseName());

        etDate=(EditText)findViewById(R.id.etDate);
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty() || etDate.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please check empty fields ",Toast.LENGTH_LONG).show();
                }

                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    addActivity(edHwTitle.getText().toString(),etHwDesc.getText().toString(),etDate.getText().toString());
                }
            }
        });





    }

    private void showDatePicker(){
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

                        // adapterAddExamDetails.getdetails().get(position).setDate(dayOfWeek +" "+(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) +" "+MONTHS[monthOfYear]+" "+year);
                        etDate.setText(selectedDate);
                        //    ctvdate.setText((dayOfMonth<10?(dayOfMonth):(dayOfMonth))+" "+  (monthOfYear+1<10?(monthOfYear+1):(monthOfYear+1)) + " "+ year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void addActivity(String title,String description,String date) {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addActivity(new JsonParameters(3,"1",title,description,date));
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
            ctvDialogMessage.setText("added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                   // Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Activity_add_activities.this,Activity_activities_main.class));

                }
                else {
                    responseDialog.dismiss();
                 //   Toast.makeText(getApplication(),"fail",Toast.LENGTH_LONG).show();
                    //responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


}
