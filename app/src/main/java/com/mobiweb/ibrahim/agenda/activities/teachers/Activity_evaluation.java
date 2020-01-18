package com.mobiweb.ibrahim.agenda.activities.teachers;


import com.mobiweb.ibrahim.agenda.Adapters.AdapterCourses;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterEvaluation;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterStudent;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterTeacherCourses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.Student;
import com.mobiweb.ibrahim.agenda.models.json.JsonClassStudents;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/13/2017.
 */

public class Activity_evaluation extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvEvaluations;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private AdapterEvaluation adapterEvaluation;
    private boolean isFilterOpen=false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate;

    private Dialog studentsDialog,filterDialog;



    private CustomTextView ctvSelectedFilter, ctvFilterAll,ctvFilterStudent,ctvFilterDate;



    private RecyclerView rvStudents;
    private  LinearLayout linearProgressDialog;
    private Activity activity;
    private AdapterStudent adapterStudent;
    private LinearLayout linearAdd;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        // Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.TEACHER_ID),Toast.LENGTH_LONG).show();
        init();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_evaluation.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);




        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)){
                    startActivity(new Intent(Activity_evaluation.this, Activity_direction_home.class));
                    finish();
                }

                else {
                    openFilterDialog();
                  /*  if(isFilterOpen) {
                        cardFilter.setVisibility(View.GONE);
                        isFilterOpen=false;
                    }else {
                        cardFilter.setVisibility(View.VISIBLE);
                        isFilterOpen=true;
                    }*/
                }

            }
        });
        toolbarTitle.setText(getString(R.string.evaluation));
        toolbarTitleAr.setText(getString(R.string.evaluation_ar));

       if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)){
           linearAdd.setVisibility(View.GONE);
           ctvSelectedFilter.setVisibility(View.VISIBLE);
           ivRight.setImageResource(R.drawable.home);
           retreiveEvaluation(AppConstants.FILTER_STUDENT_PARENT,AppHelper.getStudentId());
       }else {
           ivRight.setImageResource(R.drawable.filter_white);
           ctvSelectedFilter.setVisibility(View.GONE);
           linearAdd.setVisibility(View.VISIBLE);
           retreiveEvaluation(AppConstants.FILTER_ALL,"");
       }



    }

    private void init(){
        rvEvaluations=(RecyclerView)findViewById(R.id.rvEvaluation);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        progress=(LinearLayout)findViewById(R.id.progress);
        linearAdd=(LinearLayout) findViewById(R.id.linearAdd);
        linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_evaluation.this,Activity_add_evaluation.class));
                finish();
            }
        });

        activity=this;




        ctvSelectedFilter=(CustomTextView)findViewById(R.id.ctvSelectedFilter);


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
                        retreiveEvaluation(AppConstants.FILTER_DATE,selectedDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }








    public void retreiveEvaluation(String filterid,String filterInfo) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getEvaluations(new JsonParameters(1,
                                                AppHelper.getId_class(),
                                                AppHelper.getId_section(),
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        filterid,
                        filterInfo,
                        AppHelper.getCourseId()


                        ));
        call1.enqueue(new Callback<JsonEvaluation>() {
            @Override
            public void onResponse(Call<JsonEvaluation> call, Response<JsonEvaluation> response) {

                try {
                    onDataRetrieved(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonEvaluation> call, Throwable t) {
                call.cancel();
            }
        });
    }



    private void onDataRetrieved(JsonEvaluation evaluations){
        progress.setVisibility(View.GONE);
        adapterEvaluation=new AdapterEvaluation(evaluations.getEvaluations(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvEvaluations.setAdapter(adapterEvaluation);
        rvEvaluations.setLayoutManager(glm);

    }








    public void retreiveClassStudents() {
        linearProgressDialog.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .get_class_students(new JsonParameters(4,
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        "",
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType()

                ));
        call1.enqueue(new Callback<JsonClassStudents>() {
            @Override
            public void onResponse(Call<JsonClassStudents> call, Response<JsonClassStudents> response) {

                try {
                    onStudentsRetreived(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonClassStudents> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onStudentsRetreived(JsonClassStudents classStudents){
        progress.setVisibility(View.GONE);
        linearProgressDialog.setVisibility(View.GONE);
        adapterStudent=new AdapterStudent(classStudents.getStudents(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvStudents.setAdapter(adapterStudent);
        rvStudents.setLayoutManager(glm);



    }


    private void popupStudents(){

        studentsDialog = new Dialog(this);
        studentsDialog.setContentView(R.layout.popup_choose_student);
        rvStudents=(RecyclerView) studentsDialog.findViewById(R.id.rvStudents);
        linearProgressDialog=(LinearLayout)studentsDialog.findViewById(R.id.linearProgressDialog);
        linearProgressDialog.setVisibility(View.VISIBLE);
        studentsDialog.show();
        retreiveClassStudents();
        // studentsDialog.setCanceledOnTouchOutside(false);

    }



    private void openFilterDialog(){

        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.popup_filter_avaluation);
        rvStudents=(RecyclerView) filterDialog.findViewById(R.id.rvStudents);
        ctvFilterAll=(CustomTextView)filterDialog.findViewById(R.id.ctvFilterAll);
        ctvFilterStudent=(CustomTextView)filterDialog.findViewById(R.id.ctvFilterStudent);
        ctvFilterDate=(CustomTextView)filterDialog.findViewById(R.id.ctvFilterDate);

        ctvFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
                isFilterOpen=false;
                ctvSelectedFilter.setText("ALL");
                retreiveEvaluation(AppConstants.FILTER_ALL,"");
            }
        });

        ctvFilterStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
                isFilterOpen=false;
                ctvSelectedFilter.setText("Student");
                popupStudents();
            }
        });

        ctvFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
                isFilterOpen=false;
                ctvSelectedFilter.setText("Date");
                showDatePicker();
            }
        });

        filterDialog.show();

        // studentsDialog.setCanceledOnTouchOutside(false);

    }



    @Override
    public void onItemClicked(View view, int position) {

        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
            if (view.getId() == R.id.idStudent) {
                studentsDialog.dismiss();
                retreiveEvaluation(AppConstants.FILTER_STUDENT, adapterStudent.getinfoStudents().get(position).getIdStudent());
            } else {
                AppHelper.setSelectedDate(adapterEvaluation.getevaluations().get(position).getDate());
                AppHelper.setArrayEvaluationInfo(adapterEvaluation.getevaluations().get(position).getInfoStudent());
                startActivity(new Intent(Activity_evaluation.this, Activity_edit_evaluation.class));
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();

    }
}
