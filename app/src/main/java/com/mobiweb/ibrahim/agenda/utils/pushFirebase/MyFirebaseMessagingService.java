/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.mobiweb.ibrahim.agenda.utils.pushFirebase;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.Activity_Splash;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MyFirebaseMessagingService extends FirebaseMessagingService  {
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;
    private String pushId="";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.wtf("notification","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
       // Log.wtf("notification", "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
       // Log.wtf("notification", "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.wtf("notification", "Notification Message DATA: " + remoteMessage.getData().toString());


        String message;
        try {
            message = remoteMessage.getNotification().getBody();
        }catch (Exception e){
            message = remoteMessage.getData().get("");
        }
        Log.wtf("message", "message:" + message+"sdfdf");

  /*      JSONObject jsonData = new JSONObject(remoteMessage);
        Log.wtf("jsondata",jsonData.toString());*/




      //  String click_action = remoteMessage.getNotification().getClickAction();
       // Log.wtf("click_action","bob"+click_action);


        if (remoteMessage.getData().size()> 0){

            Log.d("Message Data", "Message Data---"+remoteMessage.getData() );

            String title=remoteMessage.getData().get("PageId");
            String message1=remoteMessage.getData().get("MatchID");

            Intent intent = new Intent("com.mobiweb.ibrahim.agenda_FCM-MESSAGE");
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
        }


 /*       switch (remoteMessage.getData().get("PageId")){
            case AppConstants.PAGE_MATCH_INSIDE:
                pushId=remoteMessage.getData().get("MatchID");
                break;
            case AppConstants.PAGE_FIND_OPONENT:
                pushId=remoteMessage.getData().get("ID");
                break;
            case AppConstants.PAGE_PREDICTION_WINNER:
                pushId=remoteMessage.getData().get("ID");
                break;
            default:
                break;
        }*/

        pushId=remoteMessage.getData().get("contentId");






        sendNotification(
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"),

                remoteMessage.getData().get("PageId"),
                pushId,
                remoteMessage.getData().get("date"),
                  remoteMessage.getData().get("withImage"),
                  remoteMessage.getData().get("arrayImages")



        );



    }

    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String id=getResources().getString(R.string.default_notification_channel_id);
        String name = "Announcement";
        String description = "Notification Announcement";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(getResources().getColor(R.color.colorPrimary));
        notificationManager.createNotificationChannel(mChannel);
    }




    //This method is only generating push notification if app is opened
    private void sendNotification(String messageTitle, String messageBody, String pageId, String pushId,String date,String withImage,String arrayImages) {
        Log.wtf("notification_send","ID "+pushId+"");
        Log.wtf("notification_send","contentTitle "+messageTitle+"");
        Log.wtf("notification_send","contentMessage "+messageBody+"");
        Log.wtf("notification_send","contentDate "+date+"");

        Log.wtf("notification_send","arrayImage "+arrayImages+"");
        Log.wtf("notification_send","withImage "+withImage+"");


        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(arrayImages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] strArr = new String[jsonArray.length()];
        ArrayList<String> arrayListImages = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                strArr[i] = jsonArray.getString(i);
                arrayListImages.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = null;
        if(strArr.length>0)
             bitmap= getBitmapfromUrl(RetrofitClient.BASE_URL+"announcement/"+strArr[0]);







        Intent intent = new Intent(this,Activity_Splash.class);
        intent.putExtra("PageId",pageId);
        intent.putExtra(AppConstants.ISPUSH,true);
        intent.putExtra("contentId",pushId);
        intent.putExtra("title",messageTitle);
        intent.putExtra("body",messageBody);
        intent.putExtra("date",date);
        intent.putExtra("withImage",withImage);
        intent.putStringArrayListExtra("arrayImage",arrayListImages);


        for(int i=0;i<arrayListImages.size();i++)
            Log.wtf("array_image","notification"+arrayListImages.get(i));


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

       PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);







        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);

            notificationBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id));
        } else
            notificationBuilder = new NotificationCompat.Builder(this);

       // String body = "Line 1<br>Line 2<br><i>Italic Line 3</i>";
        String body = messageBody.replaceAll("\\\\n","<br>");
        SpannableString formattedBody;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
             formattedBody = new  SpannableString(

             Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY)
    );
        }else {
             formattedBody = new  SpannableString(

                    Html.fromHtml(body)
            );

        }


        if(withImage.matches("1")) {
           notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                   .setSmallIcon(R.mipmap.ic_launcher)
                   .setLargeIcon(bitmap)
                   .setStyle(new NotificationCompat.BigPictureStyle()
                           .bigPicture(bitmap))/*Notification with Image*/
                   .setContentTitle(messageTitle)
                   //.setContentText(messageBody)
       /*            .setStyle(new NotificationCompat.BigTextStyle()
                           .bigText(messageBody.replaceAll("\\\\n","<br>"))
                   )*/
                   .setContentText(formattedBody)
                   .setStyle(new NotificationCompat.BigTextStyle().bigText(formattedBody).setBigContentTitle(messageTitle))


                   .setAutoCancel(true)
                   .setSound(defaultSoundUri)
                   .setChannelId(getResources().getString(R.string.default_notification_channel_id))
                   .setContentIntent(contentIntent);
       }else {
           notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                   .setSmallIcon(R.mipmap.ic_launcher)
                   .setContentTitle(messageTitle)
                  // .setContentText(messageBody)
                   .setContentText(formattedBody)
                   .setStyle(new NotificationCompat.BigTextStyle().bigText(formattedBody).setBigContentTitle(messageTitle))

                   .setAutoCancel(true)
                   .setSound(defaultSoundUri)
                   .setChannelId(getResources().getString(R.string.default_notification_channel_id))
                   .setContentIntent(contentIntent);
       }



           notificationManager.notify(count, notificationBuilder.build());


        count++;

    }



    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}