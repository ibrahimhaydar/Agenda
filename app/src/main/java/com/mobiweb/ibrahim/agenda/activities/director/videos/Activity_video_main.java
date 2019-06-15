package com.mobiweb.ibrahim.agenda.activities.director.videos;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;


public class Activity_video_main extends ActivityBase {
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

        ctvAdd.setText("Add Video");
        ctvView.setText("View Videos");

        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_video_main.this, Activity_direction_home.class));
            }
        });

        ivRight.setVisibility(View.VISIBLE);

        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_course=getIntent().getStringExtra(AppConstants.COURSE_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);


        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

        //  toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));
        toolbarTitle.setText(getString(R.string.videos));
        toolbarTitleAr.setText(getString(R.string.videosAr));
        llAddActivity=(LinearLayout)findViewById(R.id.llAddActivity);
        llViewActivity=(LinearLayout)findViewById(R.id.llViewActivities);
        llViewDetails=(LinearLayout)findViewById(R.id.llViewDetails);
        llViewDetails.setVisibility(View.GONE);

        llAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_video_main.this,Activity_add_videos.class);
                startActivity(i);
            }
        });


        llViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_video_main.this,Activity_view_videos.class);
                startActivity(i);
            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_video_main.this, Activity_direction_home.class));
    }
}
