package com.mobiweb.ibrahim.agenda.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mobiweb.ibrahim.agenda.BuildConfig;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Allclass;
import com.mobiweb.ibrahim.agenda.models.entities.Classes;
import com.mobiweb.ibrahim.agenda.models.entities.Evaluation;
import com.mobiweb.ibrahim.agenda.models.entities.Files;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.InfoStudent;
import com.mobiweb.ibrahim.agenda.models.json.JsonAddHw;
import com.mobiweb.ibrahim.agenda.models.json.JsonParameters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppHelper {
    public static String UserIdretreive;
    public static String courseName,courseId,teacherId,teacher_name,studentId="0",examCategoryName;
    public static ArrayList<Image> activityImages=new ArrayList<>();
    public static String id_class,id_section,id_category_exam,class_name;
    public static Boolean isPush=false;
    public static ArrayList<InfoStudent> arrayEvaluationInfo=new ArrayList<>();
    public static String selectedDate;
    public static ArrayList<Allclass> SelectedClasses=new ArrayList<>();
    public static ArrayList<Files> hwFiles=new ArrayList<>();
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
    public static ArrayList<Allclass> getSelectedClasses() {
        return SelectedClasses;
    }

    public static void setSelectedClasses(ArrayList<Allclass> selectedClasses) {
        SelectedClasses = selectedClasses;
    }

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

    public static String getExamCategoryName() {
        return examCategoryName;
    }

    public static void setExamCategoryName(String examCategoryName) {
        AppHelper.examCategoryName = examCategoryName;
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



    public static void setRoundImage(final Context context, final ImageView img, String ImgUrl){
        Glide.with(context).load(ImgUrl).asBitmap().centerCrop().placeholder(R.drawable.no_agent).dontAnimate().into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
    public static File getCompressed(Context context, String path, int i) throws IOException {

        if(context == null)
            throw new NullPointerException("Context must not be null.");
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        File cacheDir = context.getExternalCacheDir();
        if(cacheDir == null)
            //fall back
            cacheDir = context.getCacheDir();

        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);

        //Create ImageCompressor folder if it doesnt already exists.
        if(!root.exists())
            root.mkdirs();

        //decode and resize the original bitmap from @param path.
        Bitmap bitmap = decodeImageFromFiles(path, /* your desired width*/700, /*your desired height*/ 700);


        //create placeholder for the compressed image file
        File compressed = new File(root, SDF.format(new Date()) +i+ ".jpg" /*Your desired format*/);

        //convert the decoded bitmap to stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        /*compress bitmap into byteArrayOutputStream
            Bitmap.compress(Format, Quality, OutputStream)

            Where Quality ranges from 1 - 100.
         */
        try
        {
            // Determine Orientation
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            // Determine Rotation
            int rotation = 0;
            if      (orientation == 6)      rotation = 90;
            else if (orientation == 3)      rotation = 180;
            else if (orientation == 8)      rotation = 270;

            // Rotate Image if Necessary
            if (rotation != 0)
            {
                // Create Matrix
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                // Rotate Bitmap
                Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // Pretend none of this ever happened!
                bitmap.recycle();
                bitmap = rotated;
                rotated = null;
            }
        }
        catch (Exception e)
        {
            // TODO: Log Error Messages Here
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

        /*
        Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
        java.io.FileOutputStream can help us do just That!

         */
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();

        fileOutputStream.close();



        //File written, return to the caller. Done!
        return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();




        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }


    public static ArrayList<Files> getHwFiles() {
        return hwFiles;
    }

    public static void setHwFiles(ArrayList<Files> hwFiles) {
        AppHelper.hwFiles = hwFiles;
    }

    public static void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }




}
