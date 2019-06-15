package com.mobiweb.ibrahim.agenda.activities.director.annoucement;

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

import com.mobiweb.ibrahim.agenda.Adapters.AdapterDirectorAnnouncements;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.activities.Activity_inside_activities;
import com.mobiweb.ibrahim.agenda.models.entities.Announcements;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonAnnouncement;
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

public class Activity_view_announcement extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvActivities;
    private AdapterDirectorAnnouncements adapterAnnouncements;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private LinearLayout loadingPaging;

    private boolean isLoading = false;
    private int pageNumber = 1;
    private final int PAGE_SIZE = 5;
    private ArrayList<Announcements> arrayAnnouncements=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_activities);
        init();
        setPagination();
        retreiveActivities(pageNumber,PAGE_SIZE);
        toolbarTitle.setText(getString(R.string.announcement));
        toolbarTitleAr.setText(getString(R.string.announcementAr));

    }

    private void init() {
        rvActivities = (RecyclerView) findViewById(R.id.rvActivities);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr) findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Activity_view_announcement.this,Activity_direction_home.class));
                //logout();

            }
        });
        loadingPaging=(LinearLayout)findViewById(R.id.loading_paging);
        ivRight.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_view_announcement.this,Activity_announcement_main.class));
                else
                    Activity_view_announcement.super.onBackPressed();



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
                startActivity(new Intent(Activity_view_announcement.this,Activity_main.class));
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
        if (adapterAnnouncements != null) {
            rvActivities.scrollToPosition(adapterAnnouncements.getItemCount() - 1);
            loadingPaging.setVisibility(View.VISIBLE);
        }

        else
            progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getAnnouncements(new JsonParameters(
                       ((Agenda)getApplication()).getLoginId(),
                        page+"",
                        pageSize+"",
                        1,
                        true
                ));
        call1.enqueue(new Callback<JsonAnnouncement>() {
            @Override
            public void onResponse(Call<JsonAnnouncement> call, Response<JsonAnnouncement> response) {

                try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonAnnouncement> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonAnnouncement Allannouncement){

        progress.setVisibility(View.GONE);
        loadingPaging.setVisibility(View.GONE);
        arrayAnnouncements=Allannouncement.getAnnouncement();
        if (adapterAnnouncements == null) {
            if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                adapterAnnouncements = new AdapterDirectorAnnouncements(arrayAnnouncements, this,true);
            else
                adapterAnnouncements = new AdapterDirectorAnnouncements(arrayAnnouncements, this,false);
            GridLayoutManager glm = new GridLayoutManager(this, 1);
            rvActivities.setLayoutManager(glm);
            rvActivities.setAdapter(adapterAnnouncements);
        }else {
            int position = adapterAnnouncements.getactivities().size();
            adapterAnnouncements.getactivities().addAll(arrayAnnouncements);
            adapterAnnouncements.notifyItemInserted(position);
        }
        isLoading = Allannouncement.getAnnouncement().isEmpty();

    }


    @Override
    public void onItemClicked(View view, int position) {
        if(view.getId()==R.id.btEdit){
            Intent i=new Intent(Activity_view_announcement.this,Activity_edit_announcement.class);
            i.putExtra(AppConstants.ID,adapterAnnouncements.getactivities().get(position).getAnnouncement_id());
            i.putExtra(AppConstants.TITLE,adapterAnnouncements.getactivities().get(position).getTitle());
            i.putExtra(AppConstants.DESCRIPTION,adapterAnnouncements.getactivities().get(position).getDescription());
            startActivity(i);
        }else if(view.getId()==R.id.btDeleteActivity){
             deleteAnnouncement(adapterAnnouncements.getactivities().get(position).getAnnouncement_id());
        }
        else {
            Intent i = new Intent(Activity_view_announcement.this, Activity_inside_activities.class);
            i.putExtra(AppConstants.TITLE, adapterAnnouncements.getactivities().get(position).getTitle());
            i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_ANNOUNCEMENT);
            i.putExtra(AppConstants.DESCRIPTION, adapterAnnouncements.getactivities().get(position).getDescription());
            i.putExtra(AppConstants.DATE, adapterAnnouncements.getactivities().get(position).getAnnouncement_date());

            ArrayList<String> arrayImages = new ArrayList<String>();
            try {
                for (int j = 0; j < adapterAnnouncements.getactivities().get(position).getImages().size(); j++)
                    arrayImages.add(adapterAnnouncements.getactivities().get(position).getImages().get(j).getImageName());
                i.putStringArrayListExtra(AppConstants.HW_IMAGE, arrayImages);
            }catch (Exception e){}
            startActivity(i);
        }
    }


    public void deleteAnnouncement(String idAnnouncement) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .deleteAnnouncement(new JsonParameters(idAnnouncement,3,true));
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
            pageNumber=1;
            adapterAnnouncements=null;
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
            startActivity(new Intent(Activity_view_announcement.this,Activity_announcement_main.class));
        else
            super.onBackPressed();
    }

}
