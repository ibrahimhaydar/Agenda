package com.mobiweb.ibrahim.agenda.activities.director.activities;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
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

import com.mobiweb.ibrahim.agenda.Adapters.AdapterDirectorActivities;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.activities.Activity_inside_activities;
import com.mobiweb.ibrahim.agenda.models.entities.Activities;
import com.mobiweb.ibrahim.agenda.models.json.JsonActivities;
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

public class Activity_view_activities extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvActivities;
    private AdapterDirectorActivities adapterActivities;
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
    private final int PAGE_SIZE = 5;
    private ArrayList<Activities> arrayActivities=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_activities);
        init();
        setPagination();
        retreiveActivities(pageNumber,PAGE_SIZE);
        toolbarTitle.setText(getString(R.string.activities));
        toolbarTitleAr.setText(getString(R.string.activitiesAr));

    }

    private void init() {
        rvActivities = (RecyclerView) findViewById(R.id.rvActivities);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_view_activities.this,Activity_direction_home.class));

            }
        });
        loadingPaging=(LinearLayout)findViewById(R.id.loading_paging);
        ivRight.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_view_activities.this,Activity_activities_main.class));
                else
                    Activity_view_activities.super.onBackPressed();
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
                startActivity(new Intent(Activity_view_activities.this,Activity_main.class));
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



    private void retreiveActivities(int page, int pageSize) {
        if (adapterActivities != null) {
            rvActivities.scrollToPosition(adapterActivities.getItemCount() - 1);
            loadingPaging.setVisibility(View.VISIBLE);
        }

        else
            progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getActivities(new JsonParameters(
                       ((Agenda)getApplication()).getLoginId(),
                        page+"",
                        pageSize+"",
                        1,
                        true
                ));
        call1.enqueue(new Callback<JsonActivities>() {
            @Override
            public void onResponse(Call<JsonActivities> call, Response<JsonActivities> response) {

                try {
                    onDataRetrieved(response.body());
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
        loadingPaging.setVisibility(View.GONE);
        arrayActivities=Allteachers.getActivities();
        if (adapterActivities == null) {
            if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
               adapterActivities = new AdapterDirectorActivities(arrayActivities, this,true);
            else
                adapterActivities = new AdapterDirectorActivities(arrayActivities, this,false);
            GridLayoutManager glm = new GridLayoutManager(this, 1);
            rvActivities.setLayoutManager(glm);
            rvActivities.setAdapter(adapterActivities);
        }else {
            int position = adapterActivities.getactivities().size();
            adapterActivities.getactivities().addAll(arrayActivities);
            adapterActivities.notifyItemInserted(position);
        }
        isLoading = Allteachers.getActivities().isEmpty();

    }


    @Override
    public void onItemClicked(View view, int position) {
        if(view.getId()==R.id.btEdit){
            Intent i=new Intent(Activity_view_activities.this,Activity_edit_activities.class);
            i.putExtra(AppConstants.ID,adapterActivities.getactivities().get(position).getActivityId());
            i.putExtra(AppConstants.TITLE,adapterActivities.getactivities().get(position).getTitle());
            i.putExtra(AppConstants.DATE,adapterActivities.getactivities().get(position).getActivity_date());
            i.putExtra(AppConstants.DESCRIPTION,adapterActivities.getactivities().get(position).getDescription());
            startActivity(i);
        }else if(view.getId()==R.id.btAddEditAlbum){
            if(adapterActivities.getactivities().get(position).getImages().size()>0) {
                Intent i = new Intent(Activity_view_activities.this, Activity_edit_activity_images.class);
                i.putExtra(AppConstants.ID, adapterActivities.getactivities().get(position).getActivityId());
                i.putExtra(AppConstants.TITLE, adapterActivities.getactivities().get(position).getTitle());
                AppHelper.setActivityImages(adapterActivities.getactivities().get(position).getImages());
                startActivity(i);
            }else {
                Intent i = new Intent(Activity_view_activities.this, Activity_add_activity_images.class);
                i.putExtra(AppConstants.ID, adapterActivities.getactivities().get(position).getActivityId());
                i.putExtra(AppConstants.TITLE, adapterActivities.getactivities().get(position).getTitle());
                startActivity(i);
            }



        }else if(view.getId()==R.id.btDeleteActivity){
             deleteActivity(adapterActivities.getactivities().get(position).getActivityId());
        }
        else {
            Intent i = new Intent(Activity_view_activities.this, Activity_inside_activities.class);
            i.putExtra(AppConstants.TITLE, adapterActivities.getactivities().get(position).getTitle());
            i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_ACTIVITIES);
            i.putExtra(AppConstants.DESCRIPTION, adapterActivities.getactivities().get(position).getDescription());
            i.putExtra(AppConstants.DATE, adapterActivities.getactivities().get(position).getActivity_date());
            ArrayList<String> arrayImages = new ArrayList<String>();
            for (int j = 0; j < adapterActivities.getactivities().get(position).getImages().size(); j++)
                arrayImages.add(adapterActivities.getactivities().get(position).getImages().get(j).getImageName());
            i.putStringArrayListExtra(AppConstants.HW_IMAGE, arrayImages);
            startActivity(i);
        }
    }


    public void deleteActivity(String idActivity) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .deleteActivity(new JsonParameters(idActivity,2,true));
        call1.enqueue(new Callback<JsonAddHw>() {
            @Override
            public void onResponse(Call<JsonAddHw> call, Response<JsonAddHw> response) {

                try {
                    onImageDelete(response.body());
                    // Log.wtf("className", response.body().getAgenda().get(1).getHwDesc());
                } catch (Exception e) {
                    Log.wtf("exception","exception");

                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                call.cancel();

            }
        });
    }


    private void onImageDelete(JsonAddHw status){
        progress.setVisibility(View.GONE);
        if(status.getStatus().equals("success")) {
            Toast.makeText(getApplication(), "deleted successfully", Toast.LENGTH_LONG).show();
            adapterActivities=null;
            pageNumber=1;
            retreiveActivities(pageNumber,PAGE_SIZE);
        }
        else
            Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


    }



    private void setPagination(){
        rvActivities.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    if(!isLoading  && (rvActivities.getLayoutManager().getChildCount()
                            + ((LinearLayoutManager)rvActivities.getLayoutManager()).findFirstVisibleItemPosition()
                            >= rvActivities.getLayoutManager().getItemCount())) {
                        isLoading = true;
                        pageNumber ++;
                        retreiveActivities(pageNumber, PAGE_SIZE);
                    }
                }
            }
        });

    }







    @Override
    public void onBackPressed() {
        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
           startActivity(new Intent(Activity_view_activities.this,Activity_activities_main.class));
        else
           super.onBackPressed();
    }

}
