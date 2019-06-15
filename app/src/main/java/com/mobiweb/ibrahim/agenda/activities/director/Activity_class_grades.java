package com.mobiweb.ibrahim.agenda.activities.director;


/**
 * Created by ibrahim on 2/16/2018.
 */

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;


import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;


public class Activity_class_grades extends ActivityBase {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private WebView wvClassGrades;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_grades);
        init();
    }
    private void init(){
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        wvClassGrades=(WebView)findViewById(R.id.wvClassGrades);

        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Activity_class_grades.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);

        //  toolbarTitle.setText(getIntent().getStringExtra(AppConstants.CLASS_NAME));



        wvClassGrades.getSettings().setLoadWithOverviewMode(true);
        wvClassGrades.getSettings().setUseWideViewPort(true);
        wvClassGrades.getSettings().setBuiltInZoomControls(true);
        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)) {
            toolbarTitle.setText(getString(R.string.grades));
            toolbarTitleAr.setText(getString(R.string.grades));
            wvClassGrades.loadUrl(RetrofitClient.getClient().baseUrl() + "show_student_results.php?id_student="+AppHelper.getStudentId()+"&id_class="+AppHelper.getId_class()+"&id_section="+AppHelper.getId_section());

        }
          else {
            toolbarTitle.setText(getString(R.string.class_grades));
            toolbarTitleAr.setText(getString(R.string.class_grades_ar));
            wvClassGrades.loadUrl(RetrofitClient.getClient().baseUrl() + "show_all_student_results.php?id_exam_category=" + AppHelper.getId_category_exam() + "&id_class=" + AppHelper.getId_class() + "&id_section=" + AppHelper.getId_section());
        }
        // http://mobiweb-software.com/agenda/API3/show_all_student_results.php?id_exam_category=1&id_class=3&id_section=0
       // http://mobiweb-software.com/agenda/API3/show_student_results.php?id_student=11&id_class=3&id_section=0

    }

    @Override
    public void onBackPressed() {

      super.onBackPressed();
    }
}
