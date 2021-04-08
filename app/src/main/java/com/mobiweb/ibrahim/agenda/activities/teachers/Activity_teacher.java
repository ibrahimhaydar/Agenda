package com.mobiweb.ibrahim.agenda.activities.teachers;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonTeacherCourses;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/13/2017.
 */

public class Activity_teacher extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvCourses;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private AdapterTeacherCourses adapterTeacherCourses;
    private String teacherId="";
    private String class_id="-1";
    private String section_id="-1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
       // Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.TEACHER_ID),Toast.LENGTH_LONG).show();
        init();
        ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_teacher.super.onBackPressed();
            }
        });
        ivRight.setImageResource(R.drawable.logout);
        toolbarTitle.setText(getString(R.string.courses));
        toolbarTitleAr.setText(getString(R.string.courses_ar));


       // if(((Agenda)getApplication()).getCashedType().equals(AppConstants.LOGIN_DIRECTION)) {
            ivRight.setVisibility(View.GONE);
            ivBack.setVisibility(View.VISIBLE);
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((Agenda)getApplication()).getCashedType().equals(AppConstants.LOGIN_DIRECTION))
                       Activity_teacher.super.onBackPressed();
                    else
                       startActivity(new Intent(Activity_teacher.this,Activity_direction_home.class));
                }
            });
/*        }else {
            ivRight.setImageResource(R.drawable.logout);
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logout();

                }
            });
            ivRight.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.GONE);

        }*/


        retreiveCourses();
    }

private void init(){
    rvCourses=(RecyclerView)findViewById(R.id.rvTeacherCourses);
    toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
    toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
    ivBack=(ImageView)findViewById(R.id.ivBack);
    ivRight=(ImageView)findViewById(R.id.ivRight);
    progress=(LinearLayout)findViewById(R.id.progress);

    if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null){
        try{
        if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY).matches(AppConstants.INTENT_DIRECTOR_TACHER_EVALUATION)) {
            teacherId="-1";
            class_id=AppHelper.getId_class();
            section_id=AppHelper.getId_section();
        }else {
           teacherId= getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
           class_id="-1";
           section_id="-1";
        }}catch (Exception e){
            teacherId= getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
            class_id="-1";
            section_id="-1";
        }
    }else {
        teacherId= getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");
        class_id="-1";
        section_id="-1";
    }

    }


    private void logout(){

        logoutDialog = new Dialog(this);
        logoutDialog.setContentView(R.layout.popup_logout);
        btYes=(CustomTextView)logoutDialog.findViewById(R.id.yes);
        btNo=(CustomTextView)logoutDialog.findViewById(R.id.no);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
                editor.putBoolean(AppConstants.TEACHER_SUCCESS, false);
                editor.commit();
                ((Agenda)getApplication()).logout();
                startActivity(new Intent(Activity_teacher.this,Activity_main.class));
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




    public void retreiveCourses() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getCourses(new JsonParameters(2,teacherId,class_id,section_id));
        call1.enqueue(new Callback<JsonTeacherCourses>() {
            @Override
            public void onResponse(Call<JsonTeacherCourses> call, Response<JsonTeacherCourses> response) {

                try {
                    onDataRetrieved(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonTeacherCourses> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onDataRetrieved(JsonTeacherCourses courses){
        progress.setVisibility(View.GONE);
        adapterTeacherCourses=new AdapterTeacherCourses(courses.getCourses(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvCourses.setLayoutManager(glm);
        rvCourses.setAdapter(adapterTeacherCourses);

    }


    @Override
    public void onItemClicked(View view, int position) {

        if(!teacherId.matches("-1")) {
            AppHelper.setCourseName(adapterTeacherCourses.getcourses().get(position).getNameCourse());
            Intent i = new Intent(Activity_teacher.this, Activity_teacher_classes.class);
            AppHelper.setCourseId(adapterTeacherCourses.getcourses().get(position).getIdCourse());
            i.putExtra(AppConstants.COURSE_ID, adapterTeacherCourses.getcourses().get(position).getIdCourse());
            startActivity(i);
        }else {
            AppHelper.setCourseName(adapterTeacherCourses.getcourses().get(position).getNameCourse());
            Intent i = new Intent(Activity_teacher.this, Activity_evaluation.class);
            AppHelper.setCourseId(adapterTeacherCourses.getcourses().get(position).getIdCourse());
            i.putExtra(AppConstants.COURSE_ID, adapterTeacherCourses.getcourses().get(position).getIdCourse());
            i.putExtra(AppConstants.INTENT_ACTIVITY,AppConstants.INTENT_DIRECTOR_TACHER_EVALUATION);
            startActivity(i);

        }
    }

    @Override
    public void onBackPressed() {
        if(((Agenda)getApplication()).getCashedType().equals(AppConstants.LOGIN_DIRECTION))
            super.onBackPressed();
        else
            startActivity(new Intent(Activity_teacher.this,Activity_direction_home.class));

    }
}
