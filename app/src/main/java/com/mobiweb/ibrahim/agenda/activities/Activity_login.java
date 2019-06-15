package com.mobiweb.ibrahim.agenda.activities;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonUser;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.InternetConnection;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;
import com.mobiweb.ibrahim.agenda.utils.TelephonyHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/6/2017.
 */

public class Activity_login extends ActivityBase {
    private EditText etUsername,etPassword;

    private Button btLogin;
    private LinearLayout progress,linearToolbar;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private String loginType;
    private String inputUsername,inputPassword;
    private String usertype;
    private ScrollView scrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();



    }

    private void init(){
        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);
        btLogin=(Button)findViewById(R.id.btLogin);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        progress=(LinearLayout)findViewById(R.id.progress);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsername.getText().toString().isEmpty()||etPassword.getText().toString().isEmpty())
                    Toast.makeText(getApplication(),"Please enter your username and password. \n الرجاء ادخال رمز الدخول و كلمة المرور",Toast.LENGTH_LONG).show();
                else {
                    inputUsername=etUsername.getText().toString();
                    inputPassword= etPassword.getText().toString();
                    checkUser(inputUsername,inputPassword);
                }
            }
        });
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        linearToolbar=(LinearLayout)findViewById(R.id.linearToolbar);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        loginType=getIntent().getStringExtra(AppConstants.LOGIN_TYPE);
        switch (loginType){
            case AppConstants.LOGIN_TEACHER:
                usertype="School staff";
                toolbarTitle.setText(getString(R.string.teachers));
                toolbarTitleAr.setText(getString(R.string.teachers_ar));
                break;
            case AppConstants.LOGIN_PARENT:

                usertype="Parents";
                toolbarTitle.setText(getString(R.string.parents));
                toolbarTitleAr.setText(getString(R.string.parents_ar));
                break;
            case AppConstants.LOGIN_DIRECTION:

                usertype="Direction";
                toolbarTitle.setText(getString(R.string.direction));
                toolbarTitleAr.setText(getString(R.string.direction_ar));
                break;
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_login.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);


    }



    public void checkUser(String username,String password) {
        if(!InternetConnection.checkConnection(this)){
            Toast.makeText(getApplication(),"Please check your internet connection \n الرجاء التأكد من الإتصال بشبكة الانترنت ",Toast.LENGTH_LONG).show();
        }else {


            progress.setVisibility(View.VISIBLE);
            TelephonyHelper telephonyHelper = new TelephonyHelper();
            String deviceId = telephonyHelper.getDeviceID(this);
            Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                    .checkUser(new JsonParameters(username, password, loginType, deviceId,true,1));
            call1.enqueue(new Callback<JsonUser>() {
                @Override
                public void onResponse(Call<JsonUser> call, Response<JsonUser> response) {

                    try {
                        onDataRetrieved(response.body());

                        Log.wtf("className", response.body().getStatus());
                    } catch (Exception e) {
                        Log.wtf("exception", "exception");
                        progress.setVisibility(View.GONE);

                        //  Toast.makeText(getApplication(),"wrong username or password",Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplication(), "Make sure of your password or the type of your account. \n الرجاء التأكد من الرقم السري او من هوية الملف الذي اخترته", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonUser> call, Throwable t) {
                    call.cancel();
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplication(), "Make sure of your password or the type of your account. \n الرجاء التأكد من الرقم السري او من هوية الملف الذي اخترته", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void onDataRetrieved(JsonUser user){
        progress.setVisibility(View.GONE);
      if(user.getStatus().matches("success") && (user.getUser().getType()).matches("teacher")){
         // getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getBoolean(AppConstants.TEACHER_SUCCESS,true);


          ((Agenda)getApplication()).setCashedType(AppConstants.LOGIN_TEACHER);
          ((Agenda)getApplication()).setCashedUsername(etUsername.getText().toString());
          ((Agenda)getApplication()).setCashedPassword(etPassword.getText().toString());
          ((Agenda)getApplication()).setLoginId(user.getUser().getId());
          ((Agenda)getApplication()).setCashedUser(user.getUser());


          Intent i=new Intent(Activity_login.this,Activity_direction_home.class);
         // Intent i=new Intent(Activity_login.this,Activity_teacher.class);
          i.putExtra(AppConstants.TEACHER_ID,user.getUser().getId());
          startActivity(i);
     }
      else if(user.getStatus().matches("success")&& (user.getUser().getType()).matches("parents")){


          ((Agenda)getApplication()).setCashedType(AppConstants.LOGIN_PARENT);
          ((Agenda)getApplication()).setCashedUsername(inputUsername);
          ((Agenda)getApplication()).setCashedPassword(inputPassword);
          ((Agenda)getApplication()).setLoginId(user.getUser().getId());
          ((Agenda)getApplication()).setCashedUser(user.getUser());
         // Intent i=new Intent(Activity_login.this,Activity_classes.class);
          Intent i=new Intent(Activity_login.this,Activity_direction_home.class);
          i.putExtra(AppConstants.PARENT_ID,user.getUser().getId());
          startActivity(i);
      }


      else if(user.getStatus().matches("success")&& (user.getUser().getType()).matches("directions")){

          ((Agenda)getApplication()).setCashedType(AppConstants.LOGIN_DIRECTION);
          ((Agenda)getApplication()).setCashedUsername(etUsername.getText().toString());
          ((Agenda)getApplication()).setCashedPassword(etPassword.getText().toString());
          ((Agenda)getApplication()).setLoginId(user.getUser().getId());
          ((Agenda)getApplication()).setCashedUser(user.getUser());
          ///Intent i=new Intent(Activity_login.this,Activity_all_teachers.class);
          Intent i=new Intent(Activity_login.this,Activity_direction_home.class);
          i.putExtra(AppConstants.DIRECTION_ID,user.getUser().getId());
          startActivity(i);
      }


      else {
         // Toast.makeText(getApplication(),"wrong username or password or you are not "+usertype,Toast.LENGTH_LONG).show();
          Toast.makeText(getApplication(),"Make sure of your password or the type of your account. \n الرجاء التأكد من الرقم السري او من هوية الملف الذي اخترته",Toast.LENGTH_LONG).show();

      }

    }
}
