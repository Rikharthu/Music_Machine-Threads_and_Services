package com.teamtreehouse.musicmachine;

import android.os.Looper;
import android.util.Log;

public class DownloadThread extends Thread {

    private static final String TAG = DownloadThread.class.getSimpleName();
    public DownloadHandler mHandler;

    @Override
    public void run() {
        Log.d(TAG,"run()");
        // Initialize the current thread as a looper
        Looper.prepare();
        // By default a Handler is associated with a looper of the current thread
        mHandler=new DownloadHandler();
        // Start looping over the message queue
        Looper.loop();
    }


}
