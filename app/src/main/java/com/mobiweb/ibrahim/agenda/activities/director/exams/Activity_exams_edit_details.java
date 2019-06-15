package com.mobiweb.ibrahim.agenda.activities.director.exams;

/**
 * Created by ibrahim on 5/2/2018.
 */


import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterAddExamDetails;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterCourses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextFocusListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextOnKeyListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemCourseClickListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnLongItemClickListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.SpinnerOnSelection;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.models.entities.Courses;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.entities.ScheduleExamPost;
import com.mobiweb.ibrahim.agenda.models.json.JsonAllExamsDetails;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonScheduleDetails;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 4/16/2018.
 */

public class Activity_exams_edit_details extends ActivityBase implements RVOnItemClickListener,SpinnerOnSelection,RVOnLongItemClickListener,EditTextOnKeyListener,EditTextFocusListener,RVOnItemCourseClickListener {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight,ivAddMore;
    private LinearLayout progress;
    private ArrayList<ScheduleExamPost> arrayDetails=new ArrayList<>();
    private RecyclerView rvDetais;
    private AdapterAddExamDetails adapterAddExamDetails;
    private String id_class_exam="";
    private Activity activity;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate;
    boolean spinnerFirstTime=true;
    private Dialog deleteRowDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private CustomTextView ctvDialog;
    private CustomTextViewAr ctvDialoAr;
    private Button btAdd;

