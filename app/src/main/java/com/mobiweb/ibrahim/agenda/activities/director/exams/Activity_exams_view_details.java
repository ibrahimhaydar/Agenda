package com.mobiweb.ibrahim.agenda.activities.director.exams;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterDates;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterScheduleDetails;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonScheduleDetails;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 4/25/2018.
 */

public class Activity_exams_view_details extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvInfo;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private AdapterDates adapterDates;
    private AdapterScheduleDetails adapterScheduleDetails;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_view_details);
        init();
        retrieveDetails();
    }

    private void init(){
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);

        ivBack=(ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_exams_view_details.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_exams_view_details.this,Activity_direction_home.class));
            }
        });

        toolbarTitle.setText(getString(R.string.exams));
        toolbarTitleAr.setText(getString(R.string.examsAr));

        rvInfo=(RecyclerView)findViewById(R.id.rvDetails);
    }

    private void retrieveDetails(){

            progress.setVisibility(View.VISIBLE);
            Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                    .exams_view_exam_schedule(new JsonParameters(
                             1,
                            getIntent().getStringExtra(AppConstants.ID_EXAM)
                    ));
            call1.enqueue(new Callback<JsonScheduleDetails>() {
                @Override
                public void onResponse(Call<JsonScheduleDetails> call, Response<JsonScheduleDetails> response) {

                    try {
                        onDataRetrieved(response.body());
                    } catch (Exception e) {
                        Log.wtf("exception","exception");
                    }
                }

                @Override
                public void onFailure(Call<JsonScheduleDetails> call, Throwable t) {
                    call.cancel();
                }
            });
        }

    private void onDataRetrieved(JsonScheduleDetails details) {
        progress.setVisibility(View.GONE);
        setDates(details);

    }


    private void setDates(JsonScheduleDetails details) {
        adapterDates=new AdapterDates(details.getSchedulesExamsDetails(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvInfo.setLayoutManager(glm);
        rvInfo.setAdapter(adapterDates);
    }



    @Override
    public void onItemClicked(View view, int position) {

    }
}
