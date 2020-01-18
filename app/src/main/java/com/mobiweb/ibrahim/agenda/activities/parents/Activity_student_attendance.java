package com.mobiweb.ibrahim.agenda.activities.parents;



import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterStudentAttendance;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_activities_main;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_add_activity_images;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_edit_activities;
import com.mobiweb.ibrahim.agenda.activities.director.activities.Activity_edit_activity_images;
import com.mobiweb.ibrahim.agenda.activities.parents.activities.Activity_inside_activities;
import com.mobiweb.ibrahim.agenda.models.entities.Attendance;
import com.mobiweb.ibrahim.agenda.models.json.JsonAttendance;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 12/12/2017.
 */

public class Activity_student_attendance extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvAttendance;
    private AdapterStudentAttendance adapterActivities;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private LinearLayout loadingPaging;
    private CustomTextViewBoldAr toolbarTitleAr;

    private boolean isLoading = false;
    private int pageNumber = 1;
    private final int PAGE_SIZE = 20;
    private ArrayList<Attendance> arrayAttendance =new ArrayList<>();
    private CustomTextViewAr tvAbsent,tvPresent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        init();
        setPagination();
        retreiveAttendance(pageNumber,PAGE_SIZE);
        toolbarTitle.setText(getString(R.string.attendance));
        toolbarTitleAr.setText(getString(R.string.attendance_ar));

    }

    private void init() {
        rvAttendance = (RecyclerView) findViewById(R.id.rvAttendance);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_student_attendance.this,Activity_direction_home.class));

            }
        });
        loadingPaging=(LinearLayout)findViewById(R.id.loading_paging);
        ivRight.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Activity_student_attendance.super.onBackPressed();
            }
        });

        tvAbsent=(CustomTextViewAr) findViewById(R.id.tvAbsent);
        tvPresent=(CustomTextViewAr) findViewById(R.id.tvPresent);
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
                startActivity(new Intent(Activity_student_attendance.this,Activity_main.class));
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



    private void retreiveAttendance(int page, int pageSize) {
        if (adapterActivities != null) {
            rvAttendance.scrollToPosition(adapterActivities.getItemCount() - 1);
            loadingPaging.setVisibility(View.VISIBLE);
        }

        else
            progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .get_student_attendance(new JsonParameters(
                        1,
                        AppHelper.getStudentId(),
                        page+"",
                        pageSize+""

                ));
        call1.enqueue(new Callback<JsonAttendance>() {
            @Override
            public void onResponse(Call<JsonAttendance> call, Response<JsonAttendance> response) {

                try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonAttendance> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonAttendance attendance){

        progress.setVisibility(View.GONE);
        loadingPaging.setVisibility(View.GONE);
        arrayAttendance =attendance.getAttendance();
        if (adapterActivities == null) {
            if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                adapterActivities = new AdapterStudentAttendance(arrayAttendance, this);
            else
                adapterActivities = new AdapterStudentAttendance(arrayAttendance, this);
            GridLayoutManager glm = new GridLayoutManager(this, 1);
            rvAttendance.setLayoutManager(glm);
            rvAttendance.setAdapter(adapterActivities);
            try{tvPresent.setText(attendance.getPresent_count());}catch (Exception e){}
            try{tvAbsent.setText(attendance.getAbsence_count());}catch (Exception e){}
        }else {
            int position = adapterActivities.getattendances().size();
            adapterActivities.getattendances().addAll(arrayAttendance);
            adapterActivities.notifyItemInserted(position);
        }

        isLoading = attendance.getAttendance().isEmpty();

    }


    @Override
    public void onItemClicked(View view, int position) {

    }




    private void setPagination(){
        rvAttendance.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    if(!isLoading  && (rvAttendance.getLayoutManager().getChildCount()
                            + ((LinearLayoutManager) rvAttendance.getLayoutManager()).findFirstVisibleItemPosition()
                            >= rvAttendance.getLayoutManager().getItemCount())) {
                        isLoading = true;
                        pageNumber ++;
                        retreiveAttendance(pageNumber, PAGE_SIZE);
                    }
                }
            }
        });

    }







    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

}
