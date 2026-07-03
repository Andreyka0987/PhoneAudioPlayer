package com.example.music_player;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ForeGroundMusicService extends Service {
    public static MediaPlayer mediaPlayer;



    @SuppressLint("ForegroundServiceType")
    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        mediaPlayer.start();

        // Notification and validating foreground service example
        final String NOTIFY_CHANNEL_ID = "FOREGROUND SERVICE";
        NotificationChannel channel = new NotificationChannel(
                NOTIFY_CHANNEL_ID,
                NOTIFY_CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notify = new Notification.Builder(this,NOTIFY_CHANNEL_ID)
                .setContentText("Music is running")
                .setContentTitle("Music")
                .setSmallIcon(R.drawable.ic_launcher_foreground);


        startForeground(1313,notify.build());


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
        System.exit(0);
    }
}
