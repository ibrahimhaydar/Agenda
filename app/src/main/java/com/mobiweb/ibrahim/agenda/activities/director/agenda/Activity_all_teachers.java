package com.mobiweb.ibrahim.agenda.activities.director.agenda;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterAllTeachers;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.director.schedual.Activity_view_schedule_teacher;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.json.JsonAllTeachers;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 12/12/2017.
 */

public class Activity_all_teachers extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvTeachers;
    private AdapterAllTeachers adapterAllTeachers;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_teachers);
        init();
        retreiveClasses();
        toolbarTitle.setText(getString(R.string.teachers));
        toolbarTitleAr.setText(getString(R.string.teachers_ar));
       // ivBack.setVisibility(View.GONE);
    }

    private void init() {
        rvTeachers = (RecyclerView) findViewById(R.id.rvTeachers);
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
        ivRight.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_all_teachers.this,Activity_direction_home.class));
              // Activity_all_teachers.super.onBackPressed();
            }
        });

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
                startActivity(new Intent(Activity_all_teachers.this,Activity_main.class));
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



    private void retreiveClasses() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getTeachers(new JsonParameters(getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,""),1));
        call1.enqueue(new Callback<JsonAllTeachers>() {
            @Override
            public void onResponse(Call<JsonAllTeachers> call, Response<JsonAllTeachers> response) {

                try {
                    onDataRetrieved(response.body());
                   // Log.wtf("className", response.body().getAllteachers().get(1).getClassName());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonAllTeachers> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonAllTeachers Allteachers){
        progress.setVisibility(View.GONE);
        adapterAllTeachers=new AdapterAllTeachers(Allteachers.getAllteachers(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvTeachers.setLayoutManager(glm);
        rvTeachers.setAdapter(adapterAllTeachers);

    }


    @Override
    public void onItemClicked(View view, int position) {
if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null) {
    Intent i = new Intent(Activity_all_teachers.this, Activity_view_schedule_teacher.class);
    AppHelper.setTeacherId(adapterAllTeachers.getteachers().get(position).getIdTeacher());
    AppHelper.setTeacher_name(adapterAllTeachers.getteachers().get(position).getTeacherName());
    startActivity(i);
}else {
    Intent i = new Intent(Activity_all_teachers.this, Activity_teacher.class);
    i.putExtra(AppConstants.TEACHER_ID, adapterAllTeachers.getteachers().get(position).getIdTeacher());
    ((Agenda) getApplication()).setLoginId(adapterAllTeachers.getteachers().get(position).getIdTeacher());
    startActivity(i);
}
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_all_teachers.this,Activity_direction_home.class));
       // super.onBackPressed();
    }

}
