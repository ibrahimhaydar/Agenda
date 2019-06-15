package com.mobiweb.ibrahim.agenda.activities.teachers;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterTeacherClasses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonTeacherClasses;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_teacher_classes extends ActivityBase implements RVOnItemClickListener{
    private RecyclerView rvClasses;
    private AdapterTeacherClasses adapterClasses;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        init();
        retreiveClasses(getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,""), getIntent().getStringExtra(AppConstants.COURSE_ID)+"");
        toolbarTitle.setText(getString(R.string.classes));
        toolbarTitleAr.setText(getString(R.string.classes_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_teacher_classes.super.onBackPressed();
            }
        });

    }

    private void init() {
        rvClasses = (RecyclerView) findViewById(R.id.rvClasses);
        progress=(LinearLayout)findViewById(R.id.progress);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr) findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_teacher_classes.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_teacher_classes.this,Activity_teacher.class));
            }
        });



        rvClasses.setNestedScrollingEnabled(false);

    }

    private void retreiveClasses(String id_teacher,String id_course) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getTeacherClasses(new JsonParameters(id_teacher,id_course,true));
        call1.enqueue(new Callback<JsonTeacherClasses>() {
            @Override
            public void onResponse(Call<JsonTeacherClasses> call, Response<JsonTeacherClasses> response) {

                try {
                    onDataRetrieved(response.body());

                } catch (Exception e) {
                    Log.wtf("exception",e);
                }
            }

            @Override
            public void onFailure(Call<JsonTeacherClasses> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void onDataRetrieved(JsonTeacherClasses classes){
        progress.setVisibility(View.GONE);
        adapterClasses=new AdapterTeacherClasses(classes.getClasses(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvClasses.setLayoutManager(glm);
        rvClasses.setAdapter(adapterClasses);

    }


    @Override
    public void onItemClicked(View view, int position) {
        Intent i=new Intent(Activity_teacher_classes.this,Activity_Teacher_Homeworks.class);
        i.putExtra(AppConstants.CLASS_NAME,adapterClasses.getclasses().get(position).getClassName());
        i.putExtra(AppConstants.ClASS_ID,adapterClasses.getclasses().get(position).getIdClass());
        i.putExtra(AppConstants.ClASS_SECTION_ID,adapterClasses.getclasses().get(position).getIdSection());
        i.putExtra(AppConstants.COURSE_ID,adapterClasses.getclasses().get(position).getIdCourse());

        AppHelper.setId_class(adapterClasses.getclasses().get(position).getIdClass());
        AppHelper.setId_section(adapterClasses.getclasses().get(position).getIdSection());
        AppHelper.setCourseId(adapterClasses.getclasses().get(position).getIdCourse());

        startActivity(i);
    }
}
