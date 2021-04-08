package com.mobiweb.ibrahim.agenda.activities.teachers;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.activities.director.exams.Activity_choose_exam_category;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityAgenda;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

/**
 * Created by ibrahim on 12/4/2017.
 */

public class Activity_Teacher_Homeworks extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private String id_course,id_class,id_section,id_teacher,class_name;
    private LinearLayout llAddHw,llEditHw,llViewHw,llExams,llEvaluation,llAttendance,llGrade;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_homework);
        init();
    }
    private void init(){
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        toolbarTitleAr.setVisibility(View.GONE);
        ivBack=(ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_Teacher_Homeworks.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);


        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_Teacher_Homeworks.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_Teacher_Homeworks.this,Activity_teacher.class));
            }
        });


        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);


        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

        toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));

        llAddHw=(LinearLayout)findViewById(R.id.llAddHw);
        llEditHw=(LinearLayout)findViewById(R.id.llEditHw);
        llViewHw=(LinearLayout)findViewById(R.id.llViewHw);
        llExams=(LinearLayout)findViewById(R.id.llExams);
        llEvaluation=(LinearLayout)findViewById(R.id.llEvaluation);
        llAttendance=(LinearLayout)findViewById(R.id.llAttendance);
        llGrade=(LinearLayout)findViewById(R.id.llGrades);

        llAddHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_add_homework_files.class);

                i.putExtra(AppConstants.ClASS_ID,id_class);
                i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                i.putExtra(AppConstants.COURSE_ID,id_course);
                i.putExtra(AppConstants.CLASS_NAME,getIntent().getStringExtra(AppConstants.CLASS_NAME));
                startActivity(i);
            }
        });


        llEditHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_edit_homework.class);

                i.putExtra(AppConstants.ClASS_ID,id_class);
                i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                i.putExtra(AppConstants.COURSE_ID,id_course);
                i.putExtra(AppConstants.TEACHER_ID,id_teacher);
                i.putExtra(AppConstants.CLASS_NAME,getIntent().getStringExtra(AppConstants.CLASS_NAME));
                startActivity(i);
            }
        });



        llViewHw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(Activity_Teacher_Homeworks.this,ActivityAgenda.class);
                i.putExtra(AppConstants.CLASS_NAME,toolbarTitle.getText());
                i.putExtra(AppConstants.ClASS_ID,id_class);
                i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                startActivity(i);
            }
        });


        llExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_choose_exam_category.class);
                i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_TEACHER);
                i.putExtra(AppConstants.EXAM_FROM,AppConstants.EXAM_FROM_SCHEDULE_EXAM);

                startActivity(i);
            }
        });



        llEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_evaluation.class);
                i.putExtra(AppConstants.ClASS_ID,id_class);
                i.putExtra(AppConstants.ClASS_SECTION_ID,id_section);
                i.putExtra(AppConstants.COURSE_ID,id_course);
                startActivity(i);
            }
        });



        llAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_attendance.class);
                startActivity(i);
            }
        });

        llGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_Teacher_Homeworks.this,Activity_choose_exam_category.class);
                i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_TEACHER);
                i.putExtra(AppConstants.EXAM_FROM,AppConstants.EXAM_FROM_GRADES);
                startActivity(i);
            }
        });

    }
}