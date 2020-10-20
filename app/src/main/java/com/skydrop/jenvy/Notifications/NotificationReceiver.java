package com.skydrop.jenvy.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.skydrop.jenvy.singleton.song_singleton;


public class NotificationReceiver extends BroadcastReceiver {


    public static final int NEXT_ACTION = 1;
    public static final int PREV_ACTION = 2;
    public static final int PLAY_ACTION = 3;
    public static final String ACTION_NAME = "action";


    @Override
    public void onReceive(Context context, Intent intent) {
        int message = intent.getIntExtra(ACTION_NAME,0);
        Log.d("message",Integer.toString(message));
        song_singleton song = song_singleton.getInstance();
        if(message ==PREV_ACTION){
            song.prev(context);
        }
        else if(message == NEXT_ACTION){
            song.next(context);
        }
        else if(message == PLAY_ACTION){
            if(song.getIsPlaying()){
                song.pausesong();
            }
            else{
                song.playsong();
            }
        }
    }
}
