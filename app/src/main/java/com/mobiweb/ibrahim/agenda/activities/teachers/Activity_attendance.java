package com.mobiweb.ibrahim.agenda.activities.teachers;




import com.mobiweb.ibrahim.agenda.Adapters.AdapterAttendance;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RGOnChangeListener;
import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterTeacherCourses;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_main;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.director.agenda.Activity_all_teachers;
import com.mobiweb.ibrahim.agenda.models.Student;
import com.mobiweb.ibrahim.agenda.models.entities.PostAttendance;
import com.mobiweb.ibrahim.agenda.models.json.JsonClassStudents;
import com.mobiweb.ibrahim.agenda.models.json.JsonDate;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonResponse;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.InternetConnection;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.mobiweb.ibrahim.agenda.utils.RetrofitInterface;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ibrahim on 11/13/2017.
 */

public class Activity_attendance extends ActivityBase implements RGOnChangeListener {
    private RecyclerView rvEvaluations;
    private LinearLayout progress;
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight;
    private Dialog logoutDialog;
    private CustomTextView btYes;
    private CustomTextView btNo;

    private boolean isFilterOpen=false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String selectedDate;

    private Dialog studentsDialog;
    private CardView cardFilter;
    private Dialog responseDialog;
    private Button btOk;
    private CustomTextView ctvDialogMessage;


