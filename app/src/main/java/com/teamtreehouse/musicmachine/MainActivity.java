package com.teamtreehouse.musicmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
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
    // TODO DOC-Binding to activity
    private PlayerService mPlayerService;

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // TODO IBinder binder is returned by PlayerService IBinder onBind(...)
            // successfully connected to a service
            mBound=true;

            // TODO DOC-Binding to activity
            // create LocalBinder from passed binder object (return by service's IBinder onBind() method
            PlayerService.LocalBinder localBinder = (PlayerService.LocalBinder) binder;

            mPlayerService = localBinder.getService();

            /* User could enter the activity while music is already playing
            button would state "Play", since it is default */
            if(mPlayerService.isPlaying()){
                mPlayButton.setText("Pause");
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

                }
            }
        });

        mPlayButton = (Button) findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBound){
                    // use our client methods
                    if(mPlayerService.isPlaying()){
                        // Since song is not playing anymore, we can stop our service
                        mPlayerService.pause();
                        mPlayButton.setText("Play");
                    }else{
                        // Start service with an intent (will trigger onStartCommand(), check it)
                        Intent intent = new Intent(MainActivity.this, PlayerService.class);
                        startService(intent);

                        mPlayerService.play();
                        mPlayButton.setText("Pause");
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

    @Override
    protected void onDestroy() {
//        Log.d(TAG,"onDestroy()");
        super.onDestroy();
//        // unbind from the service if still bound
//        if(mBound){
//            unbindService(mServiceConnection);
//            mBound=false;
//        }
    }
}
