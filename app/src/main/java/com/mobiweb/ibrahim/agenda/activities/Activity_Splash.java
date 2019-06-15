package com.mobiweb.ibrahim.agenda.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.BuildConfig;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.activities.Activity_inside_activities;
import com.mobiweb.ibrahim.agenda.activities.teachers.Activity_teacher;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonUser;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;
import com.mobiweb.ibrahim.agenda.utils.TelephonyHelper;
import com.mobiweb.ibrahim.agenda.utils.pushFirebase.FcmHelper;
import com.mobiweb.ibrahim.agenda.utils.pushFirebase.FcmTokenRegistrationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/6/2017.
 */

public class Activity_Splash extends ActivityBase {
    private boolean isPush = false;
    private String pageId,contentId,contentTitle,contentMessage,contentDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("PageId") != null) {
                isPush = true;

            }
            else
                isPush = false;
            //bundle must contain all info sent in "data" field of the notification
        } else {
            try {
                isPush = getIntent().getBooleanExtra(AppConstants.ISPUSH, false);

            } catch (Exception e) {
                isPush = false;
            }
        }

        if(isPush){
            try {
                pageId = getIntent().getStringExtra("PageId");
            } catch (Exception e) {
                pageId = "";
            }
            try {
                contentId = getIntent().getStringExtra("contentId");
            } catch (Exception e) {
                contentId = "";
            }
            try {
                contentTitle = getIntent().getStringExtra("title");
            } catch (Exception e) {
                contentTitle = "";
            }
            try {
                contentMessage = getIntent().getStringExtra("body");
            } catch (Exception e) {
                contentMessage = "";
            }
            try {
                contentDate = getIntent().getStringExtra("date");
            } catch (Exception e) {
                contentDate = "";
            }

            Log.wtf("notification", "pageid" + pageId + "contentId" + contentId );
        }


        new Handler().postDelayed(new Runnable() {
            public void run() {

                registerDeviceInfo();

            }

        }, 2000);
    }





    private void checklogin(final String username, final String password, final String loginType){
        TelephonyHelper telephonyHelper = new TelephonyHelper();
        String deviceId = telephonyHelper.getDeviceID(this);
            Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                    .checkUser(new JsonParameters(username,password,loginType,deviceId,true,1));
            call1.enqueue(new Callback<JsonUser>() {
                @Override
                public void onResponse(Call<JsonUser> call, Response<JsonUser> response) {

                    try {
                        onDataRetrieved(response.body());
                        Log.wtf("className", response.body().getStatus());
                    } catch (Exception e) {
                        Log.wtf("loginerror1",e);
                        Log.wtf("loginerror1",username);
                        Log.wtf("loginerror1",password);
                        Log.wtf("loginerror1",loginType);
                        startActivity(new Intent(Activity_Splash.this, Activity_main.class));
                    }
                }

                @Override
                public void onFailure(Call<JsonUser> call, Throwable t) {
                    call.cancel();
                    startActivity(new Intent(Activity_Splash.this, Activity_main.class));
                    Log.wtf("loginerror2:",t);
                }
            });


    }


    private void onDataRetrieved(JsonUser user){
        ((Agenda)getApplication()).setCashedUser(user.getUser());
        if(user.getStatus().matches("success") && (user.getUser().getType()).matches("teacher")){

            // startActivity(new Intent(Activity_Splash.this,Activity_teacher.class));
            checkToGoNext(AppConstants.LOGIN_TEACHER);

        }else if(user.getStatus().matches("success") && (user.getUser().getType()).matches("parents")){
           checkToGoNext(AppConstants.LOGIN_PARENT);
        }else if(user.getStatus().matches("success") && (user.getUser().getType()).matches("directions")){
            checkToGoNext(AppConstants.LOGIN_DIRECTION);
        }else {
            startActivity(new Intent(Activity_Splash.this, Activity_main.class));
        }}



        private void checkToGoNext(String loginType){
            if(isPush){


                Intent intent = null;

                if (pageId != null) {
                    switch (pageId) {
                        case AppConstants.PAGE_HOME:
                            intent = new Intent(Activity_Splash.this, Activity_direction_home.class);

                            break;
                        case AppConstants.PAGE_ANNOUNCEMENT_INSIDE:

                            if(!((Agenda) getApplication()).getCashedUsername().isEmpty() && !((Agenda) getApplication()).getCashedPassword().isEmpty()) {
                                Log.wtf("announcement_extras : ", "ispush : " + isPush);
                                Log.wtf("announcement_extras : ", "id : " + contentId);
                                Log.wtf("announcement_extras : ", "id : " + contentId);
                                Log.wtf("announcement_extras : ", "title : " + contentTitle);
                                Log.wtf("announcement_extras : ", "message : " + contentMessage);
                                Log.wtf("announcement_extras : ", "date : " + contentDate);

                                intent = new Intent(Activity_Splash.this, Activity_inside_activities.class);
                                intent.putExtra(AppConstants.ANNOUNCEMENT_ID, contentId);
                                intent.putExtra(AppConstants.INTENT_FROM, AppConstants.INTENT_ANNOUNCEMENT);
                                intent.putExtra(AppConstants.TITLE, contentTitle);
                                intent.putExtra(AppConstants.DESCRIPTION, contentMessage);
                                intent.putExtra(AppConstants.DATE, contentDate);
                                intent.putExtra(AppConstants.ISPUSH, true);
                            }else {
                                intent = new Intent(Activity_Splash.this, Activity_direction_home.class);
                            }

                            break;

                        default:
                            intent = new Intent(Activity_Splash.this, Activity_direction_home.class);
                            //  intent.putExtra(AppConstants.MATCH_ID,matchId);
                            break;
                    }

                    intent.putExtra(AppConstants.ISPUSH, true);
                    startActivity(intent);
                } else {
                    intent = new Intent(Activity_Splash.this, Activity_direction_home.class);
                    intent.putExtra(AppConstants.ISPUSH, true);
                    startActivity(intent);
                }



            }
            else
                startActivity(new Intent(Activity_Splash.this,Activity_direction_home.class));

        }





    private void registerDeviceInfo() {
        String regId = "";
        FcmHelper fcmHelper = new FcmHelper(Activity_Splash.this);
        try {
            sendFcmRegistrationToken();
            // GcmHelper gcmHelper = new GcmHelper(ActivitySplash.this);//init Gcm
            regId = FirebaseInstanceId.getInstance().getToken();

            Log.wtf("tokenId", "ttttttt" + regId);
            if (regId.isEmpty()) {
                fcmHelper.register();
            } else {
                register(regId);
            }
        } catch (Exception e) {
            Log.wtf("Exception", e);
            register(regId);
        }

    }
    private void sendFcmRegistrationToken() {
        Intent intent = new Intent(this, FcmTokenRegistrationService.class);
        startService(intent);
    }








    public void     register(final String DeviceToken) {

        TelephonyHelper telephonyHelper = new TelephonyHelper();
        String deviceId = telephonyHelper.getDeviceID(this);
        String deviceModel = telephonyHelper.getDeviceModel();
        String deviceName = telephonyHelper.getDeviceName();


        String appVersion = BuildConfig.VERSION_CODE+"";
        String MNC = telephonyHelper.getMNC(this);
        String MCC = telephonyHelper.getMCC(this);
        String deviceVersion = telephonyHelper.getDeviceVersion();

        Log.wtf("deviceId",deviceId);


        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addDevice(new JsonParameters(
                        deviceId,
                        DeviceToken,
                        deviceName+" "+deviceModel,
                        AppConstants.PLATFORM_ID,
                        appVersion,
                        MNC,
                        MCC,
                        deviceVersion,

                        1


                ));
        call1.enqueue(new Callback<JsonAddHw>() {
            @Override
            public void onResponse(Call<JsonAddHw> call, Response<JsonAddHw> response) {

                try {
                    // onDataRetrieved(response.body());
                    if(response.body().getStatus().matches("success")) {
                        Log.wtf("register register1", "successfull");
                        Log.wtf("register token",DeviceToken);

                    }
                    else
                        Log.wtf("register register1", "failed");

                    checkLogin();
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                    checkLogin();
                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                Log.wtf("register register1", "failed");
                call.cancel();
                checkLogin();
            }
        });


    }

    public static void registerNew(String regId){
       new Activity_Splash().register(regId);
    }

    private void checkLogin(){
        switch (((Agenda)getApplication()).getCashedType()) {
            case AppConstants.LOGIN_TEACHER:
                checklogin(((Agenda) getApplication()).getCashedUsername(), ((Agenda) getApplication()).getCashedPassword(), AppConstants.LOGIN_TEACHER);
                // startActivity(new Intent(Activity_Splash.this,Activity_teacher.class));
                break;
            case AppConstants.LOGIN_PARENT:
                // startActivity(new Intent(Activity_Splash.this,Activity_classes.class));
                checklogin(((Agenda) getApplication()).getCashedUsername(), ((Agenda) getApplication()).getCashedPassword(), AppConstants.LOGIN_PARENT);
                break;
            case AppConstants.LOGIN_DIRECTION:
                // startActivity(new Intent(Activity_Splash.this,Activity_all_teachers.class));
                checklogin(((Agenda) getApplication()).getCashedUsername(), ((Agenda) getApplication()).getCashedPassword(), AppConstants.LOGIN_DIRECTION);
                break;
            default:

                startActivity(new Intent(Activity_Splash.this, Activity_main.class));
                break;

        }
    }


}








