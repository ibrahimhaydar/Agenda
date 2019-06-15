package com.mobiweb.ibrahim.agenda.activities.parents.exams;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterDates;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterExamsCoursesDetails;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterExpandableExams;
import com.mobiweb.ibrahim.agenda.Adapters.AdapterScheduleDetails;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.Custom.NonScrollExpandableListView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Header;
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.models.entities.ViewExam;
import com.mobiweb.ibrahim.agenda.models.json.JsonAllExamsDetails;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.SchedulesExamsDetail;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 4/25/2018.
 */

public class Activity_view_exams_all_details extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvCoursesExamsEn;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress;
    private AdapterDates adapterDates;
    private AdapterScheduleDetails adapterScheduleDetails;
    private LinearLayout noData;

    private CustomTextViewBold ctvTitle;
    private CustomTextView ctvNb;
    private CustomTextViewBoldAr ctvMore;
    private NonScrollExpandableListView expList;
    private AdapterExpandableExams adapterExamsSchedules;
    List<SchedulesExamsDetail> myChild1;

    private AdapterExamsCoursesDetails adapterCoursesDetails;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exam_schedule);
        init();
        retreiveExamsDetails();
    }

    private void init(){



       rvCoursesExamsEn=(RecyclerView)findViewById(R.id.rvEnglish);
        expList = (NonScrollExpandableListView) findViewById(R.id.idListView);
        ctvTitle=(CustomTextViewBold)findViewById(R.id.ctvTitle);
        ctvNb=(CustomTextView)findViewById(R.id.ctvNb);
        ctvMore=(CustomTextViewBoldAr) findViewById(R.id.ctvMore);
        noData=(LinearLayout)findViewById(R.id.nodata);

        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);

        ivBack=(ImageView)findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_view_exams_all_details.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);

        toolbarTitle.setText(getString(R.string.exams));
        toolbarTitleAr.setText(getString(R.string.examsAr));

        

     rvCoursesExamsEn.setNestedScrollingEnabled(false);
      
    }

    private void retreiveExamsDetails(){

        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_view_exam_all_details(
                        new JsonParameters(
                                3,
                                ((Agenda)getApplication()).getLoginId(),
                                AppHelper.getId_class(),
                                AppHelper.getId_section(),
                                AppHelper.getId_category_exam(),
                                "true"


                ));
        call1.enqueue(new Callback<JsonAllExamsDetails>() {
            @Override
            public void onResponse(Call<JsonAllExamsDetails> call, Response<JsonAllExamsDetails> response) {

               try {
                    onDataRetrieved(response.body());
                } catch (Exception e) {
                    Log.wtf("exception",e);
               }
            }

            @Override
            public void onFailure(Call<JsonAllExamsDetails> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonAllExamsDetails details) {


        progress.setVisibility(View.GONE);
        setDates(details.getSchedulesExamsDetails());
        setHeader(details.getHeader());
        if(details.getTeachersExams().size()==0) {
            rvCoursesExamsEn.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
        else {
            noData.setVisibility(View.GONE);
            setCoursesExams(details.getTeachersExams());
        }

    }


/*    private void setDates(ArrayList<SchedulesExamsDetail> arraySchedule) {
        adapterDates=new AdapterDates(arraySchedule,this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvInfo.setLayoutManager(glm);
        rvInfo.setAdapter(adapterDates);
    }*/


    private void setCoursesExams(ArrayList<ViewExam> arrayExams) {
        adapterCoursesDetails=new AdapterExamsCoursesDetails(arrayExams,this);
        rvCoursesExamsEn.setVisibility(View.VISIBLE);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvCoursesExamsEn.setLayoutManager(glm);
        rvCoursesExamsEn.setAdapter(adapterCoursesDetails);
    }

private void setHeader(Header header){
    if(!header.getTitle().isEmpty())
      ctvTitle.setText(header.getTitle());
    else
        ctvTitle.setVisibility(View.GONE);
    if(!header.getMore().isEmpty())
       ctvMore.setText(header.getMore());
    else
        ctvMore.setVisibility(View.GONE);

    if(!header.getNote().isEmpty())
       ctvNb.setText(header.getNote());
    else
        ctvNb.setVisibility(View.GONE);
}

    @Override
    public void onItemClicked(View view, int position) {

    }


    private void setDates(ArrayList<SchedulesExamsDetail> arraySchedule){

       ArrayList<SchedulesExamsDetail> finalArray=new ArrayList<>();
       finalArray=arraySchedule;





        LinkedHashMap<SchedulesExamsDetail, List<Info>> HeaderDetails = new LinkedHashMap<SchedulesExamsDetail, List<Info>>();
        for (int j=0;j<arraySchedule.size();j++){
            Log.wtf("playerpoints",arraySchedule.get(j).getDate());
            try {
                HeaderDetails.put(arraySchedule.get(j),arraySchedule.get(j).getInfo());
            }catch (Exception e){
                Log.wtf("c","cxc");
            }

        }
        myChild1 = new ArrayList<SchedulesExamsDetail>(HeaderDetails.keySet());
        adapterExamsSchedules = new AdapterExpandableExams(this,HeaderDetails,myChild1);
        expList.setAdapter(adapterExamsSchedules);
        expList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }
}
