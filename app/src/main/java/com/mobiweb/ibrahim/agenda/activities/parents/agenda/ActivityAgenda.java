package com.mobiweb.ibrahim.agenda.activities.parents.agenda;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.Adapter_agenda;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.models.json.JsonAgenda;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/8/2017.
 */

public class ActivityAgenda extends Activity implements RVOnItemClickListener{
    private LinearLayout progress,nodata,linearToolbar;
    private CustomTextViewBold toolbarTitle,ctvdate;
    private ImageView ivDate;
    private ImageView ivBack,ivRight;
    private RecyclerView rvAgenda;
    private Adapter_agenda adapter_agenda;
    private String id_class,id_section,class_name;
    private Activity activity;
    private CustomTextViewBoldAr toolbarTitleAr;


    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dateNow,mDayLetters;
    public static final String[] MONTHS = {"كانون الثاني", "شباط", "آذار", "نيسان", "أيار", "حزيران", "تموز", "آب", "أيلول", "تشرين الأول", "تشرين الثاني", "كانون الأول"};
    private String selectedDate;
    private String arabicDay="";
    private String displayDate="";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        init();
        toolbarTitleAr.setVisibility(View.GONE);


        Calendar c = Calendar.getInstance();
        int DayToday = c.get(Calendar.DAY_OF_WEEK);
        Date today = c.getTime();
        if(DayToday==c.FRIDAY)
          c.add(Calendar.DAY_OF_YEAR, 3);
        else if(DayToday==c.SATURDAY)
          c.add(Calendar.DAY_OF_YEAR, 2);
        else
          c.add(Calendar.DAY_OF_YEAR, 1);

        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        mDayLetters=c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month, day-1);
        String dayOfWeek = simpledateformat.format(date);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        //  dateNow = year + "-" +(month<10?("0"+month):(month)) + "-" + (day<10?("0"+day):(day)) ;
       // dateNow=year + "-" + (month+1<10?("0"+month+1):(month+1))  + "-" + (day<10?("0"+day):(day)) + "  " +dayOfWeek;
        dateNow=year + "-" + (month+1)  + "-" + (day<10?("0"+day):(day));
        ctvdate.setText(getArabicDay(dayOfWeek) + " " + (day < 10 ? ("0" + day) : (day)) + " " + MONTHS[month] + " " + year);
        selectedDate=year + "-" + (month+1<10?("0"+month+1):(month+1))  + "-" + (day<10?("0"+day):(day)) + "  " +dayOfWeek;



        retreiveAgenda(id_class,id_section,dateNow);



        toolbarTitle.setText(class_name);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityAgenda.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                activity.getApplicationContext().getResources().updateConfiguration(config, null);
                startActivity(new Intent(ActivityAgenda.this,Activity_main.class));
            }
        });
        ivRight.setVisibility(View.GONE);

        ivDate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                        Date date = new Date(year, monthOfYear, dayOfMonth-1);
                        String dayOfWeek = simpledateformat.format(date);




                /*        if(dateNow.matches(year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)))){
                             ctvdate.setText("اليوم");
                            displayDate="اليوم";

                        }
                        else {*/
                            ctvdate.setText(getArabicDay(dayOfWeek) + " " + (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + " " + MONTHS[monthOfYear] + " " + year);
                            displayDate=getArabicDay(dayOfWeek) + " " + (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + " " + MONTHS[monthOfYear] + " " + year;
                      //  }
                        selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                       retreiveAgenda(id_class,id_section,selectedDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
});



    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init(){
        progress=(LinearLayout)findViewById(R.id.progress);
        nodata=(LinearLayout)findViewById(R.id.nodata);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        rvAgenda = (RecyclerView) findViewById(R.id.rvAgenda);

        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        class_name=getIntent().getStringExtra(AppConstants.CLASS_NAME);

        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
        activity=this;
        linearToolbar=(LinearLayout)findViewById(R.id.linearToolbar);
        linearToolbar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        Locale locale = new Locale("ar_LB");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public void retreiveAgenda(String id_class,String id_section,String date) {
        progress.setVisibility(View.VISIBLE);
        Log.wtf("id_class",id_class);
        Log.wtf("id_section",id_section);
        Log.wtf("date",date);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getAgenda(new JsonParameters(id_class,id_section,date));
        call1.enqueue(new Callback<JsonAgenda>() {
            @Override
            public void onResponse(Call<JsonAgenda> call, Response<JsonAgenda> response) {

                try {
                    onDataRetrieved(response.body());
                   // Log.wtf("className", response.body().getAgenda().get(1).getHwDesc());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonAgenda> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onDataRetrieved(JsonAgenda agenda){

        progress.setVisibility(View.GONE);


           if (agenda.getStatus().matches("empty")) {
               nodata.setVisibility(View.VISIBLE);
           }else {
               nodata.setVisibility(View.GONE);
               adapter_agenda = new Adapter_agenda(agenda.getAgenda(), this);
               GridLayoutManager glm = new GridLayoutManager(this, 1);
               rvAgenda.setLayoutManager(glm);
               rvAgenda.setAdapter(adapter_agenda);
           }



    }


    @Override
    public void onItemClicked(View view, int position) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);

        Intent i=new Intent(ActivityAgenda.this,Activity_inside_agenda.class);
        i.putExtra(AppConstants.HW_TITLE,adapter_agenda.getclasses().get(position).getHwTitle());
        i.putExtra(AppConstants.HW_DESC,adapter_agenda.getclasses().get(position).getHwDesc());
        i.putExtra(AppConstants.HW_DATE,displayDate);
        i.putStringArrayListExtra(AppConstants.HW_IMAGE, adapter_agenda.getclasses().get(position).getHwImage());
       // i.putExtra(AppConstants.HW_IMAGE,adapter_agenda.getclasses().get(position).getHw_image());
        i.putExtra(AppConstants.HW_INFO,adapter_agenda.getclasses().get(position).getHw_info());
        startActivity(i);


    }

    private String getArabicDay(String day){

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

    @Override
    public void onBackPressed() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
        super.onBackPressed();
    }
}
