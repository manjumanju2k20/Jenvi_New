package com.skydrop.jenvy.Applications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


public class App extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final int MAIN_ACTIVITY_FLAG = 1;
    public static final int PLAYER_ACTIVITY_FLAG = 0;
    public static final int NOTIFICATION_FLAG = 2;
    public static final int ALBUM_ARTIST_FLAG = 3;


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel1.setDescription("Music Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}