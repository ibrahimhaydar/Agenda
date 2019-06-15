package com.mobiweb.ibrahim.agenda.utils;

import com.mobiweb.ibrahim.agenda.models.entities.Result;
import com.mobiweb.ibrahim.agenda.models.entities.ViewExam;
import com.mobiweb.ibrahim.agenda.models.json.JsonActivities;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonAgenda;
import com.mobiweb.ibrahim.agenda.models.json.JsonAllExamsDetails;
import com.mobiweb.ibrahim.agenda.models.json.JsonAllTeachers;
import com.mobiweb.ibrahim.agenda.models.json.JsonAnnouncement;
import com.mobiweb.ibrahim.agenda.models.json.JsonAttendance;
import com.mobiweb.ibrahim.agenda.models.json.JsonClassStudents;
import com.mobiweb.ibrahim.agenda.models.json.JsonEditExam;
import com.mobiweb.ibrahim.agenda.models.json.JsonEvaluation;
import com.mobiweb.ibrahim.agenda.models.json.JsonExamsCategory;
import com.mobiweb.ibrahim.agenda.models.json.JsonGetAllClasses;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;
import com.mobiweb.ibrahim.agenda.models.json.JsonResponse;
import com.mobiweb.ibrahim.agenda.models.json.JsonScheduleDetails;
import com.mobiweb.ibrahim.agenda.models.json.JsonSchedules;
import com.mobiweb.ibrahim.agenda.models.json.JsonTeacherClasses;
import com.mobiweb.ibrahim.agenda.models.json.JsonTeacherCourses;
import com.mobiweb.ibrahim.agenda.models.json.JsonUser;
import com.mobiweb.ibrahim.agenda.models.json.JsonVideos;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface RetrofitInterface {

    @POST("getAllClasses.php")
    Call<JsonGetAllClasses> getClasses(@Body JsonParameters parameters);


    @POST("getTeachers.php")
    Call<JsonAllTeachers> getTeachers(@Body JsonParameters parameters);

    @POST("getAgenda.php")
    Call<JsonAgenda> getAgenda(@Body JsonParameters parameters);

    @POST("check_user.php")
    Call<JsonUser> checkUser(@Body JsonParameters parameters);

    @POST("getCourses.php")
    Call<JsonTeacherCourses> getCourses(@Body JsonParameters parameters);


    @POST("getClasses.php")
    Call<JsonTeacherClasses> getTeacherClasses(@Body JsonParameters parameters);

    @POST("addHomework.php")
    Call<JsonAddHw> addHw(@Body JsonParameters parameters);

    @Multipart
    @POST("addhw.php")
    Call<Result> uploadImage(
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4,
            @Part MultipartBody.Part file5,
            @Part("id_class") RequestBody id_class,
            @Part("id_section") RequestBody id_section,
            @Part("hw_date") RequestBody hw_date,
            @Part("id_teacher") RequestBody id_teacher,
            @Part("id_course") RequestBody id_course,
            @Part("hw_title") RequestBody hw_title,
            @Part("hw_desc") RequestBody hw_desc,
            @Part("hw_image") RequestBody hw_image,
            @Part("hw_info") RequestBody hw_info


    );



    @Multipart
    @POST("edithw.php")
    Call<Result> editHw(
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4,
            @Part MultipartBody.Part file5,
            @Part("id_agenda") RequestBody id_agenda,
            @Part("hw_title") RequestBody hw_title,
            @Part("hw_desc") RequestBody hw_desc,
            @Part("hw_date") RequestBody hw_date,
            @Part("hw_image") RequestBody hw_image,
            @Part("hw_info") RequestBody hw_info


    );




    @POST("deleteHomework.php")
    Call<JsonAddHw> deleteHomework(@Body JsonParameters parameters);

    @POST("getTeacherHw.php")
    Call<JsonAgenda> getTeacherHw(@Body JsonParameters parameters);

    @POST("AddDevice.php")
    Call<JsonAddHw> addDevice(@Body JsonParameters parameters);



    @POST("get_activities.php")
    Call<JsonActivities> getActivities(@Body JsonParameters parameters);

    @POST("add_activity.php")
    Call<Result> addActivity(@Body JsonParameters parameters);


    @POST("add_announcement.php")
    Call<Result> addAnnouncement(@Body JsonParameters parameters);


    @POST("exams_add_course_exam.php")
    Call<Result> exams_add_course_exam(@Body JsonParameters parameters);


    @POST("edit_activity.php")
    Call<Result> editActivity(@Body JsonParameters parameters);

    @POST("edit_announcement.php")
    Call<Result> editAnnouncement(@Body JsonParameters parameters);



        @Multipart
        @POST("add_activity_images.php")
        Call<Result> addActivityImages(
                @Part List<MultipartBody.Part> files,
                @Part("id_activity") RequestBody id_activity);


    @Multipart
    @POST("add_announcement_images.php")
    Call<Result> addAnnouncementImages(
            @Part List<MultipartBody.Part> files,
            @Body JsonParameters parameters
            );


    @POST("delete_activity_image.php")
    Call<JsonAddHw> deleteActivityImage(@Body JsonParameters parameters);

    @POST("delete_activity.php")
    Call<JsonAddHw> deleteActivity(@Body JsonParameters parameters);

    @POST("delete_video.php")
    Call<JsonAddHw> deleteVideo(@Body JsonParameters parameters);

    @POST("delete_announcement.php")
    Call<JsonAddHw> deleteAnnouncement(@Body JsonParameters parameters);


    @POST("get_announcement.php")
    Call<JsonAnnouncement> getAnnouncements(@Body JsonParameters parameters);

    @POST("getVideos.php")
    Call<JsonVideos> getVideos(@Body JsonParameters parameters);


    @Multipart
    @POST("addVideo.php")
    Call<Result> addvideo(
                    @Part MultipartBody.Part file,
                    @Part MultipartBody.Part file2,
                    @Part("title") RequestBody id_class,
                    @Part("description") RequestBody id_section

            );

    @Multipart
    @POST("editvideo.php")
    Call<Result> editvideo(
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part("id_video") RequestBody id_video,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("old_url") RequestBody old_url,
            @Part("old_thumb") RequestBody old_thumb

    );


    @Multipart
    @POST("add_schedule.php")
    Call<Result> addSchedule(
            @Part MultipartBody.Part file,
            @Part("id_class") RequestBody id_class,
            @Part("id_section") RequestBody id_Section
  );



    @POST("get_schedule.php")
    Call<JsonSchedules> getSchedule(@Body JsonParameters parameters);




    @Multipart
    @POST("add_teacher_schedule.php")
    Call<Result> addScheduleTeacher(
            @Part MultipartBody.Part file,
            @Part("id_teacher") RequestBody id_teacher

    );



    @POST("get_teacher_schedule.php")
    Call<JsonSchedules> getScheduleTeacher(@Body JsonParameters parameters);





    @POST("exams_add_general_info.php")
    Call<Result> exams_add_general_info(@Body JsonParameters parameters);

    @POST("exams_get_category_types.php")
    Call<JsonExamsCategory> exams_get_category_types(@Body JsonParameters parameters);

    @POST("exams_view_general_info.php")
    Call<JsonEditExam> exams_view_general_info(@Body JsonParameters parameters);

    @POST("exams_edit_general_info.php")
    Call<Result> exams_edit_general_info(@Body JsonParameters parameters);

    @POST("exams_add_exam_schedule.php")
    Call<Result> exams_add_exam_schedule(@Body JsonParameters parameters);
    @POST("exams_edit_exam_schedule.php")
    Call<Result> exams_edit_exam_schedule(@Body JsonParameters parameters);

    @POST("exams_view_exam_schedule.php")
    Call<JsonScheduleDetails> exams_view_exam_schedule(@Body JsonParameters parameters);



    @POST("exams_view_course_exam.php")
    Call<ViewExam> exams_view_course_exam(@Body JsonParameters parameters);


    @POST("exams_delete_course_exam.php")
    Call<JsonAddHw> exams_delete_course_exam(@Body JsonParameters parameters);


    @POST("exams_view_exam_all_details.php")
    Call<JsonAllExamsDetails> exams_view_exam_all_details(@Body JsonParameters parameters);

    @POST("getEvaluations.php")
    Call<JsonEvaluation> getEvaluations(@Body JsonParameters parameters);

    @POST("get_class_students.php")
    Call<JsonClassStudents> get_class_students(@Body JsonParameters parameters);


    @POST("get_class_evaluation.php")
    Call<JsonClassStudents> get_class_evaluation(@Body JsonParameters parameters);


    @POST("add_evaluation.php")
    Call<JsonResponse> add_evaluation(@Body JsonParameters parameters);


    @POST("editEvaluation.php")
    Call<JsonResponse> editEvaluation(@Body JsonParameters parameters);


    @POST("update_attendance.php")
    Call<JsonResponse> update_attendance(@Body JsonParameters parameters);


    @POST("get_class_grades.php")
    Call<JsonClassStudents> get_class_grades(@Body JsonParameters parameters);

    @POST("update_grades.php")
    Call<JsonResponse> update_grades(@Body JsonParameters parameters);


    @POST("get_student_attendance.php")
    Call<JsonAttendance> get_student_attendance(@Body JsonParameters parameters);
}
