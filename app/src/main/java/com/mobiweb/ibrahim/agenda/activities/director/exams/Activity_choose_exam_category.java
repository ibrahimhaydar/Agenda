package com.mobiweb.ibrahim.agenda.activities.director.exams;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.Adapter_exams_categories;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.Activity_classes;
import com.mobiweb.ibrahim.agenda.activities.parents.exams.Activity_view_exams_all_details;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_grades;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher_exams_main;
import com.mobiweb.ibrahim.agenda.models.json.JsonExamsCategory;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Activity_choose_exam_category extends ActivityBase implements RVOnItemClickListener {
    private RecyclerView rvCategories;
    private Adapter_exams_categories adapterClasses;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;
    private String intentFrom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_activities);
        init();
        retreiveCategories();
        toolbarTitle.setText(getString(R.string.category));
        toolbarTitleAr.setText(getString(R.string.categoryAr));


    }

    private void init() {
        rvCategories = (RecyclerView) findViewById(R.id.rvActivities);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr) findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_TEACHER))
                    startActivity(new Intent(Activity_choose_exam_category.this, Activity_teacher.class));

                else
                    startActivity(new Intent(Activity_choose_exam_category.this, Activity_direction_home.class));


            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_DIRECTION_EXAMS))
                    startActivity(new Intent(Activity_choose_exam_category.this, Activity_direction_home.class));
                else
                    Activity_choose_exam_category.super.onBackPressed();

            }
        });
        rvCategories.setNestedScrollingEnabled(false);
        ivBack.setVisibility(View.VISIBLE);
  }





    private void retreiveCategories() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .exams_get_category_types(new JsonParameters("1",1));
        call1.enqueue(new Callback<JsonExamsCategory>() {
            @Override
            public void onResponse(Call<JsonExamsCategory> call, Response<JsonExamsCategory> response) {

                try {
                    onDataRetrieved(response.body());
                   
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonExamsCategory> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonExamsCategory AllClasses){
        progress.setVisibility(View.GONE);
        adapterClasses=new Adapter_exams_categories(AllClasses.getAllCategories(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvCategories.setLayoutManager(glm);
        rvCategories.setAdapter(adapterClasses);

    }


    @Override
    public void onItemClicked(View view, int position) {
        AppHelper.setId_category_exam(adapterClasses.getcategories().get(position).getIdCategory());


        AppHelper.setExamCategoryName(adapterClasses.getcategories().get(position).getTitle());

        if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_PARENTS_EXAMS)){

            Intent i = new Intent(Activity_choose_exam_category.this, Activity_view_exams_all_details.class);
            startActivity(i);

        }




       else if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_TEACHER)) {
            if(getIntent().getStringExtra(AppConstants.EXAM_FROM).matches(AppConstants.EXAM_FROM_SCHEDULE_EXAM))
            {
                Intent i = new Intent(Activity_choose_exam_category.this, Activity_teacher_exams_main.class)
                        .putExtra(AppConstants.CLASS_NAME,getIntent().getStringExtra(AppConstants.CLASS_NAME));
                startActivity(i);
            }else if(getIntent().getStringExtra(AppConstants.EXAM_FROM).matches(AppConstants.EXAM_FROM_GRADES))
            {
                Intent i = new Intent(Activity_choose_exam_category.this, Activity_grades.class);
                startActivity(i);
            }

        }
        else if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_TEACHER_EXAMS)) {
            Intent i = new Intent(Activity_choose_exam_category.this, Activity_classes.class);
            i.putExtra(AppConstants.INTENT_FROM,AppConstants.INTENT_TEACHER_EXAMS);
            startActivity(i);

        }

       else {
            Intent i = new Intent(Activity_choose_exam_category.this, Activity_classes.class);
            i.putExtra(AppConstants.INTENT_FROM,getIntent().getStringExtra(AppConstants.INTENT_FROM));
            if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null)
                i.putExtra(AppConstants.INTENT_ACTIVITY, AppConstants.INTENT_DIRECTOR_GRADES);
            startActivity(i);
        }

    }

    @Override
    public void onBackPressed() {
        if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_DIRECTION_EXAMS))
            startActivity(new Intent(Activity_choose_exam_category.this, Activity_direction_home.class));
        else
            super.onBackPressed();
    }
}
