package com.mobiweb.ibrahim.agenda.activities.teachers;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.entities.ViewExam;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 5/9/2018.
 */

/**
 * Created by ibrahim on 12/4/2017.
 */

public class Activity_teacher_exams_main extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private String id_course,id_class,id_section,id_teacher,class_name;
    private LinearLayout llAddHw,llDeleteHw,progress;
    private CustomTextViewBold ctvAddEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_exams_main);
        init();
    }
    private void init(){
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        toolbarTitleAr.setVisibility(View.GONE);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ctvAddEdit=(CustomTextViewBold)findViewById(R.id.ctvAddEdit);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_teacher_exams_main.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);


        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_teacher_exams_main.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_teacher_exams_main.this,Activity_teacher.class));
            }
        });


        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);



        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

        toolbarTitle.setText(AppHelper.courseName);


        llAddHw=(LinearLayout)findViewById(R.id.llAddHw);
        llDeleteHw=(LinearLayout)findViewById(R.id.llDeleteHw);




        llDeleteHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  deleteExam();
            }
        });

        retrieveExam();

    }


    private void retrieveExam() {

        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_view_course_exam(new JsonParameters(
                        1,
                        ((Agenda)getApplication()).getLoginId(),
                        AppHelper.getCourseId(),
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        AppHelper.getId_category_exam()

                ));
        call1.enqueue(new Callback<ViewExam>() {
            @Override
            public void onResponse(Call<ViewExam> call, Response<ViewExam> response) {

                try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<ViewExam> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onDataRetrieved(final ViewExam exam){
        progress.setVisibility(View.GONE);
        if(exam.getStatus().matches("empty")){
            ctvAddEdit.setText("Add Exam");
            llAddHw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Activity_teacher_exams_main.this,Activity_teacher_add_exams.class);

                    i.putExtra(AppConstants.ClASS_ID,id_class);
                    i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                    i.putExtra(AppConstants.COURSE_ID,id_course);
                    i.putExtra(AppConstants.CLASS_NAME,getIntent().getStringExtra(AppConstants.CLASS_NAME));
                    startActivity(i);
                }
            });
            llDeleteHw.setVisibility(View.GONE);
        }else {
            ctvAddEdit.setText("Edit Exam");
            llAddHw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Activity_teacher_exams_main.this,Activity_teacher_add_exams.class);

                    i.putExtra(AppConstants.ClASS_ID,id_class);
                    i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                    i.putExtra(AppConstants.COURSE_ID,id_course);
                    i.putExtra(AppConstants.EXAM_DESC,exam.getDesc());
                    i.putExtra(AppConstants.DATE,exam.getDate());
                    i.putExtra(AppConstants.FROM,exam.getFrom());
                    i.putExtra(AppConstants.TO,exam.getTo());
                    i.putExtra(AppConstants.CLASS_NAME,getIntent().getStringExtra(AppConstants.CLASS_NAME));
                    startActivity(i);
                }
            });

            llDeleteHw.setVisibility(View.VISIBLE);
        }
    }



    public void deleteExam() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_delete_course_exam(new JsonParameters(
                        1,
                        ((Agenda)getApplication()).getLoginId(),
                        AppHelper.getCourseId(),
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        AppHelper.getId_category_exam()));
        call1.enqueue(new Callback<JsonAddHw>() {
            @Override
            public void onResponse(Call<JsonAddHw> call, Response<JsonAddHw> response) {

                try {
                    onAgendaDeleted(response.body());
                    // Log.wtf("className", response.body().getAgenda().get(1).getHwDesc());
                } catch (Exception e) {
                    Log.wtf("exception","exception");

                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                call.cancel();

            }
        });
    }


    private void onAgendaDeleted(JsonAddHw status){
        progress.setVisibility(View.GONE);
        if(status.getStatus().equals("success")) {
            Toast.makeText(getApplication(), "Exam is deleted successfully", Toast.LENGTH_LONG).show();
            retrieveExam();


        }
        else
            Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


    }



}
