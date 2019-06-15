package com.mobiweb.ibrahim.agenda;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.mobiweb.ibrahim.agenda.models.entities.User;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

/**
 * Created by ibrahim on 11/25/2017.
 */

public class Agenda extends Application {

    public String cashedUsername,cashedPassword,cashedType,loginId,loginType;


    @Override
    public void onCreate() {
        super.onCreate();

    }


    public User getCashedUser (){
        Gson gson = new Gson();
        String json = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.USER_CACHE, "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }

    public void setCashedUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        try {
            getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit().putString(AppConstants.USER_CACHE, json).apply();
        } catch (Exception e) {
        }
    }






    public String getCashedUsername() {
        SharedPreferences sharedPref = this.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        cashedUsername = sharedPref.getString(AppConstants.SHARED_USERNAME, "");
        return cashedUsername;
    }

    public void setCashedUsername(String cashedUsername) {
        this.cashedUsername = cashedUsername;
        SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.SHARED_USERNAME, cashedUsername);
        editor.apply();
    }



    public String getCashedPassword() {
        SharedPreferences sharedPref = this.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        cashedPassword = sharedPref.getString(AppConstants.SHARED_PASSWORD, "");
        return cashedPassword;
    }

    public void setCashedPassword(String cashedPassword) {
        this.cashedPassword = cashedPassword;
        SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.SHARED_PASSWORD, cashedPassword);
        editor.apply();
    }

    public String getCashedType() {
        SharedPreferences sharedPref = this.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        cashedType = sharedPref.getString(AppConstants.SHARED_TYPE, "");

        return cashedType;
    }

    public void setCashedType(String cashedType) {
        this.cashedType = cashedType;
        SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.SHARED_TYPE, cashedType);
        editor.apply();
    }


    public String getLoginId() {
        SharedPreferences sharedPref = this.getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        loginId = sharedPref.getString(AppConstants.LOGIN_ID, "");
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
        SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(AppConstants.LOGIN_ID, loginId);
        editor.apply();
    }


public void logout(){
    SharedPreferences.Editor editor = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
    editor.putString(AppConstants.LOGIN_ID, "");
    editor.apply();

    SharedPreferences.Editor editor2 = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
    editor2.putString(AppConstants.SHARED_TYPE, "");
    editor2.apply();

    SharedPreferences.Editor editor3 = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
    editor3.putString(AppConstants.SHARED_PASSWORD, "");
    editor3.apply();

    SharedPreferences.Editor editor4 = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).edit();
    editor4.putString(AppConstants.SHARED_USERNAME, "");
    editor4.apply();
}

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
  /*  @Override
    public void onCreate() {
        super.onCreate();
       // Realm.init(this); //initialize other plugins

    }*/

}
