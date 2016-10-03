package com.teamtreehouse.musicmachine;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;


public class ActivityHandler extends Handler {
    public static final String TAG = ActivityHandler.class.getSimpleName();

    MainActivity mMainActivity;

    public ActivityHandler(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, "Received{\n"+MessageUtils.getMsgInfo(msg)+"}");


        if(msg.arg1==0){
            // Music is NOT playing
            if(msg.arg2==1){
                // do not play the music
                // just update button label
                mMainActivity.changePlayButtonText("Play");
            }else {
                // Play the music
                Message message = Message.obtain();
                message.arg1 = 0; // 0 - play
                // reply to PlayerHandler
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // Change the play Button to say "Pause"
                mMainActivity.changePlayButtonText("Pause");
            }
        }else if(msg.arg1==1){
            // Music is playing
            if(msg.arg2==1){
                mMainActivity.changePlayButtonText("Pause");
            }else {
                // Pause the music
                Message message = Message.obtain();
                message.arg1 = 1; // 1 - pause
                // reply to PlayerHandler
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // Change the play Button to say "Play"
                mMainActivity.changePlayButtonText("Play");
            }
        }

    }
}
