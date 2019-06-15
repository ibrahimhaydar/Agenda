package com.mobiweb.ibrahim.agenda.activities.director.annoucement;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterPopupClasses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.models.entities.PostClasses;
import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.json.JsonGetAllClasses;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/14/2017.
 */

public class Activity_add_announcement extends ActivityBase implements RVOnItemClickListener {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private LinearLayout progress,linearFilterClasses;
    private EditText edHwTitle,etHwDesc;
    private Button btAdd;
    private Activity activity;
    private CustomTextView ctvDialogMessage;
    private CustomTextViewBold ctvTitleLabel,ctvDescLabel;
    private RadioButton rdAll, rdFilter;
    private RadioGroup rgFilterClasses;
    private Dialog studentsDialog;
    private RecyclerView rvClasses;
    private  AdapterPopupClasses adapterClasses;


    private Dialog responseDialog;
    private Button btOk;
    private LinearLayout linearDate;
    private LinearLayout linearProgressDialog;
    private String isFilter="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activites);
        init();
        toolbarTitle.setText(getString(R.string.add_announcement));
        toolbarTitleAr.setText(getString(R.string.add_announcement_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_add_announcement.super.onBackPressed();
            }
        });

    }


    private void init() {


        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        linearDate=(LinearLayout)findViewById(R.id.linearDate);
        linearFilterClasses=(LinearLayout)findViewById(R.id.linearFilterClasses);
        linearDate.setVisibility(View.GONE);
        linearFilterClasses.setVisibility(View.VISIBLE);


        rgFilterClasses =(RadioGroup)findViewById(R.id.rgFilter);
        rdAll =(RadioButton) rgFilterClasses.findViewById(R.id.rdAll);
        rdFilter =(RadioButton) rgFilterClasses.findViewById(R.id.rdFilter);
        rdAll.setChecked(true);
/*        rgFilterClasses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               if(radioGroup.getCheckedRadioButtonId()==R.id.rdFilter){
                   isFilter="1";
                   shoPopupClasses();

               }

            }
        });*/


        RadioButton rb = (RadioButton) findViewById(R.id.rdFilter);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilter="1";
                shoPopupClasses();
            }
        });




        ivBack=(ImageView)findViewById(R.id.ivBack);
        progress=(LinearLayout)findViewById(R.id.progress);
        activity=this;
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_add_announcement.this,Activity_direction_home.class));
            }
        });
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.home);
        edHwTitle=(EditText)findViewById(R.id.etTitle);
       // edHwTitle.setText(AppHelper.getCourseName());
        etHwDesc=(EditText)findViewById(R.id.etDescription);
        btAdd=(Button)findViewById(R.id.btAdd);
        ctvTitleLabel=(CustomTextViewBold)findViewById(R.id.ctvTitleLabel);
        ctvDescLabel=(CustomTextViewBold)findViewById(R.id.ctvDescLabel);

        ctvTitleLabel.setText("Announcement title");
        ctvDescLabel.setText("Announcement Description");


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etHwDesc.getText().toString().isEmpty() || edHwTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Please fill title and desc",Toast.LENGTH_LONG).show();
                }

                else {
                    //   AddHomework( edHwTitle.getText().toString(),etHwDesc.getText().toString(),ctvdate.getText().toString());
                    addActivity(edHwTitle.getText().toString(),etHwDesc.getText().toString());
                }
            }
        });





    }





    private void shoPopupClasses(){

        studentsDialog = new Dialog(this);
        studentsDialog.setContentView(R.layout.popup_classes);
        rvClasses =(RecyclerView) studentsDialog.findViewById(R.id.rvCourses);
        linearProgressDialog=(LinearLayout)studentsDialog.findViewById(R.id.linearProgressDialog);
        linearProgressDialog.setVisibility(View.VISIBLE);
        studentsDialog.show();

        Button btDone=(Button)studentsDialog.findViewById(R.id.btDone);
        Button btCancel=(Button)studentsDialog.findViewById(R.id.btCancel);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilter="1";
                studentsDialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilter = "0";
                rdAll.setChecked(true);
                studentsDialog.dismiss();
            }
        });

        if(adapterClasses==null)
          retreiveClasses();
        else{
            try{linearProgressDialog.setVisibility(View.GONE);
                GridLayoutManager glm=new GridLayoutManager(this,1);
                rvClasses.setLayoutManager(glm);
                rvClasses.setAdapter(adapterClasses);
            }catch (Exception e){}
        }




         studentsDialog.setCanceledOnTouchOutside(false);
         studentsDialog.setCancelable(false);

    }



    private void retreiveClasses() {
        linearProgressDialog.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getClasses(new JsonParameters(
                        "",false)
                );
        call1.enqueue(new Callback<JsonGetAllClasses>() {
            @Override
            public void onResponse(Call<JsonGetAllClasses> call, Response<JsonGetAllClasses> response) {

                try {
                    onDataRetrieved(response.body());
                    Log.wtf("className", response.body().getAllclasses().get(1).getClassName());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonGetAllClasses> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void onDataRetrieved(JsonGetAllClasses AllClasses){
        linearProgressDialog.setVisibility(View.GONE);
        adapterClasses=new AdapterPopupClasses(AllClasses.getAllclasses(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvClasses.setLayoutManager(glm);
        rvClasses.setAdapter(adapterClasses);

    }


    private void addActivity(String title,String description) {


        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addAnnouncement(new JsonParameters("1",title,description,isFilter,getArraySelected()));
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        if (response.body().getResult().equals("success")){
                            popUpMessage(true);

                        }
                        else
                            popUpMessage(false);
                    }catch (Exception e){
                        popUpMessage(false);

                    }

                } else {
                    popUpMessage(false);
                }




            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.wtf("exception",t);
                progress.setVisibility(View.GONE);
            }
        });
    }









    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("Added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                   // Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show();
                  startActivity(new Intent(Activity_add_announcement.this,Activity_view_announcement.class));
                }
                else {
                   // Toast.makeText(getApplication(),"fail",Toast.LENGTH_LONG).show();
                   responseDialog.dismiss();
                }
            }
        });



        responseDialog.show();
    }


    @Override
    public void onItemClicked(View view, int position) {
        adapterClasses.getclasses().get(position).setSelected(!adapterClasses.getclasses().get(position).getSelected());
        adapterClasses.notifyItemChanged(position);
    }


    private ArrayList<PostClasses> getArraySelected(){
        ArrayList<PostClasses> arrayClasses =new ArrayList();
        PostClasses p = null;
       adapterClasses.notifyDataSetChanged();
       if(adapterClasses!=null) {
           for (int i = 0; i < adapterClasses.getclasses().size(); i++) {
               if (adapterClasses.getclasses().get(i).getSelected()) {
                   p = new PostClasses(adapterClasses.getclasses().get(i).getIdClass(), adapterClasses.getclasses().get(i).getIdSection());
                   arrayClasses.add(p);
               }


           }
       }


        return arrayClasses;
    }
}
