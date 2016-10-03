package com.teamtreehouse.musicmachine;


import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class PlayerHandler extends Handler {
    public static final String TAG=PlayerHandler.class.getSimpleName();

    private PlayerService mPlayerService;

    public PlayerHandler(PlayerService playerService) {
        // we will use it to control music player
        mPlayerService=playerService;
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(TAG, "Received{\n"+MessageUtils.getMsgInfo(msg)+"}");
        switch(msg.arg1){
            case 0: // Play
                Log.d(TAG,"play()");
                mPlayerService.play();
                break;
            case 1: // Pause
                Log.d(TAG,"pause()");
                mPlayerService.pause();
                break;
            case 2: // isPlaying?
                // transform int to boolean, so it can be attached with message
                int isPlaying = mPlayerService.isPlaying()? 1:0;
                Log.d(TAG,"playing: "+isPlaying);
                Message message = Message.obtain();
                message.arg1=isPlaying;
                if(msg.arg2==1){
                    message.arg2=1;
                }
                // tell to reply back here (PlayerService's messenger instance of this class)
                message.replyTo = mPlayerService.mMessenger;

                // send this message back to ActivityHandler (provided in replyTo property)
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
