package com.mobiweb.ibrahim.agenda.models.json;

import com.google.gson.annotations.SerializedName;
import com.mobiweb.ibrahim.agenda.models.entities.Evaluation;
import com.mobiweb.ibrahim.agenda.models.entities.InfoStudent;
import com.mobiweb.ibrahim.agenda.models.entities.PostAttendance;
import com.mobiweb.ibrahim.agenda.models.entities.PostClasses;
import com.mobiweb.ibrahim.agenda.models.entities.PostDeletedImages;
import com.mobiweb.ibrahim.agenda.models.entities.PostEvaluation;
import com.mobiweb.ibrahim.agenda.models.entities.PostGrades;
import com.mobiweb.ibrahim.agenda.models.entities.ScheduleExamPost;

import java.util.ArrayList;

/**
 * Created by Ibrahim on 6/13/2017.
 */

public class JsonParameters {

    @SerializedName("id_parent")
    private String id_parent;

    @SerializedName("id_class")
    private String id_class;
    @SerializedName("id_section")
    private String id_section;
    @SerializedName("hw_date")
    private String hw_date;

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    @SerializedName("id_teacher")
    private String id_teacher;

    @SerializedName("id_course")
    private String id_course;

    @SerializedName("hw_title")
    private String hw_title;
    @SerializedName("hw_desc")
    private String hw_desc;
    @SerializedName("loginType")
    private String loginType;
    @SerializedName("id_agenda")
    private String id_agenda;

    @SerializedName("id_direction")
    private String id_direction;


    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("deviceToken")
    private String deviceToken;

    @SerializedName("deviceName")
    private String deviceName;

    @SerializedName("platformId")
    private String platformId;

    @SerializedName("hw_image")
    private String hw_image;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("id_activity")
    private String id_activity;

    @SerializedName("id_activity_image")
    private String id_activity_image;

    @SerializedName("page_number")
    private String page_number;
    @SerializedName("page_size")
    private String page_size;

    @SerializedName("id_user")
    private String id_user;



    @SerializedName("id_announcement")
    private String id_announcement;
    @SerializedName("id_video")
    private String id_video;


    @SerializedName("note")
    private String note;
    @SerializedName("more")
    private String more;
    @SerializedName("id_exam_category")
    private String id_exam_category;

    @SerializedName("id_exam")
    private String id_exam;

    @SerializedName("latest_year")
    private String latest_year;

    @SerializedName("desc")
    private String desc;

    @SerializedName("date")
    private String date;
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;

    @SerializedName("activity_date")
    private String activity_date;

    @SerializedName("is_all_details")
    private String isAllDetails;



    @SerializedName("appVersion")
    private String appVersion;
    @SerializedName("MNC")
    private String MNC;
    @SerializedName("MCC")
    private String MCC;
    @SerializedName("deviceVersion")
    private String deviceVersion;

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("user_type")
    private String user_type;
    @SerializedName("filter_id")
    private String filter_id;
    @SerializedName("filter_info")
    private String filter_info;


    @SerializedName("arrayPost")
    private ArrayList<ScheduleExamPost> arrayPost;

    @SerializedName("arrayEvaluation")
    private ArrayList<PostEvaluation> arrayEvaluation;

    @SerializedName("arrayStudents")
    private ArrayList<PostAttendance> arrayAttendance;

    @SerializedName("arrayGrades")
    private ArrayList<PostGrades> arrayGrades;

    @SerializedName("is_filter")
    private String is_filter;

    @SerializedName("arrayClasses")
    private ArrayList<PostClasses> arrayClasses;

    @SerializedName("array_id_deleted")
    private ArrayList<PostDeletedImages> array_id_deleted;


    @SerializedName("id_student")
    private String id_student;

    @SerializedName("withImage")
    private String withImage;


/*.................retreive all classes.................*/
    public JsonParameters(String id,boolean isTeacher) {
       if(isTeacher)
         this.id_teacher = id;
        else
           this.id_parent = id;
    }


    /*...............retreive agenda................*/

    public JsonParameters(String id_class, String id_section, String hw_date) {
        this.id_class = id_class;
        this.id_section = id_section;
        this.hw_date = hw_date;
    }


    public JsonParameters(String param1, String param2, int type) {
       if(type==1) {
           this.id_class = param1;
           this.id_section = param2;
       }

    }




