package com.mobiweb.ibrahim.agenda.activities.parents;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterAllClasses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_class_grades;
import com.mobiweb.ibrahim.agenda.activities.director.exams.Activity_choose_exam_category;
import com.mobiweb.ibrahim.agenda.activities.director.exams.Activity_exams_main;
import com.mobiweb.ibrahim.agenda.activities.director.schedual.Activity_view_schedule;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityAgenda;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityImage;
import com.mobiweb.ibrahim.agenda.activities.parents.exams.Activity_view_exams_all_details;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_add_evaluation;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_attendance;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_evaluation;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.json.JsonGetAllClasses;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_classes extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvClasses;
    private AdapterAllClasses adapterClasses;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private String intentFrom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        init();
        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER))
           retreiveClasses(((Agenda)getApplication()).getLoginId());
        else
            retreiveClasses("");

        toolbarTitle.setText(getString(R.string.classes));
        toolbarTitleAr.setText(getString(R.string.classes_ar));


    }

    private void init() {
        rvClasses = (RecyclerView) findViewById(R.id.rvClasses);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr) findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        setRightIcon();

        rvClasses.setNestedScrollingEnabled(false);
        ivBack.setVisibility(View.GONE);

        try {
            intentFrom=getIntent().getStringExtra(AppConstants.INTENT_FROM);
        }catch (Exception e){
            intentFrom="";
        }
        if(intentFrom==null)
            intentFrom="";

        //if(intentFrom.matches(AppConstants.INTENT_DIRECTION) ){
          //  ivRight.setVisibility(View.GONE);
            ivBack.setVisibility(View.VISIBLE);
            ivBack.setImageResource(R.drawable.back_icon);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(intentFrom.matches(AppConstants.INTENT_DIRECTION_EXAMS)) {
                        Intent i = new Intent(Activity_classes.this, Activity_choose_exam_category.class);
                        i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION_EXAMS);
                        startActivity(i);
                    }else {
                        Activity_classes.super.onBackPressed();
                    }
                }
            });
        //}

    }

    private void logout(){
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.popup_logout);
        btYes=(CustomTextView)logoutDialog.findViewById(R.id.yes);
        btNo=(CustomTextView)logoutDialog.findViewById(R.id.no);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Agenda)getApplication()).logout();
                startActivity(new Intent(Activity_classes.this,Activity_main.class));
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });
        logoutDialog.show();
    }



    private void retreiveClasses(String id_teacher) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getClasses(new JsonParameters(
                        id_teacher,true)
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
        progress.setVisibility(View.GONE);
        adapterClasses=new AdapterAllClasses(AllClasses.getAllclasses(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvClasses.setLayoutManager(glm);
        rvClasses.setAdapter(adapterClasses);

    }

    private void setRightIcon(){
        if(intentFrom.matches(AppConstants.INTENT_DIRECTION)){
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Activity_classes.this,Activity_direction_home.class));

                }
            });


        }

        else if(intentFrom.matches(AppConstants.INTENT_PARENTS)){

            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Activity_classes.this,Activity_direction_home.class));

                }
            });


        }



        else if(intentFrom.matches(AppConstants.INTENT_DIRECTION_EXAMS)){

            ivRight.setVisibility(View.GONE);

        }

        else if(intentFrom.matches(AppConstants.INTENT_PARENTS_EXAMS)){

            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Activity_classes.this,Activity_direction_home.class));

                }
            });

        }


        else {


            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Activity_classes.this,Activity_direction_home.class));

                }
            });

        }
    }


    @Override
    public void onItemClicked(View view, int position) {
        if(intentFrom.matches(AppConstants.INTENT_DIRECTION)){

            if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null){
         /*      if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY).matches(AppConstants.INTENT_DIRECTOR_TACHER_SCHDL)) {
                   Intent i = new Intent(Activity_classes.this, Activity_class_grades.class);
                   AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
                   AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
                   AppHelper.setClass_name(adapterClasses.getclasses().get(position).getClassName());
                   startActivity(i);
               }else*/ if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY).matches(AppConstants.INTENT_DIRECTOR_TACHER_EVALUATION)) {
                   Intent i = new Intent(Activity_classes.this, Activity_teacher.class);
                   AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
                   AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
                   AppHelper.setClass_name(adapterClasses.getclasses().get(position).getClassName());
                   i.putExtra(AppConstants.INTENT_ACTIVITY,AppConstants.INTENT_DIRECTOR_TACHER_EVALUATION);
                   startActivity(i);
               }else if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY).matches(AppConstants.INTENT_DIRECTOR_TACHER_ATTENDANCE)) {
                   Intent i = new Intent(Activity_classes.this, Activity_attendance.class);
                   i.putExtra(AppConstants.INTENT_ACTIVITY,AppConstants.INTENT_DIRECTOR_TACHER_ATTENDANCE);
                   AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
                   AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
                   AppHelper.setClass_name(adapterClasses.getclasses().get(position).getClassName());
                   startActivity(i);
               }else {
                   Intent i = new Intent(Activity_classes.this, Activity_class_grades.class);
                   AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
                   AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
                   AppHelper.setClass_name(adapterClasses.getclasses().get(position).getClassName());
                   startActivity(i);
               }

               }else {

                if(getIntent().getStringExtra(AppConstants.CLASSES_TYPE)!=null){
                    Intent i = new Intent(Activity_classes.this, ActivityAgenda.class);
                    i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
                    i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
                    i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
                    startActivity(i);
                }else {
                    Intent i = new Intent(Activity_classes.this, Activity_view_schedule.class);
                    i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
                    i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
                    i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
                    startActivity(i);
                }


            }




        }

        else if(intentFrom.matches(AppConstants.INTENT_PARENTS) || intentFrom.matches(AppConstants.INTENT_TEACHER)){

            Intent i = new Intent(Activity_classes.this, ActivityImage.class);
            i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_PARENTS);
            i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
            i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
            i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
            startActivity(i);


        }



        else if(intentFrom.matches(AppConstants.INTENT_DIRECTION_EXAMS) || intentFrom.matches(AppConstants.INTENT_TEACHER_EXAMS)  ){

            Intent i = new Intent(Activity_classes.this, Activity_exams_main.class);
            i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
            i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
            i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
            AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
            AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
            startActivity(i);

       }

        else if(intentFrom.matches(AppConstants.INTENT_PARENTS_EXAMS)){

            Intent i = new Intent(Activity_classes.this, Activity_view_exams_all_details.class);
            i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
            i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
            i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
            AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
            AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
            startActivity(i);

        }


        else {


            Intent i = new Intent(Activity_classes.this, ActivityAgenda.class);
            i.putExtra(AppConstants.CLASS_NAME, adapterClasses.getclasses().get(position).getClassName());
            i.putExtra(AppConstants.ClASS_ID, adapterClasses.getclasses().get(position).getIdClass());
            i.putExtra(AppConstants.ClASS_SECTION_ID, adapterClasses.getclasses().get(position).getIdSection());
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if(intentFrom.matches(AppConstants.INTENT_DIRECTION_EXAMS)) {
            Intent i = new Intent(Activity_classes.this, Activity_choose_exam_category.class);
            i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION_EXAMS);
            startActivity(i);
       }else {
            super.onBackPressed();
       }



    }
}
