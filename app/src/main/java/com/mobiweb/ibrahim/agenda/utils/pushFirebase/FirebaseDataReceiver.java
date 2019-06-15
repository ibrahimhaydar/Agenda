package com.mobiweb.ibrahim.agenda.utils.pushFirebase;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.mobiweb.ibrahim.agenda.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by test on 4/17/2018.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadcastReceiver::", "BroadcastReceiver");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getExtras().getString("title"))
                .setContentText(intent.getExtras().getString("message"));
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}