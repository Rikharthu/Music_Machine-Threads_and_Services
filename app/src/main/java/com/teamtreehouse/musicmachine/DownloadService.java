package com.teamtreehouse.musicmachine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

public class DownloadService extends Service {
    private static final String TAG = DownloadService.class.getSimpleName();

    DownloadHandler mHandler;

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");

        // start new thread (since service code is running on main thread of the app that started it)
        DownloadThread thread = new DownloadThread();
        thread.setName("DownloadThread");
        thread.start();

        /*  */
        // wait until the handler on thread exists before getting reference to it
        while(thread.mHandler == null){}
        // looper and handler instantiated, ready for handling messages

        mHandler=thread.mHandler;
        // pass reference to this service to the handler
        mHandler.setService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand(), startId="+startId);
        // grab the song from an intent
        String song = intent.getStringExtra(MainActivity.KEY_SONG);

        Message message = Message.obtain();
        message.obj=song;
        // pass startId to the handler
        message.arg1=startId;

        mHandler.sendMessage(message);

        // Make it return this value instead of default super.onStartCommand()
        // Re-deliver all unfinished intents
        return Service.START_REDELIVER_INTENT;
    }

    // Must be implemented
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }
}
