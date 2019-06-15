package com.mobiweb.ibrahim.agenda.activities.teachers;

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
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.Adapter_agenda;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonAgenda;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/8/2017.
 */

public class Activity_edit_homework extends ActivityBase implements RVOnItemClickListener{
    private LinearLayout progress,nodata,linearToolbar;
    private CustomTextViewBold toolbarTitle,ctvdate;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivDate;
    private ImageView ivBack,ivRight;
    private RecyclerView rvAgenda;
    private Adapter_agenda adapter_agenda;
    private String id_class,id_section,class_name,id_teacher;
    private Activity activity;

    private ArrayList<com.mobiweb.ibrahim.agenda.models.entities.Agenda> arrayAgenda=new ArrayList<com.mobiweb.ibrahim.agenda.models.entities.Agenda>();

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dateNow,mDayLetters;
    private LinearLayout linearDelete;
    private String selectedDate;
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        init();


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


        dateNow=year + "-" + (month+1)  + "-" + (day<10?("0"+day):(day));
        ctvdate.setText(dayOfWeek + " " + (day < 10 ? ("0" + day) : (day)) + " " + MONTHS[month] + " " + year);
        selectedDate=year + "-" + (month+1<10?("0"+month+1):(month+1))  + "-" + (day<10?("0"+day):(day)) + "  " +dayOfWeek;

        retreiveAgenda(id_class,id_section,dateNow);
        toolbarTitle.setText(getString(R.string.edit_hw));
        toolbarTitleAr.setText(getString(R.string.edit_hw_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_edit_homework.super.onBackPressed();
            }
        });



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



                                ctvdate.setText(dayOfWeek +" "+(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) +" "+MONTHS[monthOfYear]+" "+year);

                                selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                               // selectedDate=year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth)) + "  " +dayOfWeek;

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
        linearDelete=(LinearLayout)findViewById(R.id.linearDelete);
        linearDelete.setVisibility(View.VISIBLE);


        id_class=getIntent().getStringExtra(AppConstants.ClASS_ID);
        id_section=getIntent().getStringExtra(AppConstants.ClASS_SECTION_ID);
        class_name=getIntent().getStringExtra(AppConstants.CLASS_NAME);
        id_teacher=getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE).getString(AppConstants.LOGIN_ID,"");

        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        ctvdate.setText("Today");
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
        activity=this;
        linearToolbar=(LinearLayout)findViewById(R.id.linearToolbar);
        linearToolbar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);


        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION))
                    startActivity(new Intent(Activity_edit_homework.this,Activity_all_teachers.class));
                else
                    startActivity(new Intent(Activity_edit_homework.this,Activity_teacher.class));
            }
        });
    }

    public void retreiveAgenda(String id_class,String id_section,String date) {
        progress.setVisibility(View.VISIBLE);
        Log.wtf("id_class",id_class);
        Log.wtf("id_section",id_section);
        Log.wtf("date",date);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getTeacherHw(new JsonParameters(id_class,id_section,date,id_teacher));
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
            arrayAgenda=agenda.getAgenda();
            nodata.setVisibility(View.GONE);
            adapter_agenda = new Adapter_agenda(arrayAgenda, this,true);
            GridLayoutManager glm = new GridLayoutManager(this, 1);
            rvAgenda.setLayoutManager(glm);
            rvAgenda.setAdapter(adapter_agenda);
        }



    }



    public void deleteAgenda(String idAgenda, final int position) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .deleteHomework(new JsonParameters(idAgenda));
        call1.enqueue(new Callback<JsonAddHw>() {
            @Override
            public void onResponse(Call<JsonAddHw> call, Response<JsonAddHw> response) {

                try {
                    onAgendaDeleted(response.body(),position);
                    // Log.wtf("className", response.body().getAgenda().get(1).getHwDesc());
                } catch (Exception e) {
                    Log.wtf("exception","exception");
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonAddHw> call, Throwable t) {
                call.cancel();
                nodata.setVisibility(View.VISIBLE);
            }
        });
    }


private void onAgendaDeleted(JsonAddHw status,int position){
    progress.setVisibility(View.GONE);
    if(status.getStatus().equals("success")) {
        Toast.makeText(getApplication(), "Homework is deleted successfully", Toast.LENGTH_LONG).show();
        arrayAgenda.remove(position);
        adapter_agenda.notifyDataSetChanged();

    }
    else
        Toast.makeText(getApplication(),"Please try again",Toast.LENGTH_LONG).show();


}

    @Override
    public void onItemClicked(View view, int position) {

        if(view.getId()==R.id.linearDesc) {
            Intent i = new Intent(Activity_edit_homework.this, Activity_edit_hw_inside.class);
            i.putExtra(AppConstants.HW_TITLE, adapter_agenda.getclasses().get(position).getHwTitle());
            i.putExtra(AppConstants.HW_DESC, adapter_agenda.getclasses().get(position).getHwDesc());
            i.putExtra(AppConstants.HW_DATE, adapter_agenda.getclasses().get(position).getHw_date());
            i.putStringArrayListExtra(AppConstants.HW_IMAGE, adapter_agenda.getclasses().get(position).getHwImage());
            i.putExtra(AppConstants.HW_ID, adapter_agenda.getclasses().get(position).getHw_id());

            i.putExtra(AppConstants.ClASS_ID, id_class);
            i.putExtra(AppConstants.ClASS_SECTION_ID, id_section);
            i.putExtra(AppConstants.HW_INFO, adapter_agenda.getclasses().get(position).getHw_info());
           // Toast.makeText(getApplication(),adapter_agenda.getclasses().get(position).getHw_info(),Toast.LENGTH_LONG).show();


            startActivity(i);
        }else if(view.getId()==R.id.linearDelete) {
            deleteAgenda(adapter_agenda.getclasses().get(position).getHw_id(),position);
        }

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