        /*...............rget teacher hw................*/

    public JsonParameters(String id_class, String id_section, String hw_date,String id_teacher) {
        this.id_class = id_class;
        this.id_section = id_section;
        this.hw_date = hw_date;
        this.id_teacher=id_teacher;
    }

            /*...............Add device................*/

    public JsonParameters(String deviceId, String deviceToken, String deviceName,String platformId,String appVersion,String MNC,String MCC,String deviceVersion,int type) {
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.deviceName = deviceName;
        this.platformId=platformId;
        this.appVersion=appVersion;
        this.MNC=MNC;
        this.MCC=MCC;
        this.deviceVersion=deviceVersion;
    }

    /*...............check user................*/
    public JsonParameters(String username, String password,String type,String deviceId,boolean isLogin,int value) {
        this.username = username;
        this.password = password;
        this.loginType=type;
        this.deviceId=deviceId;
    }


    /*..........get teacher classes...........*/
    public JsonParameters(String id_teacher, String id_course,boolean isTeacher) {
      this.id_teacher=id_teacher;
        this.id_course=id_course;
    }


    /*..........add hw.................*/

    public JsonParameters(String id_class, String id_section, String hw_date, String id_teacher, String id_course, String hw_title, String hw_desc,String hw_image) {
        this.id_class = id_class;
        this.id_section = id_section;
        this.hw_date = hw_date;
        this.id_teacher = id_teacher;
        this.id_course = id_course;
        this.hw_title = hw_title;
        this.hw_desc = hw_desc;
        this.hw_image=hw_image;
    }


        /*..........edit hw.................*/

    public JsonParameters(String id_agenda, String hw_title, String hw_desc, String hw_date,boolean edit) {
        this.id_agenda=id_agenda;
        this.hw_title=hw_title;
        this.hw_desc=hw_desc;
        this.hw_date=hw_date;
    }

    public JsonParameters(String id_agenda) {
        this.id_agenda=id_agenda;

    }

    public JsonParameters(String id_direction,int type) {
        this.id_direction=id_direction;

    }

    public JsonParameters(String id_direction, String title, String description,int type) {
        this.id_direction = id_direction;
        this.title = title;
        this.description = description;
    }

    public JsonParameters(String id_direction, String title, String description, String is_filter, ArrayList<PostClasses> arrayClasses) {
        this.id_direction = id_direction;
        this.title = title;
        this.description = description;
        this.is_filter=is_filter;
        this.arrayClasses=arrayClasses;
    }

    public JsonParameters(String id_direction, String title, String description, String is_filter, ArrayList<PostClasses> arrayClasses,String withImage) {
        this.id_direction = id_direction;
        this.title = title;
        this.description = description;
        this.is_filter=is_filter;
        this.arrayClasses=arrayClasses;
        this.withImage=withImage;
    }

    public JsonParameters(String id_announcement, String id_direction, String title, String description, String is_filter, ArrayList<PostClasses> arrayClasses, String withImage, ArrayList<PostDeletedImages> postDeletedImages) {
       this.id_announcement=id_announcement;
        this.id_direction = id_direction;
        this.title = title;
        this.description = description;
        this.is_filter=is_filter;
        this.arrayClasses=arrayClasses;
        this.withImage=withImage;
        this.array_id_deleted=postDeletedImages;
    }


    public JsonParameters(String id_direction, String title, String description,String id,int type,boolean isEdit) {
        this.id_direction = id_direction;
        this.title = title;
        this.description = description;
        if(type==1)
          this.id_activity=id;
        else if(type==2)
            this.id_announcement=id;
    }

    public JsonParameters(String param, int type,boolean isDelete) {
        if(type==1)
            this.id_activity_image=param;

        else if(type==2)
            this.id_activity=param;

        else if(type==3)
            this.id_announcement=param;
        else if(type==4)
            this.id_video=param;
    }


    public JsonParameters(String param, String pageNumber,String pageSize,int type,boolean paging) {
       this.page_number=pageNumber;
        this.page_size=pageSize;

       if(type==1)
            this.id_user=param;
    }



    public JsonParameters(int type,String param) {
       if(type==1)
            this.id_exam=param;
       else if(type==2){
           this.id_teacher=param;
       }
    }