    private Dialog responseDialog,coursesDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;
    private RecyclerView rvCourses;
    private ArrayList<Courses> arrayCourses=new ArrayList<>();
    private AdapterCourses adapterCourses;
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_add_details);
        init();
        retrieveDetails();
    }

    private void init(){
        progress=(LinearLayout)findViewById(R.id.progress);
        id_class_exam=getIntent().getStringExtra(AppConstants.ID_EXAM);
        activity=this;
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        btAdd=(Button)findViewById(R.id.btAdd);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_exams_edit_details.this,Activity_exams_main.class));
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_exams_edit_details.this, Activity_direction_home.class));

            }
        });
        toolbarTitle.setText(getString(R.string.exam_timing_details));
        toolbarTitleAr.setText(getString(R.string.exam_timing_details_ar));
        ivAddMore=(ImageView)findViewById(R.id.ivAdd);
        ivAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        rvDetais=(RecyclerView)findViewById(R.id.rvDetails);
        rvDetais.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                adapterAddExamDetails.setUserIsInteracting(false);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        setDetails();
        checkToAdd();


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean dayOffError=false;
                boolean emptyCourse=false;
                boolean emptyDate=false;
                boolean emptyTimeFrom=false;
                boolean emptyTimeTo=false;
                adapterAddExamDetails.notifyDataSetChanged();
                adapterAddExamDetails.setUserIsInteracting(false);
                for (int i=0;i<adapterAddExamDetails.getdetails().size();i++){

                  if(adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_EXAM) && adapterAddExamDetails.getdetails().get(i).getTitle().isEmpty())
                      emptyCourse=true;
                  if(adapterAddExamDetails.getdetails().get(i).getDate().isEmpty())
                      emptyDate=true;
                    if(!adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_OFF) && !adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_REVISION)&& (adapterAddExamDetails.getdetails().get(i).getFrom().isEmpty() || adapterAddExamDetails.getdetails().get(i).getFrom().matches("00:00:00")))
                        emptyTimeFrom=true;
                    if(!adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_OFF) && !adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_REVISION) && (adapterAddExamDetails.getdetails().get(i).getTo().isEmpty() || adapterAddExamDetails.getdetails().get(i).getTo().matches("00:00:00")))
                        emptyTimeTo=true;


                        if(adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_OFF) || adapterAddExamDetails.getdetails().get(i).getType().matches(AppConstants.TYPE_REVISION)){

                       for (int j=0;j<adapterAddExamDetails.getdetails().size();j++) {
                         Log.wtf("details info","date 1 is "+adapterAddExamDetails.getdetails().get(i).getDate()+" and date 2 is "+adapterAddExamDetails.getdetails().get(j).getDate()+" and type search "+ adapterAddExamDetails.getdetails().get(j).getType() +" i is " +i+" and j is "+j+"\n");

                           DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                           Date d1 = dateFormat.parse(adapterAddExamDetails.getdetails().get(i).getDate(), new ParsePosition(0));
                           Date d2 = dateFormat.parse(adapterAddExamDetails.getdetails().get(j).getDate(), new ParsePosition(0));
                          try {
                              if(i!=j & d1.compareTo(d2)==0) {
                                  dayOffError = true;

                              }
                          }catch (Exception e){
                              emptyDate=true;
                          }



                       }
                    }
                }

                if(emptyDate)
                    Toast.makeText(getApplication(),"Please check empty date(s)",Toast.LENGTH_LONG).show();
                else  if(emptyCourse)
                    Toast.makeText(getApplication(),"Please check empty Subject(s)",Toast.LENGTH_LONG).show();
                else  if(emptyTimeFrom)
                    Toast.makeText(getApplication(),"Please check empty Time From(s)",Toast.LENGTH_LONG).show();
                else  if(emptyTimeTo)
                    Toast.makeText(getApplication(),"Please check empty Time To(s)",Toast.LENGTH_LONG).show();
                else if(dayOffError)
                    Toast.makeText(getApplication(),"you have been entered exams with an off date",Toast.LENGTH_LONG).show();
                else
                    addExam();






            }
        });

    }


    private void checkToAdd(){
 /*       if(adapterAddExamDetails.getdetails().size()==0)
            btAdd.setVisibility(View.GONE);
        else*/
            btAdd.setVisibility(View.VISIBLE);
    }

    private void addRow(){

        ScheduleExamPost detail=new ScheduleExamPost();
        detail.setId_class_exam(id_class_exam);
        arrayDetails.add(detail);
        adapterAddExamDetails.setUserIsInteracting(false);
        adapterAddExamDetails.notifyDataSetChanged();
        rvDetais.scrollToPosition(adapterAddExamDetails.getdetails().size()-1);
        spinnerFirstTime=true;
        checkToAdd();
    }


    private void setDetails(){
        adapterAddExamDetails=new AdapterAddExamDetails(arrayDetails,this,this,this,this,this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvDetais.setLayoutManager(glm);
        rvDetais.setAdapter(adapterAddExamDetails);
    }

    @Override
    public void onItemClicked(View view, int position) {

        switch (view.getId()){
            case R.id.ivPickDate:
                showDatePicker(position);
                break;
            case R.id.ivPickTimeFrom:
                showTimePicker(position,"from");
                break;
            case R.id.ivPickTimeTo:
                showTimePicker(position,"to");
                break;
            case R.id.ctvCourseTitle:
                if(adapterAddExamDetails.getdetails().get(position).getType().matches(AppConstants.TYPE_EXAM))
                    popupCourses(position);
                break;
        }
    }



    private void showDatePicker(final int position){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        adapterAddExamDetails.setUserIsInteracting(false);
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                        Date date = new Date(year, monthOfYear, dayOfMonth-1);
                        String dayOfWeek = simpledateformat.format(date);
                        //selectedDate=year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                        selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));

                        // adapterAddExamDetails.getdetails().get(position).setDate(dayOfWeek +" "+(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) +" "+MONTHS[monthOfYear]+" "+year);
                        adapterAddExamDetails.getdetails().get(position).setDate(selectedDate);
                        adapterAddExamDetails.notifyItemChanged(position);
                        //    ctvdate.setText((dayOfMonth<10?(dayOfMonth):(dayOfMonth))+" "+  (monthOfYear+1<10?(monthOfYear+1):(monthOfYear+1)) + " "+ year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void showTimePicker(final int position, final String type){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        adapterAddExamDetails.setUserIsInteracting(false);


                        String timeMinutes=minute+"";
                        String timeHours=hourOfDay+"";
                        if(timeMinutes.length()==1)
                            timeMinutes="0"+timeMinutes;
                        if(timeHours.length()==1)
                            timeHours="0"+timeHours;



                        if(type.matches("from")) {
                            adapterAddExamDetails.getdetails().get(position).setFrom(timeHours + ":" + timeMinutes);
                            adapterAddExamDetails.notifyItemChanged(position);
                        }else {
                            adapterAddExamDetails.getdetails().get(position).setTo(timeHours + ":" + timeMinutes);
                            adapterAddExamDetails.notifyItemChanged(position);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    @Override
    public void onSpinnerSelected(View view, int layoutPosition, int SpinnerPosition) {

        // Toast.makeText(getApplication(),"lp"+layoutPosition+"sp"+SpinnerPosition,Toast.LENGTH_LONG).show();

       // adapterAddExamDetails.setCheck(0);
        switch (SpinnerPosition) {
            case 0:
                adapterAddExamDetails.getdetails().get(layoutPosition).setType(AppConstants.TYPE_EXAM);
                if(adapterAddExamDetails.isUserIsInteracting())
                    adapterAddExamDetails.getdetails().get(layoutPosition).setTitle("");
                adapterAddExamDetails.notifyItemChanged(layoutPosition);
                break;

            case 1:
                adapterAddExamDetails.getdetails().get(layoutPosition).setType(AppConstants.TYPE_BREAK);
                adapterAddExamDetails.getdetails().get(layoutPosition).setTitle("Break");
                adapterAddExamDetails.notifyItemChanged(layoutPosition);
                break;

            case 2:
                adapterAddExamDetails.getdetails().get(layoutPosition).setType(AppConstants.TYPE_OFF);
                adapterAddExamDetails.getdetails().get(layoutPosition).setTitle("عطلة");
                adapterAddExamDetails.notifyItemChanged(layoutPosition);
                break;
            case 3:
                adapterAddExamDetails.getdetails().get(layoutPosition).setType(AppConstants.TYPE_REVISION);
                adapterAddExamDetails.getdetails().get(layoutPosition).setTitle("مراجعة في البيت");
                adapterAddExamDetails.notifyItemChanged(layoutPosition);
                break;
        }




    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        adapterAddExamDetails.setUserIsInteracting(true);
    }

    @Override
    public void onLongItemClicked(View view, int position) {
        showPopUpToDelete(position);
    }


    private void showPopUpToDelete(final int position){
        deleteRowDialog = new Dialog(this);
        deleteRowDialog.setContentView(R.layout.popup_logout);
        btYes=(CustomTextView)deleteRowDialog.findViewById(R.id.yes);
        btNo=(CustomTextView)deleteRowDialog.findViewById(R.id.no);
        ctvDialog=(CustomTextView)deleteRowDialog.findViewById(R.id.ctvDialog);
        ctvDialoAr=(CustomTextViewAr)deleteRowDialog.findViewById(R.id.ctvDialogAr);

        ctvDialog.setText("Are you sure you want delete row?");
        ctvDialoAr.setVisibility(View.GONE);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapterAddExamDetails.getdetails().remove(position);
                adapterAddExamDetails.notifyItemRemoved(position);
                deleteRowDialog.dismiss();
                checkToAdd();
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRowDialog.dismiss();
            }
        });



        deleteRowDialog.show();

    }



    private void addExam() {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_edit_exam_schedule(new JsonParameters(
                       ((Agenda)getApplication()).getLoginId(),
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        AppHelper.getId_category_exam(),
                        id_class_exam,
                        adapterAddExamDetails.getdetails())


                );
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


    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("Details are added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    //Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Activity_exams_edit_details.this,Activity_exams_main.class));

                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }











    private void popupCourses(int position){

        coursesDialog = new Dialog(this);
        coursesDialog.setContentView(R.layout.popup_courses);
        rvCourses=(RecyclerView) coursesDialog.findViewById(R.id.rvCourses);
       // coursesDialog.setCanceledOnTouchOutside(false);
        adapterCourses=new AdapterCourses(arrayCourses,this,position);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvCourses.setAdapter(adapterCourses);
        rvCourses.setLayoutManager(glm);
        coursesDialog.show();
    }











    @Override
    public void EtOnKeyListener(int position,String text) {
        //   Toast.makeText(getApplication(),text,Toast.LENGTH_LONG).show();
        try {
            adapterAddExamDetails.getdetails().get(position).setTitle(text);
        }catch (Exception e){}

    }

    @Override
    public void onFocusListener(View view, int position) {
        //  Toast.makeText(getApplication(),"asdsad",Toast.LENGTH_LONG).show();
        rvDetais.scrollToPosition(position+1);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_exams_edit_details.this,Activity_exams_main.class));
    }


    private void retrieveDetails(){

        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_view_exam_all_details(new JsonParameters(
                        3,
                        ((Agenda)getApplication()).getLoginId(),
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        AppHelper.getId_category_exam(),
                        "false"
                ));
        call1.enqueue(new Callback<JsonAllExamsDetails>() {
            @Override
            public void onResponse(Call<JsonAllExamsDetails> call, Response<JsonAllExamsDetails> response) {

                try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonAllExamsDetails> call, Throwable t) {
                call.cancel();
            }
        });
    }
    private void onDataRetrieved(JsonAllExamsDetails details) {
        progress.setVisibility(View.GONE);
        arrayDetails=new ArrayList<>();
        for (int i=0;i<details.getSchedulesExamsDetails().size();i++){
            for (int j=0;j<details.getSchedulesExamsDetails().get(i).getInfo().size();j++)
            {
                ScheduleExamPost scheduleExamPost=new ScheduleExamPost(
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).getDate(),
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).getFrom(),
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).getTo(),
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).getTitle(),
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).getType(),
                        "1",
                        details.getSchedulesExamsDetails().get(i).getInfo().get(j).isTeacherAdd(),
                        id_class_exam
                );
                arrayDetails.add(scheduleExamPost);
            }

        }

         setDetails();
        arrayCourses=details.getCourses();

    }

    @Override
    public void onItemClicked(View view, int position, int rowPosition) {

        adapterAddExamDetails.getdetails().get(rowPosition).setTitle(adapterCourses.getcourses().get(position).getCourse_name());
        adapterAddExamDetails.notifyDataSetChanged();
        adapterAddExamDetails.setUserIsInteracting(false);
        coursesDialog.dismiss();
    }
}
