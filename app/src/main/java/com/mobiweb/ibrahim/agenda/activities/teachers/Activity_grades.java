package com.mobiweb.ibrahim.agenda.activities.teachers;



import com.mobiweb.ibrahim.agenda.Adapters.AdapterAddGrades;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextOnKeyListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;

import com.mobiweb.ibrahim.agenda.models.entities.PostGrades;
import com.mobiweb.ibrahim.agenda.models.json.JsonClassStudents;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonResponse;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/13/2017.
 */

public class Activity_grades extends ActivityBase implements EditTextOnKeyListener {
    private RecyclerView rvGrades;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private AdapterAddGrades adapterAddGrades;


   private Button btAddEdit,btRemove;
    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;

    private Activity activity;
    private boolean isEdit=false;
    private boolean isDelete=false;
    private TextView tvCardClassName,tvCardExamName;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        // Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.TEACHER_ID),Toast.LENGTH_LONG).show();
        init();


        toolbarTitle.setText(getString(R.string.grades));
        toolbarTitleAr.setText(getString(R.string.grades_ar));

        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Activity_grades.super.onBackPressed();
                finish();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_grades.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_grades.this,Activity_teacher.class));
            }
        });


        getClassGrades();
    }

    private void init(){
        tvCardClassName=findViewById(R.id.tvCardClassName);
        tvCardClassName.setText(AppHelper.getClass_name());
        tvCardExamName=findViewById(R.id.tvCardExamName);
        tvCardExamName.setText(AppHelper.getExamCategoryName());
        tvCardExamName.setVisibility(View.VISIBLE);

        rvGrades=(RecyclerView)findViewById(R.id.rvGrades);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        progress=(LinearLayout)findViewById(R.id.progress);

        btAddEdit=(Button)findViewById(R.id.btAddEdit);
        btRemove=(Button)findViewById(R.id.btRemove);
        activity=this;


        btAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try{ adapterAddGrades.notifyDataSetChanged();}catch (Exception e){}

                if(checkGradeArray()){
                    Toast.makeText(getApplication(),"Please add grade",Toast.LENGTH_LONG).show();
                }
                else
                    addGrade(getPostGradess());
            }
        });


    }



    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            if(!isEdit)
             ctvDialogMessage.setText("added successfully");
            else if(isDelete)
                ctvDialogMessage.setText("Deleted Successfully");
            else
                ctvDialogMessage.setText("Edited Successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                        startActivity(new Intent(Activity_grades.this,Activity_Teacher_Homeworks.class));
                    else
                        startActivity(new Intent(Activity_grades.this,Activity_Teacher_Homeworks.class));

                    finish();
                }
                else {
                    responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }



    private boolean checkGradeArray(){
        boolean gradeEmpty=true;
        try{
            for(int i = 0; i< adapterAddGrades.getinfoStudents().size(); i++){
                if(!adapterAddGrades.getinfoStudents().get(i).getGrade().isEmpty())
                    gradeEmpty=false;
            }}catch (Exception e) {
            gradeEmpty=true;
        }
        return gradeEmpty;
    }


    private boolean checkNotValidGrades(){
        boolean gradeNotValid=true;
        try{
            for(int i = 0; i< adapterAddGrades.getinfoStudents().size(); i++){
                if(!adapterAddGrades.getinfoStudents().get(i).getGrade().isEmpty())
                    gradeNotValid=false;
            }}catch (Exception e) {
            gradeNotValid=true;
        }
        return gradeNotValid;
    }




    public void getClassGrades() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .get_class_grades(new JsonParameters(
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),

                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        AppHelper.getCourseId(),
                        AppHelper.getId_category_exam(),
                        2

                ));
        call1.enqueue(new Callback<JsonClassStudents>() {
            @Override
            public void onResponse(Call<JsonClassStudents> call, Response<JsonClassStudents> response) {

                try {
                    onStudentsRetreived(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonClassStudents> call, Throwable t) {
                call.cancel();
            }
        });
    }



    public void addGrade(ArrayList<PostGrades> arrayPostGrade) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .update_grades(new JsonParameters(
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                       ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        AppHelper.getId_category_exam(),
                        AppHelper.getCourseId(),
                        arrayPostGrade

                ));
        call1.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {

                try {
                    onGradeAdded(response.body());

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private ArrayList<PostGrades> getPostGradess(){
        ArrayList<PostGrades> arrayPostGrades=new ArrayList<>();
        PostGrades p;
        for(int i = 0; i< adapterAddGrades.getinfoStudents().size(); i++){
            // if(!adapterAddGrades.getinfoStudents().get(i).getInfo().isEmpty()){
            p=new PostGrades(adapterAddGrades.getinfoStudents().get(i).getIdStudent(), adapterAddGrades.getinfoStudents().get(i).getGrade());
            arrayPostGrades.add(p);
            //  }

        }
        return arrayPostGrades;
    }


    private void onGradeAdded(JsonResponse jsonResponse){
        progress.setVisibility(View.GONE);
        if(jsonResponse.getStatus().matches("success")){
            popUpMessage(true);
        }else {
            popUpMessage(false);
        }

    }



    private void onStudentsRetreived(JsonClassStudents classStudents){
        progress.setVisibility(View.GONE);
        if(classStudents.getIsEdit().matches("1")){
            isEdit=true;
            btRemove.setVisibility(View.VISIBLE);
            btRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteGrade();
                }
            });
        }
        else {
            isEdit=false;
            btRemove.setVisibility(View.GONE);
        }
        String max_grade="0";

        if(classStudents.getMax_grade().matches(""))
            max_grade="20";
        else
            max_grade=classStudents.getMax_grade();
        adapterAddGrades =new AdapterAddGrades(classStudents.getStudents(),this,Integer.parseInt(max_grade));
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvGrades.setAdapter(adapterAddGrades);
        rvGrades.setLayoutManager(glm);



    }




    private void deleteGrade(){

        final Dialog deletGradeDialog = new Dialog(this);
        deletGradeDialog.setContentView(R.layout.popup_logout);

        CustomTextView ctvDialog=(CustomTextView)deletGradeDialog.findViewById(R.id.ctvDialog);
        CustomTextViewAr ctvDialogAr=(CustomTextViewAr)deletGradeDialog.findViewById(R.id.ctvDialogAr);
        ctvDialog.setText("Are you sure you want to delete grade on all class?");
        ctvDialogAr.setVisibility(View.GONE);

        CustomTextView btYes=(CustomTextView)deletGradeDialog.findViewById(R.id.yes);
        CustomTextView btNo=(CustomTextView)deletGradeDialog.findViewById(R.id.no);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletGradeDialog.dismiss();
                isDelete=true;
                addGrade(getPostGradesDelete());
                
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletGradeDialog.dismiss();
            }
        });



        deletGradeDialog.show();
    }

    private ArrayList<PostGrades> getPostGradesDelete(){
        ArrayList<PostGrades> arrayPostGrade=new ArrayList<>();
        PostGrades p;
        for(int i=0;i<adapterAddGrades.getinfoStudents().size();i++){

            p=new PostGrades(adapterAddGrades.getinfoStudents().get(i).getIdStudent(),"");
            arrayPostGrade.add(p);


        }
        return arrayPostGrade;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public void EtOnKeyListener(int position, String text) {
         adapterAddGrades.getinfoStudents().get(position).setGrade(text);
    }
}
