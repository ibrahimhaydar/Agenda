package com.mobiweb.ibrahim.agenda.activities.director;

/**
 * Created by ibrahim on 2/16/2018.
 */

import com.mobiweb.ibrahim.agenda.Adapters.AdapterChilds;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterAllTeachers;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_activities_main;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_view_activities;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.activities.director.annoucement.Activity_announcement_main;
import com.mobiweb.ibrahim.agenda.activities.director.annoucement.Activity_view_announcement;
import com.mobiweb.ibrahim.agenda.activities.director.exams.Activity_choose_exam_category;
import com.mobiweb.ibrahim.agenda.activities.director.schedual.Activity_view_schedule;
import com.mobiweb.ibrahim.agenda.activities.director.videos.Activity_video_main;
import com.mobiweb.ibrahim.agenda.activities.director.videos.Activity_view_videos;
import com.mobiweb.ibrahim.agenda.activities.parents.Activity_classes;
import com.mobiweb.ibrahim.agenda.activities.parents.Activity_student_attendance;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityAgenda;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityImage;
import com.mobiweb.ibrahim.agenda.activities.teachers.ActivityImageTeacher;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_evaluation;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.entities.User;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;

import java.util.Locale;


/**
 * Created by ibrahim on 12/12/2017.
 */

