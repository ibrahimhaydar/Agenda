package com.mobiweb.ibrahim.agenda.activities.teachers;


import com.mobiweb.ibrahim.agenda.Adapters.AdapterAddEvaluation;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterEvaluation;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextOnKeyListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.Student;
import com.mobiweb.ibrahim.agenda.models.entities.PostEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonClassStudents;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonResponse;
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

public class Activity_edit_evaluation extends ActivityBase implements EditTextOnKeyListener {
    private RecyclerView rvEvaluations;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private AdapterAddEvaluation adapterAddEvaluation;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate="";
    private ImageView ivDate;
    private CustomTextViewBold ctvdate;
    public static final String[] MONTHS = {"كانون الثاني", "شباط", "آذار", "نيسان", "أيار", "حزيران", "تموز", "آب", "أيلول", "تشرين الأول", "تشرين الثاني", "كانون الأول"};
    private Button btAddEdit,btRemove;
    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;



    private RecyclerView rvStudents;
    private  LinearLayout linearProgressDialog;
    private Activity activity;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evaluation);
        // Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.TEACHER_ID),Toast.LENGTH_LONG).show();
        init();


        toolbarTitle.setText(getString(R.string.evaluation));
        toolbarTitleAr.setText(getString(R.string.evaluation_ar));

        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_edit_evaluation.this,Activity_evaluation.class));
                finish();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_edit_evaluation.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_edit_evaluation.this,Activity_teacher.class));
              finish();
            }
        });



        retreiveClassStudents();





        btAddEdit.setText("Edit Evaluation");
        btRemove.setVisibility(View.VISIBLE);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvaluation();
            }
        });




    }

    private void init(){
        rvEvaluations=(RecyclerView)findViewById(R.id.rvEvaluation);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        progress=(LinearLayout)findViewById(R.id.progress);
        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        btAddEdit=(Button)findViewById(R.id.btAddEdit);
        btRemove=(Button)findViewById(R.id.btRemove);

        activity=this;
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterAddEvaluation.notifyDataSetChanged();
                if(selectedDate.isEmpty())
                    Toast.makeText(getApplication(),"Please pick a date",Toast.LENGTH_LONG).show();
                else if(checkEvaluationArray()){
                    Toast.makeText(getApplication(),"Please add evaluation",Toast.LENGTH_LONG).show();
                }else
                    editEvaluation(getPostEvaluations());
            }
        });
      selectedDate=AppHelper.getSelectedDate();
      ctvdate.setText(selectedDate);
    }


    public void retreiveClassStudents() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .get_class_evaluation(new JsonParameters(
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        selectedDate,
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        AppHelper.getCourseId(),
                        3

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

        adapterAddEvaluation =new AdapterAddEvaluation(classStudents.getStudents(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvEvaluations.setAdapter(adapterAddEvaluation);
        rvEvaluations.setLayoutManager(glm);



    }


    private void deleteEvaluation(){

        final Dialog deletEvaluationDialog = new Dialog(this);
        deletEvaluationDialog.setContentView(R.layout.popup_logout);

        CustomTextView ctvDialog=(CustomTextView)deletEvaluationDialog.findViewById(R.id.ctvDialog);
        CustomTextViewAr ctvDialogAr=(CustomTextViewAr)deletEvaluationDialog.findViewById(R.id.ctvDialogAr);
        ctvDialog.setText("Are you sure you want to delete evaluation on all class?");
        ctvDialogAr.setVisibility(View.GONE);

        CustomTextView btYes=(CustomTextView)deletEvaluationDialog.findViewById(R.id.yes);
        CustomTextView btNo=(CustomTextView)deletEvaluationDialog.findViewById(R.id.no);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deletEvaluationDialog.dismiss();
               editEvaluation(getPostEvaluationsDelete());
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletEvaluationDialog.dismiss();
            }
        });



        deletEvaluationDialog.show();
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
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                        startActivity(new Intent(Activity_edit_evaluation.this,Activity_evaluation.class));
                    else
                        startActivity(new Intent(Activity_edit_evaluation.this,Activity_evaluation.class));
                   finish();
                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }



    private boolean checkEvaluationArray(){
        boolean evaluationEmpty=true;
        try{
            for(int i=0;i<adapterAddEvaluation.getinfoStudents().size();i++){
                if(!adapterAddEvaluation.getinfoStudents().get(i).getInfo().isEmpty())
                    evaluationEmpty=false;
            }}catch (Exception e) {
            evaluationEmpty=true;
        }
        return evaluationEmpty;
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
                        ctvdate.setText(AppHelper.getArabicDay(dayOfWeek) + " " + (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + " " + MONTHS[monthOfYear] + " " + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }




    public void editEvaluation(ArrayList<PostEvaluation> arrayEvaluation) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .editEvaluation(new JsonParameters(1,
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        selectedDate,
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        AppHelper.courseId,
                        arrayEvaluation

                ));
        call1.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {

                try {
                    onEvaluationAdded(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private ArrayList<PostEvaluation> getPostEvaluations(){
        ArrayList<PostEvaluation> arrayPostEvaluation=new ArrayList<>();
        PostEvaluation p;
        for(int i=0;i<adapterAddEvaluation.getinfoStudents().size();i++){

                p=new PostEvaluation(adapterAddEvaluation.getinfoStudents().get(i).getIdStudent(),adapterAddEvaluation.getinfoStudents().get(i).getInfo(),adapterAddEvaluation.getinfoStudents().get(i).getId_evaluation());
                arrayPostEvaluation.add(p);


        }
        return arrayPostEvaluation;
    }


    private ArrayList<PostEvaluation> getPostEvaluationsDelete(){
        ArrayList<PostEvaluation> arrayPostEvaluation=new ArrayList<>();
        PostEvaluation p;
        for(int i=0;i<adapterAddEvaluation.getinfoStudents().size();i++){

            p=new PostEvaluation(adapterAddEvaluation.getinfoStudents().get(i).getIdStudent(),"",adapterAddEvaluation.getinfoStudents().get(i).getId_evaluation());
            arrayPostEvaluation.add(p);


        }
        return arrayPostEvaluation;
    }


    private void onEvaluationAdded(JsonResponse jsonResponse){
        progress.setVisibility(View.GONE);
        if(jsonResponse.getStatus().matches("success")){
            popUpMessage(true);
        }else {
            popUpMessage(false);
        }

    }








    @Override
    public void onBackPressed() {

            startActivity(new Intent(Activity_edit_evaluation.this,Activity_evaluation.class));
            finish();

    }

    @Override
    public void EtOnKeyListener(int position, String text) {

        adapterAddEvaluation.getinfoStudents().get(position).setInfo(text);
     //   adapterAddEvaluation.notifyItemChanged(position);

    }
}