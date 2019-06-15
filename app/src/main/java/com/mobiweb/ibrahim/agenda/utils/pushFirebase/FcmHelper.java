package com.mobiweb.ibrahim.agenda.utils.pushFirebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobiweb.ibrahim.agenda.activities.Activity_Splash;


public class FcmHelper {

    // Variables
    public static final String TAG = "FCM NOTIFICATION";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private static Context context;
    private String regId;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // Constructor
    public FcmHelper(Context context) {
        FcmHelper.context = context;

    }

    public void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getFcmPreferences();
        int appVersion = getAppVersion();
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    public static String getRegistrationId() {
        final SharedPreferences prefs = getFcmPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId == null || registrationId.equals("")) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void register() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String msg;
                try {

                    regId = FirebaseInstanceId.getInstance().getToken();
                    msg = "Device registered, registration ID = " + regId;
                    Log.wtf("regId", "regId : " + msg);
                    Log.wtf("regId", "regId : " + msg);
                    Log.wtf("regId", "regId : " + msg);
                    Log.wtf("regId", "regId : " + msg);
                    Log.wtf("regId", "regId : " + msg);
                    Activity_Splash.registerNew(regId);
                    storeRegistrationId(regId);


                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();

                    retry_register();
                }
                return msg;
            }
        }.execute(null, null, null);
    }


    public void retry_register() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String msg;
                try {

                    regId = FirebaseInstanceId.getInstance().getToken();
                    msg = "Device registered, registration ID = " + regId;

                    Log.d("regId", "regId : " + regId);
                    Log.d("regId", "regId : " + regId);
                    Log.d("regId", "regId : " + regId);
                    Log.d("regId", "regId : " + regId);
                    Log.d("regId", "regId : " + regId);

                    //AppHelper.register(context, regId);
                    storeRegistrationId(regId);
                    Activity_Splash.registerNew(regId);
                } catch (Exception ex) {
                    msg = "Error retry_register:" + ex.getMessage();
                    Activity_Splash.registerNew("");
                }
                return msg;
            }

        }.execute(null, null, null);
    }

    private static SharedPreferences getFcmPreferences() {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

}