public class Activity_direction_home extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvTeachers;
    private AdapterAllTeachers adapterAllTeachers;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private CardView cardAgenda,cardActivities,cardExams,cardAnnoucement,cardVideo,cardSchedule,cardTeacherSchedule,cardGrades,cardEvaluation,cardAttendance;
    private LinearLayout linearChilds;
    private ImageView ivArrowDown;
    private RecyclerView rvChilds;
    private AdapterChilds adapterChilds;
    private CustomTextViewBold ctvSelectedChild;
    private CustomTextViewBoldAr ctvSelectedChildAr;
    private String SelectedChildId,selectedChildClass;
    private boolean isRvChildOpen=false;
    private User cachedUser;
    private LinearLayout linearcardSchedualTeacher,linearCardEvaluation,linearCardAttendance,linearCardGrades;
    private CustomTextViewBold ctvAgenda;
    private CustomTextViewBoldAr ctvAgendaAr;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_home);
        init();
        toolbarTitle.setText(getString(R.string.school_title));
        toolbarTitleAr.setText(getString(R.string.school_title_ar));
        ivBack.setVisibility(View.GONE);
    }

    private void init() {
        ctvAgenda=(CustomTextViewBold)findViewById(R.id.ctvAgenda);
        ctvAgendaAr=(CustomTextViewBoldAr)findViewById(R.id.ctvAgendaAr);
        linearChilds=(LinearLayout)findViewById(R.id.linearChilds) ;
        ivArrowDown=(ImageView)findViewById(R.id.ivArrowDown);
        rvChilds=(RecyclerView)findViewById(R.id.rvChilds);
        ctvSelectedChild=(CustomTextViewBold) findViewById(R.id.ctvSelectedChild);
        ctvSelectedChildAr=(CustomTextViewBoldAr) findViewById(R.id.ctvSelectedChildAr);
        ctvSelectedChildAr.setVisibility(View.GONE);
        ctvSelectedChild.setVisibility(View.VISIBLE);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.logout);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

            }
        });

        cardAgenda=(CardView)findViewById(R.id.cardAgenda);
        cardActivities=(CardView)findViewById(R.id.cardActivities);
        cardAnnoucement=(CardView)findViewById(R.id.cardAnnouncement);
        cardExams=(CardView)findViewById(R.id.cardExams);
        cardVideo=(CardView)findViewById(R.id.cardVideo);
        cardSchedule=(CardView)findViewById(R.id.cardSchedual);
        cardTeacherSchedule=(CardView)findViewById(R.id.cardSchedualTeacher);
        cardGrades=(CardView)findViewById(R.id.cardGrades);
        cardEvaluation=(CardView)findViewById(R.id.cardEvaluation);
        cardAttendance=(CardView)findViewById(R.id.cardAttendance);

        linearcardSchedualTeacher=(LinearLayout)findViewById(R.id.linearcardSchedualTeacher);
        linearCardAttendance=(LinearLayout)findViewById(R.id.linearCardAttendance);
        linearCardEvaluation=(LinearLayout)findViewById(R.id.linearCardEvaluation);
        linearCardGrades=(LinearLayout)findViewById(R.id.linearCardGrades);





        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)){
            linearCardEvaluation.setVisibility(View.GONE);
            linearCardAttendance.setVisibility(View.GONE);
            ctvAgenda.setText(getText(R.string.classes));
            ctvAgendaAr.setText(getText(R.string.classes_ar));
            AppHelper.setMargins(getApplicationContext(),linearCardGrades,18,10,18,40);




        } else if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER)){
            linearCardEvaluation.setVisibility(View.GONE);
            linearCardAttendance.setVisibility(View.GONE);
            ctvAgenda.setText(getText(R.string.classes));
            ctvAgendaAr.setText(getText(R.string.classes_ar));
            AppHelper.setMargins(getApplicationContext(),linearCardGrades,18,10,18,40);



        }else{
            linearcardSchedualTeacher.setVisibility(View.GONE);
            linearCardEvaluation.setVisibility(View.VISIBLE);
            linearCardAttendance.setVisibility(View.VISIBLE);
            cardAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Activity_direction_home.this,Activity_student_attendance.class));
                }
            });

        }









        cardAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                  startActivity(new Intent(Activity_direction_home.this, Activity_all_teachers.class));
               else if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER))
                   startActivity(new Intent(Activity_direction_home.this, Activity_teacher.class));
                else{
                   Intent i = new Intent(Activity_direction_home.this, ActivityAgenda.class);
                   i.putExtra(AppConstants.CLASS_NAME, selectedChildClass);
                   i.putExtra(AppConstants.ClASS_ID, AppHelper.getId_class());
                   i.putExtra(AppConstants.ClASS_SECTION_ID, AppHelper.getId_section());
                   startActivity(i);
               }

            }
        });

        cardActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                startActivity(new Intent(Activity_direction_home.this, Activity_activities_main.class));
              else
                startActivity(new Intent(Activity_direction_home.this, Activity_view_activities.class));

            }
        });

        cardAnnoucement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                startActivity(new Intent(Activity_direction_home.this, Activity_announcement_main.class));
              else
                startActivity(new Intent(Activity_direction_home.this, Activity_view_announcement.class));
            }
        });

        cardExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_choose_exam_category.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION_EXAMS);
                    startActivity(i);
                }else if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_choose_exam_category.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_PARENTS_EXAMS);
                    startActivity(i);
                }else{
                    Intent i = new Intent(Activity_direction_home.this, Activity_choose_exam_category.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_TEACHER_EXAMS);
                    startActivity(i);
                }

            }
        });

        cardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                   startActivity(new Intent(Activity_direction_home.this, Activity_video_main.class));
                else
                    startActivity(new Intent(Activity_direction_home.this, Activity_view_videos.class));

            }
        });

        cardSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_classes.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION);
                    startActivity(i);
                }
                else   if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_classes.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_TEACHER);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Activity_direction_home.this, ActivityImage.class);
                    i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_PARENTS);
                    i.putExtra(AppConstants.CLASS_NAME, selectedChildClass);
                    i.putExtra(AppConstants.ClASS_ID, AppHelper.getId_class());
                    i.putExtra(AppConstants.ClASS_SECTION_ID,AppHelper.getId_section());
                    startActivity(i);
                }
            }
        });


        cardTeacherSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_all_teachers.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION);
                    i.putExtra(AppConstants.INTENT_ACTIVITY,AppConstants.INTENT_DIRECTOR_TACHER_SCHDL);
                    startActivity(i);
                }else if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER)) {

                    Intent i = new Intent(Activity_direction_home.this, ActivityImageTeacher.class);
                    startActivity(i);
                }
            }
        });



        cardGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION) || ((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER)) {
                    Intent i = new Intent(Activity_direction_home.this, Activity_choose_exam_category.class);
                    i.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_DIRECTION);
                    i.putExtra(AppConstants.INTENT_ACTIVITY, AppConstants.INTENT_DIRECTOR_GRADES);
                    startActivity(i);
                }else  {
                    Intent i = new Intent(Activity_direction_home.this, Activity_class_grades.class);
                    startActivity(i);
                }
            }
        });


        cardEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(Activity_direction_home.this, Activity_evaluation.class);
                    startActivity(i);

            }
        });




        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)) {
            setChildsList();
            checkStudentPermissions();
        }else {
            cachedUser=((Agenda)getApplication()).getCashedUser();
            ivArrowDown.setVisibility(View.GONE);
            ctvSelectedChild.setText(((Agenda)getApplication()).getCashedUsername());
            ctvSelectedChildAr.setText(((Agenda)getApplication()).getCashedUsername());
        }
    }



    private void checkStudentPermissions(){
        if(cachedUser.getEnable_attendance().matches("1"))
            linearCardAttendance.setVisibility(View.VISIBLE);
        else
            linearCardAttendance.setVisibility(View.GONE);

        if(cachedUser.getEnable_evaluation().matches("1"))
            linearCardEvaluation.setVisibility(View.VISIBLE);
        else
            linearCardEvaluation.setVisibility(View.GONE);

        if(cachedUser.getEnable_grade().matches("1"))
            linearCardGrades.setVisibility(View.VISIBLE);
        else
            linearCardGrades.setVisibility(View.GONE);
    }



    private void setChildsList(){
         cachedUser=((Agenda)getApplication()).getCashedUser();
        if(AppHelper.isProbablyArabic(cachedUser.getChilds().get(0).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName())) {
            ctvSelectedChildAr.setVisibility(View.VISIBLE);
            ctvSelectedChild.setVisibility(View.GONE);

        }else {
            ctvSelectedChildAr.setVisibility(View.GONE);
            ctvSelectedChild.setVisibility(View.VISIBLE);
        }


        if(cachedUser.getChilds().size()>1){
            rvChilds.setVisibility(View.GONE);


            int position = 0;
            for(int i=0;i<cachedUser.getChilds().size();i++){
                if(cachedUser.getChilds().get(i).getIdChild()==AppHelper.getStudentId())
                    position=i;
            }
            ctvSelectedChild.setText(cachedUser.getChilds().get(position).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());
            ctvSelectedChildAr.setText(cachedUser.getChilds().get(position).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());

            SelectedChildId = cachedUser.getChilds().get(position).getIdChild();
            selectedChildClass=cachedUser.getChilds().get(position).getClassName();
            AppHelper.setId_class(cachedUser.getChilds().get(position).getIdClass());
            AppHelper.setId_section(cachedUser.getChilds().get(position).getIdSection());
            AppHelper.setStudentId(SelectedChildId);
            AppHelper.setClass_name(selectedChildClass);

            ivArrowDown.setVisibility(View.VISIBLE);
            adapterChilds=new AdapterChilds(cachedUser.getChilds(),this);
            GridLayoutManager glm=new GridLayoutManager(this,1);
            rvChilds.setAdapter(adapterChilds);
            rvChilds.setLayoutManager(glm);
            ctvSelectedChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isRvChildOpen){
                        rvChilds.setVisibility(View.GONE);
                        isRvChildOpen=false;
                    }else {
                        rvChilds.setVisibility(View.VISIBLE);
                        isRvChildOpen=true;
                    }

                }
            });


            ctvSelectedChildAr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isRvChildOpen){
                        rvChilds.setVisibility(View.GONE);
                        isRvChildOpen=false;
                    }else {
                        rvChilds.setVisibility(View.VISIBLE);
                        isRvChildOpen=true;
                    }

                }
            });

        }else if(cachedUser.getChilds().size()==1) {
            rvChilds.setVisibility(View.GONE);

           if(AppHelper.getStudentId()!=null){
               int position = 0;
               for(int i=0;i<cachedUser.getChilds().size();i++){
                   if(cachedUser.getChilds().get(i).getIdChild()==AppHelper.getStudentId())
                       position=i;
               }
               ctvSelectedChild.setText(cachedUser.getChilds().get(position).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());
               ctvSelectedChildAr.setText(cachedUser.getChilds().get(position).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());

               SelectedChildId = cachedUser.getChilds().get(position).getIdChild();
               selectedChildClass=cachedUser.getChilds().get(position).getClassName();
               AppHelper.setId_class(cachedUser.getChilds().get(position).getIdClass());
               AppHelper.setId_section(cachedUser.getChilds().get(position).getIdSection());
               AppHelper.setClass_name(cachedUser.getChilds().get(position).getClassName());

           }else {
               ctvSelectedChild.setText(cachedUser.getChilds().get(0).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());
               ctvSelectedChildAr.setText(cachedUser.getChilds().get(0).getChildName() + " " + cachedUser.getFatherName() + " " + cachedUser.getFatherLastName());

               SelectedChildId = cachedUser.getChilds().get(0).getIdChild();
               selectedChildClass=cachedUser.getChilds().get(0).getClassName();
               AppHelper.setId_class(cachedUser.getChilds().get(0).getIdClass());
               AppHelper.setId_section(cachedUser.getChilds().get(0).getIdSection());
               AppHelper.setClass_name(cachedUser.getChilds().get(0).getClassName());
           }

            AppHelper.setStudentId(SelectedChildId);
            ivArrowDown.setVisibility(View.GONE);

        }
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
                startActivity(new Intent(Activity_direction_home.this,Activity_main.class));
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







    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onItemClicked(View view, int position) {
        SelectedChildId=adapterChilds.getChilds().get(position).getIdChild();
        selectedChildClass=cachedUser.getChilds().get(position).getClassName();

        AppHelper.setStudentId(SelectedChildId);
        AppHelper.setClass_name(selectedChildClass);
        AppHelper.setId_class(cachedUser.getChilds().get(position).getIdClass());
        AppHelper.setId_section(cachedUser.getChilds().get(position).getIdSection());
        rvChilds.setVisibility(View.GONE);
        ctvSelectedChild.setText(adapterChilds.getChilds().get(position).getChildName() +" "+ cachedUser.getFatherName() +" "+ cachedUser.getFatherLastName());
        ctvSelectedChildAr.setText(adapterChilds.getChilds().get(position).getChildName() +" "+ cachedUser.getFatherName() +" "+ cachedUser.getFatherLastName());

    }
}
