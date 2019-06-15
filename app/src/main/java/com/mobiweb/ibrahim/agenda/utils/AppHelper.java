package com.mobiweb.ibrahim.agenda.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.BuildConfig;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Evaluation;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.InfoStudent;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppHelper {
    public static String UserIdretreive;
    public static String courseName,courseId,teacherId,teacher_name,studentId;
    public static ArrayList<Image> activityImages=new ArrayList<>();
    public static String id_class,id_section,id_category_exam,class_name;
    public static Boolean isPush=false;
    public static ArrayList<InfoStudent> arrayEvaluationInfo=new ArrayList<>();
    public static String selectedDate;

    public static String getTeacherId() {
        return teacherId;
    }

    public static void setTeacherId(String teacherId) {
        AppHelper.teacherId = teacherId;
    }

    public static String getTeacher_name() {
        return teacher_name;
    }

    public static void setTeacher_name(String teacher_name) {
        AppHelper.teacher_name = teacher_name;
    }

    public static String getStudentId() {
        return studentId;
    }

    public static void setStudentId(String studentId) {
        AppHelper.studentId = studentId;
    }

    public static String getClass_name() {
        return class_name;
    }

    public static void setClass_name(String class_name) {
        AppHelper.class_name = class_name;
    }

    public static ArrayList<InfoStudent> getArrayEvaluationInfo() {
        return arrayEvaluationInfo;
    }

    public static void setArrayEvaluationInfo(ArrayList<InfoStudent> arrayEvaluationInfo) {
        AppHelper.arrayEvaluationInfo = arrayEvaluationInfo;
    }

    public static String getSelectedDate() {
        return selectedDate;
    }

    public static void setSelectedDate(String selectedDate) {
        AppHelper.selectedDate = selectedDate;
    }

    public static Boolean getIsPush() {
        return isPush;
    }

    public static void setIsPush(Boolean isPush) {
        AppHelper.isPush = isPush;
    }

    public static String getCourseId() {
        return courseId;
    }

    public static void setCourseId(String courseId) {
        AppHelper.courseId = courseId;
    }

    public static String getId_class() {
        return id_class;
    }

    public static void setId_class(String id_class) {
        AppHelper.id_class = id_class;
    }

    public static String getId_section() {
        return id_section;
    }

    public static void setId_section(String id_section) {
        AppHelper.id_section = id_section;
    }

    public static String getId_category_exam() {
        return id_category_exam;
    }

    public static void setId_category_exam(String id_category_exam) {
        AppHelper.id_category_exam = id_category_exam;
    }

    public static ArrayList<Image> getActivityImages() {
        return activityImages;
    }

    public static void setActivityImages(ArrayList<Image> activityImages) {
        AppHelper.activityImages = activityImages;
    }

    public static String getCourseName() {
        return courseName;
    }

    public static void setCourseName(String courseName) {
        AppHelper.courseName = courseName;
    }

    public static Typeface getTypeFace(Context context){

            return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/NeoTech-Light.otf");
    }

    public static Typeface getTypeFaceAr(Context context){

        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/GE_SS_Two_Light.otf");
    }

    public static Typeface getTypeFaceBold(Context context){

            return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/NeoTech-Bold.otf");
    }

    public static Typeface getTypeFaceBoldAr(Context context){

        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/GE_SS_Two_Bold.otf");
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }





    public static String getArabicDay(String day){
        String arabicDay="";
        switch (day.toLowerCase()){

            case "monday":
                arabicDay="الإثنين";
                break;
            case "tuesday":
                arabicDay="الثلاثاء";
                break;
            case "wednesday":
                arabicDay="الاربعاء";
                break;
            case "thursday":
                arabicDay="الخميس";
                break;
            case "friday":
                arabicDay="الجمعة";
                break;
            case "saturday":
                arabicDay="السبت";
                break;
            case "sunday":
                arabicDay="الاحد";
                break;
            default:
                arabicDay=day;
        }
        return arabicDay;

    }



    public static String replaceArabicMonth(String oldMonth){
       String newMonth=oldMonth;
       if(oldMonth.contains("يناير")){
           newMonth=oldMonth.replaceAll("يناير" , "كانون الثاتي");
       }else if(oldMonth.contains("فبراير")){
           newMonth=oldMonth.replaceAll("فبراير" , "شباط");
       }
       else if(oldMonth.contains("مارس")){
           newMonth=oldMonth.replaceAll("مارس" , "آذَار");
       }
       else if(oldMonth.contains("أبريل") || oldMonth.contains("إبريل")){
           newMonth=oldMonth.replaceAll("أبريل" , "نيسان");
           newMonth=newMonth.replaceAll("إبريل" , "نيسان");
       }
       else if(oldMonth.contains("مايو")){
           newMonth=oldMonth.replaceAll("مايو" , "أَيَّار");
       }
       else if(oldMonth.contains("يونيو") || oldMonth.contains("يونية")){
           newMonth=oldMonth.replaceAll("يونيو" , "حزيران");
           newMonth=newMonth.replaceAll("يونية" , "حزيران");
       }
       else if(oldMonth.contains("يوليو") || oldMonth.contains("يولية")){
           newMonth=oldMonth.replaceAll("يوليو" , "تَمُّوز");
           newMonth=newMonth.replaceAll("يولية" , "تَمُّوز");
       }
       else if(oldMonth.contains("أغسطس")){
           newMonth=oldMonth.replaceAll("أغسطس" , "آب");
       }
       else if(oldMonth.contains("سبتمبر")){
           newMonth=oldMonth.replaceAll("سبتمبر" , "أيلول");
       }
       else if(oldMonth.contains("أكتوبر")){
           newMonth=oldMonth.replaceAll("أكتوبر" , "تشرين الأول");
       }
       else if(oldMonth.contains("نوفمبر")){
           newMonth=oldMonth.replaceAll("نوفمبر" , "تشرين الثاني");
       }
       else if(oldMonth.contains("ديسمبر")){
           newMonth=oldMonth.replaceAll("ديسمبر" , "كانون الأول");
       }

        return newMonth;
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }



    public static void register(final Context context, final String DeviceToken) {

        TelephonyHelper telephonyHelper = new TelephonyHelper();
        String deviceId = telephonyHelper.getDeviceID(context);
        String deviceModel = telephonyHelper.getDeviceModel();
        String deviceName = telephonyHelper.getDeviceName();

        Log.wtf("deviceId",deviceId);
        String appVersion = BuildConfig.VERSION_CODE+"";
        String MNC = telephonyHelper.getMNC(context);
        String MCC = telephonyHelper.getMCC(context);
        String deviceVersion = telephonyHelper.getDeviceVersion();


        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .addDevice(new JsonParameters(
                        deviceId,
                        DeviceToken,
                        deviceName+" "+deviceModel+ " "+deviceVersion,
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


                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                Log.wtf("register register1", "failed");
                call.cancel();
            }
        });












    }



    public static void resetLanguage(Context context){
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }




    public static void setImage(Context context, ImageView img, String ImgUrl){
        Glide.with(context)
                .load(ImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .dontAnimate().into(img);

    }




    public static void setMargins (Context context,View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {

            int leftInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, left, context.getResources()
                            .getDisplayMetrics());
            int topInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, top, context.getResources()
                            .getDisplayMetrics());

            int rightInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, right, context.getResources()
                            .getDisplayMetrics());

            int bottomInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, bottom, context.getResources()
                            .getDisplayMetrics());

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(leftInDp, topInDp, rightInDp, bottomInDp);
            view.requestLayout();
        }
    }


}
