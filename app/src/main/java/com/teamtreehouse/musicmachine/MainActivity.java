package com.teamtreehouse.musicmachine;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_SONG = "song";
    private Button mDownloadButton;


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
    }


}
