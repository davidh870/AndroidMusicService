package edu.uic.cs478.clipserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.uic.cs478.aidl.MusicPlayerService;



public class ClipServer extends Service {

    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer mPlayer;
    private int mStartID;
    private Notification notification ;

    private static String CHANNEL_ID = "Music Player Channel" ;

    @Override
    public void onCreate(){
        super.onCreate();

        // Create Notification Channel
        this.createNotificationChannel();

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setOngoing(true).setContentTitle("Music Playing")
                        .setTicker("Music is playing!")
                        .build();

        // Put this service in a foregrounds state, else it will be killed by OS
        startForeground(NOTIFICATION_ID, notification);
    }


    // Implement stub for this object
    private final MusicPlayerService.Stub mBinder = new MusicPlayerService.Stub(){
        @Override
        public void startSong(String songID){
            changeSong(songID);
        }

        @Override
        public void playSong(){
            mPlaySong();
        }

        @Override
        public void pauseSong(){
            mPauseSong();
        }

        @Override
        public void stopSong(){
            mStopSong();
        }

    };

    // Changes song
    void changeSong(String mySongID){
        // if the player has been created, stop the media player
        if(mPlayer != null){mPlayer.stop(); mPlayer.release(); mPlayer = null;}

        if(mySongID.equals("1")){
            mPlayer = MediaPlayer.create(this, R.raw.a1);
        }
        else if (mySongID.equals("2")){
            mPlayer = MediaPlayer.create(this, R.raw.a2);
        }
        else if (mySongID.equals("3")){
            mPlayer = MediaPlayer.create(this, R.raw.a3);
        }
        else if (mySongID.equals("4")){
            mPlayer = MediaPlayer.create(this, R.raw.a4);
        }
        else if (mySongID.equals("5")){
            mPlayer = MediaPlayer.create(this, R.raw.a5);
        }

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent i =  new Intent("musicFilter");
                i.putExtra("song", "complete");
                sendBroadcast(i);
            }
        });

        mPlayer.start();
    }

    // Resumes / Play Song
    void mPlaySong(){
        if(mPlayer != null){
            if(!mPlayer.isPlaying()){
                mPlayer.start();
            }
        }
    }

    // Pause song
    void mPauseSong(){
        if(mPlayer != null){
            if(mPlayer.isPlaying()){
                mPlayer.pause();
            }
        }
    }

    // Stop Song
    void mStopSong(){
        // if the player has been created, stop the media player
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    // Creates notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music player notification";
            String description = "The channel for music player notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {

        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }


    // Return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}