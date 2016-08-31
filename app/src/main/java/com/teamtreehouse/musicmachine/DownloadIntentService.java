package com.teamtreehouse.musicmachine;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
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

    @Override
    protected void onHandleIntent(Intent intent) {
        String song = intent.getStringExtra(MainActivity.KEY_SONG);
        Log.d(TAG,"onHandleIntent() for song "+song);
        // no need to use handler, since IntentService launches on a separate thread
        downloadSong(song);

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
