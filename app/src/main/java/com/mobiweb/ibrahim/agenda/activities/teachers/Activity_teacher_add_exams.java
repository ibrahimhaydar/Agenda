package com.mobiweb.ibrahim.agenda.activities.teachers;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Agenda;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_teacher_add_exams extends ActivityBase {
    private CustomTextViewBold toolbarTitle,addImage;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private EditText edHwTitle,etHwDesc;
    private Button btAdd;

    private Activity activity;
    private String id_course,id_class,id_section,id_teacher;
    private ImageView ivPickerFrom,ivPickerTo,ivDatepicker;
    private CustomTextView ctvPickedDate,ctvPickedFrom,ctvPickedTo;

    private Dialog responseDialog;
    private Button btOk;
    private boolean isEdit=false;
    private CustomTextView ctvDialogMessage;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate="0000:00:00";
    private String selectedFrom="00:00:00";
    private String selectedTo="00:00:00";


   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_exams);
        init();
        if(isEdit){
           toolbarTitle.setText(getString(R.string.edit_exam_info));
           toolbarTitleAr.setText(getString(R.string.edit_exam_info_ar));
        }else {
            toolbarTitle.setText(getString(R.string.add_exam_info));
            toolbarTitleAr.setText(getString(R.string.add_exam_info_ar));
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_teacher_add_exams.super.onBackPressed();
            }
        });

    }


    private void init() {


        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);

        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);


        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_teacher_add_exams.this,Activity_direction_home.class));
                else
                    startActivity(new Intent(Activity_teacher_add_exams.this,Activity_teacher.class));
            }
        });






        edHwTitle=(EditText)findViewById(R.id.etTitle);
       // edHwTitle.setEnabled(false);
        edHwTitle.setText(AppHelper.getCourseName());
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);
        try {
            etHwDesc.setText(getIntent().getStringExtra(AppConstants.EXAM_DESC));
        }catch (Exception e){}

        if(getIntent().getStringExtra(AppConstants.EXAM_DESC)!=null){
            isEdit=true;
            btAdd.setText("Edit");
        }
        try{
            if(getIntent().getStringExtra(AppConstants.DATE)!=null) {
                selectedDate = getIntent().getStringExtra(AppConstants.DATE);
                ctvPickedDate.setText(getIntent().getStringExtra(AppConstants.DATE));
            }else {
                selectedDate="0000:00:00";
            }
        }catch (Exception e){
            selectedDate="0000:00:00";
        }

       try{
            if(getIntent().getStringExtra(AppConstants.FROM)!=null) {
                selectedFrom = getIntent().getStringExtra(AppConstants.FROM);
                ctvPickedFrom.setText(getIntent().getStringExtra(AppConstants.FROM));
            }else {
                selectedFrom="00:00:00";
            }
        }catch (Exception e){
           selectedFrom="00:00:00";
       }


        try{
            if(getIntent().getStringExtra(AppConstants.TO)!=null){
            selectedTo=getIntent().getStringExtra(AppConstants.TO);
            ctvPickedTo.setText(getIntent().getStringExtra(AppConstants.TO));}else{
                selectedTo="00:00:00";
            }
        }catch (Exception e){
            selectedTo="00:00:00";
        }



        ivPickerFrom=(ImageView)findViewById(R.id.ivFrom);
        ivPickerTo=(ImageView)findViewById(R.id.ivTo);
        ivDatepicker=(ImageView)findViewById(R.id.ivPickDate);

        ctvPickedDate=(CustomTextView)findViewById(R.id.ctvPickedDate);
        ctvPickedFrom=(CustomTextView)findViewById(R.id.ctvPickedFrom);
        ctvPickedTo=(CustomTextView)findViewById(R.id.ctvPickedTo);

        ivPickerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker("from");
            }
        });

        ivPickerTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker("to");
            }
        });


        ivDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


        if(getIntent().getStringExtra(AppConstants.DATE)!=null && getIntent().getStringExtra(AppConstants.DATE)!="0000:00:00"){
            ctvPickedDate.setText(getIntent().getStringExtra(AppConstants.DATE));
            selectedDate=getIntent().getStringExtra(AppConstants.DATE);
        }

        if(getIntent().getStringExtra(AppConstants.FROM)!=null && getIntent().getStringExtra(AppConstants.FROM)!="00:00:00"){
           if(getIntent().getStringExtra(AppConstants.FROM).length()==8)
               ctvPickedFrom.setText(getIntent().getStringExtra(AppConstants.FROM).substring(0,getIntent().getStringExtra(AppConstants.FROM).length()-3));
           else
               ctvPickedFrom.setText(getIntent().getStringExtra(AppConstants.FROM));
            selectedFrom=getIntent().getStringExtra(AppConstants.FROM);

        }

        if(getIntent().getStringExtra(AppConstants.TO)!=null && getIntent().getStringExtra(AppConstants.TO)!="00:00:00"){
           if(getIntent().getStringExtra(AppConstants.TO).length()==8)
               ctvPickedTo.setText(getIntent().getStringExtra(AppConstants.TO).substring(0,getIntent().getStringExtra(AppConstants.TO).length()-3));
           else
               ctvPickedTo.setText(getIntent().getStringExtra(AppConstants.TO));
            selectedTo=getIntent().getStringExtra(AppConstants.TO);
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill title and desc",Toast.LENGTH_LONG).show();
                }
   /*            else if(!selectedDate.matches("0000:00:00") && (selectedFrom.matches("00:00:00") || selectedTo.matches("00:00:00")))
                    Toast.makeText(getApplication(),"Please set time",Toast.LENGTH_LONG).show();
                else if(selectedDate.matches("0000:00:00") && (!selectedFrom.matches("00:00:00") || !selectedTo.matches("00:00:00")))
                    Toast.makeText(getApplication(),"Please set date",Toast.LENGTH_LONG).show();
              */
                else if(selectedDate.matches("0000:00:00"))
                    Toast.makeText(getApplication(),"Please set date",Toast.LENGTH_LONG).show();
                else if(selectedFrom.matches("00:00:00"))
                    Toast.makeText(getApplication(),"Please set Time",Toast.LENGTH_LONG).show();
                else if(selectedTo.matches("00:00:00"))
                    Toast.makeText(getApplication(),"Please set Time",Toast.LENGTH_LONG).show();

                else {
                    addExam(edHwTitle.getText().toString(),etHwDesc.getText().toString());
                }
            }
        });

    }



    private void addExam(String title,String description) {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_add_course_exam(new JsonParameters(
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        AppHelper.getCourseId(),
                        AppHelper.getId_category_exam(),
                        ((Agenda)getApplication()).getLoginId(),
                        title,
                        description,
                        selectedDate,
                        selectedFrom,
                        selectedTo,
                        "1",
                        1




                ));
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getResult().equals("success")){
                            popUpMessage(true,response.body().getMessage());

                        }
                        else
                            popUpMessage(false,response.body().getMessage());
                    }catch (Exception e){
                        popUpMessage(false,"");

                    }

                } else {
                    popUpMessage(false,"");
                }




            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
    }



    private void popUpMessage(final boolean isSuccess,String message){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            if(!isEdit)
               ctvDialogMessage.setText("Added successfully");
            else
                ctvDialogMessage.setText("Edited successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again." +message);
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                        startActivity(new Intent(Activity_teacher_add_exams.this,Activity_direction_home.class));
                    else
                        startActivity(new Intent(Activity_teacher_add_exams.this,Activity_teacher.class));

                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
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
                        ctvPickedDate.setText(selectedDate);
                        //    ctvdate.setText((dayOfMonth<10?(dayOfMonth):(dayOfMonth))+" "+  (monthOfYear+1<10?(monthOfYear+1):(monthOfYear+1)) + " "+ year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void showTimePicker(final String type){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
String timeMinutes=minute+"";
String timeHours=hourOfDay+"";
                        if(timeMinutes.length()==1)
                            timeMinutes="0"+timeMinutes;
                        if(timeHours.length()==1)
                            timeHours="0"+timeHours;

                        if(type.matches("from")) {

                            selectedFrom=timeHours + ":" + minute;
                            ctvPickedFrom.setText(timeHours + ":" + timeMinutes);

                        }else {
                            selectedTo=timeHours + ":" + minute;
                            ctvPickedTo.setText(timeHours + ":" + timeMinutes);
                        }
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }



}
