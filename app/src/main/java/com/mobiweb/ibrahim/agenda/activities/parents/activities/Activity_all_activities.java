package com.mobiweb.ibrahim.agenda.activities.parents.activities;

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

import com.mobiweb.ibrahim.agenda.Adapters.AdapterActivities;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.models.json.JsonActivities;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
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

public class Activity_all_activities extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvActivities;
    private AdapterActivities adapterActivities;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_activities);
        init();
        retreiveClasses();
        toolbarTitle.setText(getString(R.string.agenda));
        ivBack.setVisibility(View.GONE);
    }

    private void init() {
        rvActivities = (RecyclerView) findViewById(R.id.rvActivities);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.logout);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();

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
                startActivity(new Intent(Activity_all_activities.this,Activity_main.class));
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
                .getActivities(new JsonParameters(getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,""),1));
        call1.enqueue(new Callback<JsonActivities>() {
            @Override
            public void onResponse(Call<JsonActivities> call, Response<JsonActivities> response) {

                try {
                    onDataRetrieved(response.body());
                    // Log.wtf("className", response.body().getAllteachers().get(1).getClassName());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonActivities> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonActivities Allteachers){
        progress.setVisibility(View.GONE);
        adapterActivities=new AdapterActivities(Allteachers.getActivities(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvActivities.setLayoutManager(glm);
        rvActivities.setAdapter(adapterActivities);

    }


    @Override
    public void onItemClicked(View view, int position) {
        Intent i=new Intent(Activity_all_activities.this,Activity_inside_activities.class);

        i.putExtra(AppConstants.HW_TITLE,adapterActivities.getactivities().get(position).getTitle());
        i.putExtra(AppConstants.HW_DESC,adapterActivities.getactivities().get(position).getDescription());
        i.putExtra(AppConstants.DATE,adapterActivities.getactivities().get(position).getActivity_date());
        ArrayList<String> arrayImages=new ArrayList<String>();
        for (int j=0;j<adapterActivities.getactivities().get(position).getImages().size();j++)
            arrayImages.add(adapterActivities.getactivities().get(position).getImages().get(j).getImageName());
        i.putStringArrayListExtra(AppConstants.HW_IMAGE, arrayImages);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
