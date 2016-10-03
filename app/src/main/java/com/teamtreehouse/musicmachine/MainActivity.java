package com.teamtreehouse.musicmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_SONG = "song";
    private Button mDownloadButton;
    private Button mPlayButton;
    private boolean mBound=false;
    // We will use Messenger approach
//    private PlayerService mPlayerService;
    private Messenger mServiceMessenger;
    private Messenger mActivityMessenger=new Messenger(new ActivityHandler(this));



    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // IBinder binder is returned by PlayerService IBinder onBind(...)

            // successfully connected to a service
            mBound=true;

            // Get reference to PlayerService
            /*
            // 1. Cast passed binder to our LocalBinder
            PlayerService.LocalBinder localBinder = (PlayerService.LocalBinder) binder;

            // retreive reference to the service
            mPlayerService = localBinder.getService();

            if(mPlayerService.isPlaying()){
                mPlayButton.setText("Pause");
            }
            */
            // Messenger approach:
            mServiceMessenger=new Messenger(binder);
            Message message = Message.obtain();
            message.arg1=2; // is playing?
            message.arg2=1; //  we just want to check button state, without playing/pausing
            // provide our own messenger as a replyTo parameter to hear back
            message.replyTo=mActivityMessenger;
            // use mServiceMessenger to send message to a service
            try {
                Log.d(TAG,"sending to mServiceMessenger\n"+MessageUtils.getMsgInfo(message));
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // disconnected from the service
            mBound=false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Handler version */
        final DownloadThread thread = new DownloadThread();
        thread.setName("DownloadThread");
        thread.start();

        mDownloadButton = (Button) findViewById(R.id.downloadButton);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Downloading", Toast.LENGTH_SHORT).show();

                /* Thread version */

                /* Handler version */
                // Send Messages or Runnables to handler for processing
//                for(String song:Playlist.songs){
//                    Log.d(TAG, "Sending message, where msg.obj="+song);
//                    Message message = Message.obtain();
//                    // attach song name to a message
//                    message.obj=song;
//                    // send message to a handler
//                    thread.mHandler.sendMessage(message);
//                }


                /* Service version */

                for(String song:Playlist.songs){
                    Log.d(TAG, "Sending message, where msg.obj="+song);
                    // Extends Service
//                    Intent intent = new Intent(MainActivity.this,DownloadService.class);
                    // Extends IntentService
                    Intent intent = new Intent(MainActivity.this,DownloadIntentService.class);
                    intent.putExtra(KEY_SONG, song);
                    // Request to start a service
                    startService(intent);

                    Message message = Message.obtain();
                    message.arg1=2; // is playing?
                    // provide our own messenger as a replyTo parameter to hear back
                    message.replyTo=mActivityMessenger;
                    // use mServiceMessenger to send message to a service
                    try {
                        mServiceMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        mPlayButton = (Button) findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    // Start service with an intent (will trigger onStartCommand(), check it)
                    Intent intent = new Intent(MainActivity.this, PlayerService.class);
                    startService(intent);

                    Message message = Message.obtain();
                    message.arg1=2; // is playing?
                    // since we want to actually play or stop music, do not specify arg2
//                    message.arg2=1;
                    // provide our own messenger as a replyTo parameter to hear back
                    message.replyTo=mActivityMessenger;
                    // use mServiceMessenger to send message to a service
                    try {
                        Log.d(TAG,"sending to mServiceMessenger\n"+MessageUtils.getMsgInfo(message));
                        mServiceMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        // Context.BIND_AUTO_CREATE - automatically create service when we bind to it
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop()");
        super.onStop();
        // unbind from the service if still bound
        if(mBound){
            Log.d(TAG,"unbinding");
            unbindService(mServiceConnection);
            mBound=false;
        }
    }

    public void changePlayButtonText(String text){
        mPlayButton.setText(text);
    }

}
