package com.mobiweb.ibrahim.agenda.activities;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

/**
 * Created by ibrahim on 11/25/2017.
 */

public class Activity_main extends ActivityBase {
    private LinearLayout linearAgenda,linearAbout,linearAnnoucement,linearNews;
    private ImageView ivBack,ivRight,ivAbout,ivParents,ivTeachers,ivDirection;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleِAr;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        toolbarTitle.setText(getString(R.string.school_title));
        toolbarTitleِAr.setText(getString(R.string.school_title_ar));

        ivBack.setVisibility(View.GONE);

        ivRight.setImageResource(R.drawable.logout);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        ivRight.setVisibility(View.GONE);
        //  Toast.makeText(getApplication(),getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.PARENT_ID,""),Toast.LENGTH_LONG).show();

    }

    private void init(){


        ivAbout=(ImageView)findViewById(R.id.ivAboutPolygone);
        ivParents=(ImageView)findViewById(R.id.ParentsPolygone);
        ivTeachers=(ImageView)findViewById(R.id.ivTeacherPolygone);
        ivDirection=(ImageView)findViewById(R.id.ivDirectionPolygon);



        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleِAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);


        ivTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_main.this,Activity_login.class);
                i.putExtra(AppConstants.LOGIN_TYPE,AppConstants.LOGIN_TEACHER);
                startActivity(i);
            }
        });

        ivDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_main.this,Activity_login.class);
                i.putExtra(AppConstants.LOGIN_TYPE,AppConstants.LOGIN_DIRECTION);
                startActivity(i);
            }
        });


        ivParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Activity_main.this,Activity_login.class);
                i.putExtra(AppConstants.LOGIN_TYPE,AppConstants.LOGIN_PARENT);
                startActivity(i);
            }
        });

        ivAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_main.this,Activity_about_school.class));
            }
        });


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
                editor.putBoolean(AppConstants.PARENT_SUCCESS, false);
                editor.commit();
                startActivity(new Intent(Activity_main.this,Activity_login.class));
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

    }
}
