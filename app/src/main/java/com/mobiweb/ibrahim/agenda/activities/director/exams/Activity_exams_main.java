package com.mobiweb.ibrahim.agenda.activities.director.exams;

/**
 * Created by ibrahim on 2/16/2018.
 */

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.Activity_classes;
import com.mobiweb.ibrahim.agenda.activities.parents.exams.Activity_view_exams_all_details;
import com.mobiweb.ibrahim.agenda.models.json.JsonEditExam;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Activity_exams_main extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private String id_course,id_class,id_section,id_teacher,class_name,id_category,id_user;
    private LinearLayout llAddActivity,llViewActivity,llViewDetails;
    private CustomTextViewBold ctvAdd,ctvView,ctvViewDetails;
    private LinearLayout progress;
    private String id_exam="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_activities_main);
        init();
        retreiveInfo();
    }
    private void init(){
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ctvAdd=(CustomTextViewBold)findViewById(R.id.ctvAdd);
        ctvView=(CustomTextViewBold)findViewById(R.id.ctvView);
        ctvViewDetails=(CustomTextViewBold)findViewById(R.id.ctvViewDetails);

        ctvAdd.setText(getString(R.string.add_exam_info));
        ctvView.setText(getString(R.string.exam_timing_details));
        ctvViewDetails.setText(getString(R.string.view_exam_details));

        ivBack=(ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_exams_main.this, Activity_classes.class);
                i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION_EXAMS);
                startActivity(i);
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_exams_main.this,Activity_direction_home.class));
            }
        });

        id_class=AppHelper.getId_class();
        id_section=AppHelper.getId_section();
        id_category= AppHelper.getId_category_exam();
        id_user=((Agenda)getApplication()).getLoginId();


        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

        //  toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));
        toolbarTitle.setText(getString(R.string.exams));
        toolbarTitleAr.setText(getString(R.string.examsAr));
        llAddActivity=(LinearLayout)findViewById(R.id.llAddActivity);
        llViewActivity=(LinearLayout)findViewById(R.id.llViewActivities);
        llViewDetails=(LinearLayout)findViewById(R.id.llViewDetails);




        llViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_exams_main.this,Activity_exams_edit_details.class);
                i.putExtra(AppConstants.ID_EXAM,id_exam);
                startActivity(i);
            }
        });



        llViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent i=new Intent(Activity_exams_main.this,Activity_exams_view_details.class);
                i.putExtra(AppConstants.ID_EXAM,id_exam);
                startActivity(i);*/

                Intent i = new Intent(Activity_exams_main.this, Activity_view_exams_all_details.class);
                i.putExtra(AppConstants.CLASS_NAME, class_name);
                i.putExtra(AppConstants.ClASS_ID, id_class);
                i.putExtra(AppConstants.ClASS_SECTION_ID, id_section);
                AppHelper.setId_class(id_class);
                AppHelper.setId_section(id_section);
                startActivity(i);
            }
        });

        progress=(LinearLayout)findViewById(R.id.progress);

    }



    private void retreiveInfo() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_view_general_info(new JsonParameters(1,id_user,id_class,id_section,id_category));
        call1.enqueue(new Callback<JsonEditExam>() {
            @Override
            public void onResponse(Call<JsonEditExam> call, Response<JsonEditExam> response) {
                onDataRetrieved(response.body());
            }

            @Override
            public void onFailure(Call<JsonEditExam> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void onDataRetrieved(final JsonEditExam info){
        progress.setVisibility(View.GONE);
        id_exam=info.getIdExam();
       // Toast.makeText(getApplication(),info.getStatus(),Toast.LENGTH_LONG).show();
        if(info.getStatus().matches("success")) {


            ctvAdd.setText(getString(R.string.edit_exam_info));
            llAddActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Activity_exams_main.this,Activity_exams_edit_general_info.class);
                    i.putExtra(AppConstants.TITLE,info.getTitle());
                    i.putExtra(AppConstants.NOTE,info.getNote());
                    i.putExtra(AppConstants.MORE,info.getMore());
                    i.putExtra(AppConstants.ID_EXAM,info.getIdExam());
                    startActivity(i);
                }
            });
            llViewDetails.setVisibility(View.VISIBLE);
            llViewActivity.setVisibility(View.VISIBLE);
        }
        else {
            llViewDetails.setVisibility(View.GONE);
            llViewActivity.setVisibility(View.GONE);
            ctvAdd.setText(getString(R.string.add_exam_info));
            llAddActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Activity_exams_main.this,Activity_exams_add_general_info.class);
                    startActivity(i);
                }
            });

        }

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Activity_exams_main.this, Activity_classes.class);
        i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION_EXAMS);
        startActivity(i);
    }
}