    private  LinearLayout linearProgressDialog;
    private Activity activity;
    private String dateNow,mDayLetters;
    private RecyclerView rvAttendance;
    private AdapterAttendance adapterAttendance;
    private ImageView ivDate;
    private Button btAddEdit;
    private CustomTextViewBold ctvdate;
    public static final String[] MONTHS = {"كانون الثاني", "شباط", "آذار", "نيسان", "أيار", "حزيران", "تموز", "آب", "أيلول", "تشرين الأول", "تشرين الثاني", "كانون الأول"};
    private String displayDate="اليوم";
    private LinearLayout btSelectAll;
    private Date DateNow,SelectedDate;
    private TextView tvCannotAdd;
    private TextView tvCardClassName;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        // Toast.makeText(getApplication(),getIntent().getStringExtra(AppConstants.TEACHER_ID),Toast.LENGTH_LONG).show();
        init();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_attendance.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setImageResource(R.drawable.home);


        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
                    if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null) {
                        startActivity(new Intent(Activity_attendance.this, Activity_direction_home.class));
                    }else {
                        startActivity(new Intent(Activity_attendance.this, Activity_all_teachers.class));
                    }
                }
                else
                    startActivity(new Intent(Activity_attendance.this,Activity_teacher.class));
                finish();
            }
        });
        toolbarTitle.setText(getString(R.string.attendance));
        toolbarTitleAr.setText(getString(R.string.attendance_ar));


        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        mDayLetters=c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month, day-1);
        DateNow=date;
        SelectedDate=date;
        String dayOfWeek = simpledateformat.format(date);
        dateNow=year + "-" + (month+1)  + "-" + (day<10?("0"+day):(day));
        selectedDate=dateNow;
        ivDate=(ImageView)findViewById(R.id.ivCalendar);
        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


        retreiveClassStudents(dateNow);
        btAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedAttendance(selectedDate);
            }
        });

    }

    @Override
    protected void onResume() {
        if(!InternetConnection.checkConnection(this)){
            showDialog("Please check your internet connection");
        }else
           checkDate();
        super.onResume();
    }

    public void checkDate() {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .getServerDate();
        call1.enqueue(new Callback<JsonDate>() {
            @Override
            public void onResponse(Call<JsonDate> call, Response<JsonDate> response) {

                try {
                    Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                   String phoneDate=year + "-" + (month+1<10?"0"+(month+1):(month+1))  + "-" + (day<10?("0"+day):(day));
                   if (!response.body().getServer_date().matches(phoneDate))
                       showDialog("Please check your phone date");

                } catch (Exception e) {
                    Log.wtf("exception","exception");
                }
            }

            @Override
            public void onFailure(Call<JsonDate> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void showDialog(String message){
        final Dialog responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        Button btOk=(Button)responseDialog.findViewById(R.id.btOk);
        CustomTextView ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        responseDialog.setCancelable(false);
        ctvDialogMessage.setText(message);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseDialog.dismiss();
                finish();

            }
        });



        responseDialog.show();
    }

    private void init(){

        tvCardClassName=findViewById(R.id.tvCardClassName);
        tvCardClassName.setText(AppHelper.getClass_name());

        rvEvaluations=(RecyclerView)findViewById(R.id.rvEvaluation);
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ivRight=(ImageView)findViewById(R.id.ivRight);
        progress=(LinearLayout)findViewById(R.id.progress);
        ctvdate=(CustomTextViewBold)findViewById(R.id.ctvDate);
        ctvdate.setText("اليوم");
        activity=this;

        rvAttendance=(RecyclerView)findViewById(R.id.rvAttendance);
        btAddEdit=(Button)findViewById(R.id.btAddEdit);
        btSelectAll=(LinearLayout)findViewById(R.id.linearSelectAll);
        tvCannotAdd=(TextView)findViewById(R.id.tvCannotAdd);

    }


    private void showDatePicker(){
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
                        SelectedDate =date;
                        String dayOfWeek = simpledateformat.format(date);
                        //selectedDate=year + "-" + (monthOfYear+1<10?("0"+monthOfYear+1):(monthOfYear+1))  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                        selectedDate=year + "-" + (monthOfYear+1)  + "-" + (dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth));
                        ctvdate.setText(AppHelper.getArabicDay(dayOfWeek) + " " + (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + " " + MONTHS[monthOfYear] + " " + year);

                        retreiveClassStudents(selectedDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }









    public void retreiveClassStudents(String date) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .get_class_students(new JsonParameters(4,
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        date,
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType()

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


    private void onStudentsRetreived(JsonClassStudents classStudents){
        progress.setVisibility(View.GONE);

/*        ArrayList<Student> array=new ArrayList<>();
        array.addAll(classStudents.getStudents());
        Locale arabic = new Locale("ar");
        final Collator arabicCollator = Collator.getInstance(arabic);
         Collections.sort(array, new Comparator<Student>() {
            @Override
            public int compare(Student one, Student two) {
                             return arabicCollator.compare(one.getName(), two.getName());
            }

        });*/



        adapterAttendance=new AdapterAttendance(classStudents.getStudents(),this);
        GridLayoutManager glm=new GridLayoutManager(this,1);
        rvAttendance.setAdapter(adapterAttendance);
        rvAttendance.setLayoutManager(glm);

        btSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllSelected();
            }
        });

        if(SelectedDate.compareTo(DateNow)>0
        || (!((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION) && SelectedDate.compareTo(DateNow)<0)
        || (!((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION) && SelectedDate.compareTo(DateNow)==0 && classStudents.getIsEdit().matches("1"))
         ) {
            btAddEdit.setVisibility(View.GONE);
            tvCannotAdd.setVisibility(View.VISIBLE);
        }
        else {
            btAddEdit.setVisibility(View.VISIBLE);
            tvCannotAdd.setVisibility(View.GONE);
        }

    }


    private void setAllSelected(){
        for (int i=0;i<adapterAttendance.getinfoStudents().size();i++){
            adapterAttendance.getinfoStudents().get(i).setIsPresent("1");
        }
        adapterAttendance.notifyDataSetChanged();
    }


    private ArrayList<PostAttendance> getPostAttendance(){
        ArrayList<PostAttendance> arrayAttendance=new ArrayList<>();
        PostAttendance p;
        for (int i=0;i<adapterAttendance.getinfoStudents().size();i++){
            p=new PostAttendance(adapterAttendance.getinfoStudents().get(i).getIdStudent(),adapterAttendance.getinfoStudents().get(i).getIsPresent());
            arrayAttendance.add(p);
        }
        return arrayAttendance;
    }


    public void updatedAttendance(String date) {
        progress.setVisibility(View.VISIBLE);
        Call call1 = RetrofitClient.getClient().create(RetrofitInterface.class)
                .update_attendance(new JsonParameters(1,
                        AppHelper.getId_class(),
                        AppHelper.getId_section(),
                        date,
                        ((Agenda)getApplication()).getLoginId(),
                        ((Agenda)getApplication()).getCashedType(),
                        getPostAttendance()

                ));
        call1.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {

                try {
                    onAttendanceUpdated(response.body());

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
    
    
    private void onAttendanceUpdated(JsonResponse jsonResponse){
        progress.setVisibility(View.GONE);
        if(jsonResponse.getStatus().matches("success")){
            popUpMessage(true);
        }else {
            popUpMessage(false);
        }
    }

    private void popUpMessage(final boolean isSuccess){

        responseDialog = new Dialog(this);
        responseDialog.setContentView(R.layout.popup_response);
        btOk=(Button)responseDialog.findViewById(R.id.btOk);
        ctvDialogMessage=(CustomTextView)responseDialog.findViewById(R.id.ctvMessage);
        responseDialog.setCanceledOnTouchOutside(false);
        if(isSuccess) {
            ctvDialogMessage.setText("added successfully");
        }
        else {
            ctvDialogMessage.setText("Failed,Please Try Again.");
        }
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSuccess) {
                    if(((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) {
                        if(getIntent().getStringExtra(AppConstants.INTENT_ACTIVITY)!=null) {
                            startActivity(new Intent(Activity_attendance.this, Activity_direction_home.class));
                        }else {
                            startActivity(new Intent(Activity_attendance.this, Activity_Teacher_Homeworks.class));
                        }
                    }
                    else
                        startActivity(new Intent(Activity_attendance.this,Activity_Teacher_Homeworks.class));

                    finish();
                }
                else {
                    responseDialog.dismiss();

                }
            }
        });



        responseDialog.show();
    }

    @Override
    public void onRadioChanged(View view, int position, RadioGroup rg,int rgValue) {

       try {
           if (rg.getCheckedRadioButtonId() == R.id.rdPresent) {
               adapterAttendance.getinfoStudents().get(position).setIsPresent("1");
           } else {
               adapterAttendance.getinfoStudents().get(position).setIsPresent("0");
           }

           adapterAttendance.notifyItemChanged(position);
       }catch (Exception e){}
     }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
