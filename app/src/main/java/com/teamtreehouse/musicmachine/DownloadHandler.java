package com.teamtreehouse.musicmachine;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadHandler extends Handler {
    private static final String TAG = DownloadHandler.class.getSimpleName();

    private DownloadService mService;

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, "Handling message, where msg.obj="+msg.obj.toString());
        // obtain passed object
        String song = msg.obj.toString();
        downloadSong(song);

        // Stop the service if it exists after finishing downlload
        if(mService!=null){
            // stopSelf(int) stops the service when passed startId equals last startId it was launched with
            mService.stopSelf(msg.arg1);
            Log.d(TAG,"Stopping DownloadService where startId="+msg.arg1);
        }
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

    public void setService(DownloadService service) {
        mService = service;
    }
}
