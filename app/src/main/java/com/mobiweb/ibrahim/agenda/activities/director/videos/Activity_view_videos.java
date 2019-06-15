package com.mobiweb.ibrahim.agenda.activities.director.videos;

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

import com.mobiweb.ibrahim.agenda.Adapters.AdapterVideos;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.models.entities.Videos;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonVideos;
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

public class Activity_view_videos extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvVideos;
    private AdapterVideos adapterVideos;
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
    private final int PAGE_SIZE = 3;
    private ArrayList<Videos> arrayVideos=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_videos);
        init();
        setPagination();
        retrieveVideos(pageNumber,PAGE_SIZE);
        toolbarTitle.setText(getString(R.string.videos));
        toolbarTitleAr.setText(getString(R.string.videosAr));

    }

    private void init() {
        rvVideos = (RecyclerView) findViewById(R.id.rvVideos);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr) findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                startActivity(new Intent(Activity_view_videos.this,Activity_video_main.class));
            else
                 Activity_view_videos.super.onBackPressed();

            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_view_videos.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);


        loadingPaging=(LinearLayout)findViewById(R.id.loading_paging);

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
                startActivity(new Intent(Activity_view_videos.this,Activity_main.class));
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



    private void retrieveVideos(int page, int pageSize) {
        if (adapterVideos != null) {
            rvVideos.scrollToPosition(adapterVideos.getItemCount() - 1);
            loadingPaging.setVisibility(View.VISIBLE);
        }

        else
            progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getVideos(new JsonParameters(
                        ((Agenda)getApplication()).getLoginId(),
                        page+"",
                        pageSize+"",
                        1,
                        true
                ));
        call1.enqueue(new Callback<JsonVideos>() {
            @Override
            public void onResponse(Call<JsonVideos> call, Response<JsonVideos> response) {

                try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonVideos> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonVideos Allvideos){

        progress.setVisibility(View.GONE);
        loadingPaging.setVisibility(View.GONE);
        arrayVideos=Allvideos.getVideos();
        if (adapterVideos == null) {
            if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
               adapterVideos = new AdapterVideos(arrayVideos, this,true);
            else
                adapterVideos = new AdapterVideos(arrayVideos, this,false);
            GridLayoutManager glm = new GridLayoutManager(this, 1);
            rvVideos.setLayoutManager(glm);
            rvVideos.setAdapter(adapterVideos);
        }else {
            int position = adapterVideos.getvideos().size();
            adapterVideos.getvideos().addAll(arrayVideos);
            adapterVideos.notifyItemInserted(position);
        }
        isLoading = Allvideos.getVideos().isEmpty();

    }
    public void deleteVideo(String idVideo) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .deleteVideo(new JsonParameters(idVideo,4,true));
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


    private void onVideoDelete(JsonAddHw status){
        progress.setVisibility(View.GONE);
        if(status.getStatus().equals("success")) {
            Toast.makeText(getApplication(), "deleted successfully", Toast.LENGTH_LONG).show();
            retrieveVideos(pageNumber,PAGE_SIZE);
        }
        else
            Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


    }


    @Override
    public void onItemClicked(View view, int position) {

        if(view.getId()==R.id.btDeleteActivity){
            deleteVideo(adapterVideos.getvideos().get(position).getVideo_id());
        }
        else if(view.getId()==R.id.btEdit){
            Intent i=new Intent(Activity_view_videos.this,Activity_edit_videos.class);
            i.putExtra(AppConstants.VIDEO_ID,adapterVideos.getvideos().get(position).getVideo_id());
            i.putExtra(AppConstants.VIDEO_URL,adapterVideos.getvideos().get(position).getUrl());
            i.putExtra(AppConstants.VIDEO_TITLE,adapterVideos.getvideos().get(position).getTitle());
            i.putExtra(AppConstants.VIDEO_DESC,adapterVideos.getvideos().get(position).getDescription());
            i.putExtra(AppConstants.VIDEO_THUMB,adapterVideos.getvideos().get(position).getThumb());
            startActivity(i);
        }

        else {
            Intent i = new Intent(Activity_view_videos.this, ActivityVideo.class);
            i.putExtra(AppConstants.VIDEO_URL, RetrofitClient.BASE_URL + "videos/" + adapterVideos.getvideos().get(position).getUrl());
            startActivity(i);
        }


    }




    private void onImageDelete(JsonAddHw status){
        progress.setVisibility(View.GONE);
        if(status.getStatus().equals("success")) {
            Toast.makeText(getApplication(), "deleted successfully", Toast.LENGTH_LONG).show();
            pageNumber=1;
            adapterVideos=null;
            retrieveVideos(pageNumber,PAGE_SIZE);
        }
        else
            Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


    }



    private void setPagination(){
        rvVideos.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    if(!isLoading  && (rvVideos.getLayoutManager().getChildCount()
                            + ((LinearLayoutManager)rvVideos.getLayoutManager()).findFirstVisibleItemPosition()
                            >= rvVideos.getLayoutManager().getItemCount())) {
                        isLoading = true;
                        pageNumber ++;
                        retrieveVideos(pageNumber, PAGE_SIZE);
                    }
                }
            }
        });

    }







    @Override
    public void onBackPressed() {

        if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
            startActivity(new Intent(Activity_view_videos.this,Activity_video_main.class));
        else
            super.onBackPressed();
    }

}
