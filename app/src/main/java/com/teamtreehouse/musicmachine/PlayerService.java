package com.teamtreehouse.musicmachine;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayerService extends Service{
    private static final String TAG = PlayerService.class.getSimpleName();

    private IBinder mBinder = new LocalBinder();
    /**
     * Point of this class is to allow the activity to access this service
     */
    public class LocalBinder extends Binder {
        public PlayerService getService(){
            // Return this instance of PlayerService so clients can call public methods
            return PlayerService.this;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // we want our service to be in started state only when our song is playing
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG,"OnCompletionListener.onCompletion()");
                // In other words - persist started state while song is playing
                // leaving Activity will call unBind(), but wont call onDestroy()
                // if song aint playing - stop to exit from started state, thus letting service to destroy
                // when leaving the activity.
                // stop service immidiatelly when song completed/paused
                stopSelf();
            }
        });
        // use START_NOT_STICKY to prevent it from being destroyed while it is playing
        return Service.START_NOT_STICKY;
    }

    // Will use for handling song
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        mPlayer=MediaPlayer.create(this,R.raw.pharaoh_mm);
    }

    // Gets called every time app binds to this service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
        // return our LocalBinder. will be passed to onServiceConnected(... IBinder)
        return mBinder;
    }

    // Client Methods
    public void play(){
        Log.d(TAG,"play()");
        mPlayer.start();
    }

    public void pause(){
        Log.d(TAG,"pause()");
        mPlayer.pause();
    }

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void stop(){
        mPlayer.stop();
    }


    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
    }



    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind()");
        return super.onUnbind(intent);
    }
}
