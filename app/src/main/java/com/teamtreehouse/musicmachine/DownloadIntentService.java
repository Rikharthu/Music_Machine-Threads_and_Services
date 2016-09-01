package com.teamtreehouse.musicmachine;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.util.Log;

public class DownloadIntentService extends IntentService {
    public static final String TAG = DownloadIntentService.class.getSimpleName();

    // Must implement zero-argument constructor
    public DownloadIntentService() {
        // set the name of the thread where this service will do it's work
        super("DownloadIntentService");

        // By default it will be START_STICKY
        // set it to Service.START_REDELIVER_INTENT
        setIntentRedelivery(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        // This code runs on a separate thread
        String song = intent.getStringExtra(MainActivity.KEY_SONG);
        Log.d(TAG,"onHandleIntent() for song "+song);
        // no need to use handler, since IntentService launches on a separate thread
        downloadSong(song);

        /* Notification */
        // create an intent
        Intent intent2 = new Intent(this, MainActivity.class);
        // Use a TaskStackBuilder to make the back button play nicely
        // and create the pending intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent2);
        // by giving PendingIntent to another app/etc you are literally passing the rights
        // to execute this intent as if the other app was yourself
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                );
        // Build the notification
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(getString(R.string.app_name)) // set title
                .setAutoCancel(true) // make the notification disappear when clicked
                .setPriority(Notification.PRIORITY_MAX) // give it maximum priority
                .setDefaults(Notification.DEFAULT_VIBRATE) // vibration
                .setContentIntent(pendingIntent) // pass pending intent, so notification can start the app's MainActivity
                .setContentText("Download finished for "+song) // set text
                .build();
        // Display the notification using the Android notification service
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(945823, notification);

    }

    private void downloadSong(String title) {
        long endTime = System.currentTimeMillis() + 2*1000;
        while (System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Song \""+title+"\" downloaded!");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }
}