    public JsonParameters(String param1, String param2,String param3,String param4,String param5,String param6,int type) {


        if(type==1){
            this.title=param1;
            this.note=param2;
            this.more=param3;
            this.id_class=param4;
            this.id_section=param5;
            this.id_exam_category=param6;
        }else if(type==2){
            this.id_class=param1;
            this.id_section=param2;

            this.user_id=param3;
            this.user_type=param4;
            this.id_course=param5;
            this.id_exam_category=param6;
        }else if(type==3){
            this.id_class=param1;
            this.id_section=param2;
            this.date=param3;
            this.user_id=param4;
            this.user_type=param5;
            this.id_course=param6;
        }

    }


    public JsonParameters(String param1, String param2,String param3,String param4,String param5,String param6,String param7,String param8,String param9,String param10,String param11,int type) {


        if(type==1){
            this.id_class=param1;
            this.id_section=param2;
            this.id_course=param3;
            this.id_exam_category=param4;
            this.id_teacher=param5;
            this.title=param6;
            this.desc=param7;

            this.date=param8;
            this.from=param9;
            this.to=param10;

            this.latest_year=param11;
        }

    }


    public JsonParameters(int type,String param1, String param2,String param3,String param4) {

        if(type==1){
            this.id_user=param1;
            this.id_class=param2;
            this.id_section=param3;
            this.id_exam_category=param4;

        }else if(type==2){
            this.title=param1;
            this.note=param2;
            this.more=param3;
            this.id_exam=param4;
        }else if(type==3){
            this.id_direction = param1;
            this.title = param2;
            this.description = param3;
            this.activity_date=param4;
        }


    }



    public JsonParameters(int type,String param1, String param2,String param3,String param4,String param5) {

        if(type==1){
            this.id_teacher=param1;
            this.id_course=param2;
            this.id_class=param3;
            this.id_section=param4;
            this.id_exam_category=param5;

        }
       else if(type==2){
            this.id_direction = param1;
            this.title = param2;
            this.description = param3;
            this.activity_date = param4;
            this.id_activity=param5;

        }else if(type==3){
            this.id_user=param1;
            this.id_class=param2;
            this.id_section=param3;
            this.id_exam_category=param4;
            this.isAllDetails=param5;

        }else if(type==4){
            this.id_class=param1;
            this.id_section=param2;
            this.date=param3;
            this.user_id=param4;
            this.user_type=param5;

        }

    }


    public JsonParameters(String param1,String param2,String param3,String param4,String param5,ArrayList<ScheduleExamPost> arrayPost) {
        this.id_user=param1;
        this.id_class=param2;
        this.id_section=param3;
        this.id_exam_category=param4;
        this.id_exam=param5;
        this.arrayPost=arrayPost;

    }

    public JsonParameters(ArrayList<ScheduleExamPost> arrayPost) {

        this.arrayPost=arrayPost;

    }


    public JsonParameters(int type,String param1, String param2,String param3,String param4,String param5,String param6,String param7) {
       if(type==1){
           this.id_class=param1;
           this.id_section=param2;
           this.user_id=param3;
           this.user_type=param4;
           this.filter_id=param5;
           this.filter_info=param6;
           this.id_course=param7;
       }
    }



    public JsonParameters(int type,String param1, String param2,String param3,String param4,String param5,String param6,ArrayList<PostEvaluation> param7) {
        if(type==1){
            this.id_class=param1;
            this.id_section=param2;
            this.date=param3;
            this.user_id=param4;
            this.user_type=param5;
            this.id_course=param6;
            this.arrayEvaluation=param7;
        }
    }

    public JsonParameters(int type,String param1, String param2,String param3,String param4,String param5,ArrayList<PostAttendance> param6) {
        if(type==1){
            this.id_class=param1;
            this.id_section=param2;
            this.date=param3;
            this.user_id=param4;
            this.user_type=param5;

            this.arrayAttendance=param6;
        }
    }



    public JsonParameters(String param1, String param2,String param3,String param4,String param5,String param6,ArrayList<PostGrades> param7) {

        this.id_class=param1;
        this.id_section=param2;

        this.user_id=param3;
        this.user_type=param4;
        this.id_exam_category=param5;
        this.id_course=param6;
        this.arrayGrades=param7;

    }



    public JsonParameters(int type,String param1, String param2,String param3) {
        if(type==1) {
            this.id_student = param1;
            this.page_number = param2;
            this.page_size = param3;
        }

    }

}
