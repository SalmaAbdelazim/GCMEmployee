package com.example.salma.gcmemployee;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by salma on 13/05/2017.
 */

public class GCMReceiverService extends GcmListenerService {


    public static HashMap<String,String> jobs=new HashMap<String,String>();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String acceptedToken=data.getString("acceptedToken");
        String notificationTime= data.getString("time");



        if(message.contains("already accepted"))
        {
            jobs.put(notificationTime,"taken");
        }else{
            jobs.put(notificationTime,"available");
        }

        if(GCMRegistration.token==null)
        {
            new GCMRegistration().registerGCM();
        }
        if(acceptedToken.equals(GCMRegistration.token))
        {
            sendNotification("CONGRATULATION YOU GET THE JOB OF TIME"+notificationTime,notificationTime);
        }else {
            sendNotification(message,notificationTime);
        }
    }
    private void sendNotification(String message,String notificationTime) {
        Intent intent;
        if(message.contains("new")){
            ///////new job offer
             intent = new Intent(getApplicationContext(), JobActivity.class);
        }else {
            ////got or missed
            intent = new Intent(getApplicationContext(), DetailsActivity.class);
        }


        intent.putExtra("time",notificationTime);
        intent.putExtra("msg",message);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("My GCM message :X:X")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss",
                Locale.ENGLISH);

        Date secondDate= null;
        try {
            secondDate = sdf.parse(notificationTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ////get notification identifier
        int i = (int)secondDate.getTime();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(i, noBuilder.build());
    }
}
