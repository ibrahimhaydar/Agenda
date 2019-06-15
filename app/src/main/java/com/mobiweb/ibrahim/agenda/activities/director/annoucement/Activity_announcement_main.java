package com.mobiweb.ibrahim.agenda.activities.director.annoucement;

/**
 * Created by ibrahim on 2/16/2018.
 */

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
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;


public class Activity_announcement_main extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private String id_course,id_class,id_section,id_teacher,class_name;
    private LinearLayout llAddActivity,llViewActivity,llViewDetails;
    private CustomTextViewBold ctvAdd,ctvView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_activities_main);
        init();
    }
    private void init(){
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ctvAdd=(CustomTextViewBold)findViewById(R.id.ctvAdd);
        ctvView=(CustomTextViewBold)findViewById(R.id.ctvView);

        ctvAdd.setText("Add Announcement");
        ctvView.setText("View Announcemnent");

        ivBack=(ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_announcement_main.this,Activity_direction_home.class));
                else
                    Activity_announcement_main.super.onBackPressed();

            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);

        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);


        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

      //  toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));
        toolbarTitle.setText(getString(R.string.announcement));
        toolbarTitleAr.setText(getString(R.string.announcementAr));


        llAddActivity=(LinearLayout)findViewById(R.id.llAddActivity);
        llViewActivity=(LinearLayout)findViewById(R.id.llViewActivities);
        llViewDetails=(LinearLayout)findViewById(R.id.llViewDetails);
        llViewDetails.setVisibility(View.GONE);

        llAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_announcement_main.this,Activity_add_announcement.class);
                startActivity(i);
            }
        });


        llViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_announcement_main.this,Activity_view_announcement.class);
               startActivity(i);
            }
        });



    }

    @Override
    public void onBackPressed() {
        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
            startActivity(new Intent(Activity_announcement_main.this,Activity_direction_home.class));
        else
            super.onBackPressed();
        //super.onBackPressed();
    }
}
