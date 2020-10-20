package com.skydrop.jenvy.singleton;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.skydrop.jenvy.Activities.Playeractivity;
import com.skydrop.jenvy.Notifications.NotificationReceiver;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.models.SongModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.skydrop.jenvy.Applications.App.ALBUM_ARTIST_FLAG;
import static com.skydrop.jenvy.Applications.App.CHANNEL_1_ID;
import static com.skydrop.jenvy.Applications.App.MAINACTIVITY_FLAG;
import static com.skydrop.jenvy.Applications.App.PlAYERACTIVITY_FLAG;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.ACTION_NAME;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.NEXT_ACTION;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.PLAY_ACTION;

public class song_singleton extends AppCompatActivity {

    //region Final Variables
    private final SongsList_singleton singleton = SongsList_singleton.getInstance();
    //endregion

    //region Private Variables
    private MediaPlayer mediaPlayer;
    private int flag;
    private SongModel model;
    private Context app_context;
    private String item;
    private String itemname;
    private boolean isshuffled;
    private List<Integer> shufflingarray = new ArrayList<>();
    private int shufflingindex;
    //endregion

    //region Public Variables
    public int position;
    public TextView Player_SongName;
    public ImageButton Player_Play;
    public TextView Player_album;
    public ImageView Player_albumart;
    public ImageButton Player_shuffle;
    public ImageButton Player_repeat;
    public TextView played_duration;
    public TextView total_duration;
    public SeekBar seekBar;
    public Handler handler;

    public TextView Main_SongName;
    public ImageView Main_albumart;
    public ImageButton Main_Play;

    public ImageButton Album_Artist_Play;
    public TextView Album_Artist_title;
    public ImageView Album_Artist_albumart;
    //endregion

    //region Static Variables
    @SuppressLint("StaticFieldLeak")
    private static final song_singleton instance = new song_singleton();
    private boolean isrepeating;
    //endregion

    public void play(final Context context, int position, String item, String itemname) {
        this.position = position;
        this.app_context = context;
        this.item = item;
        this.itemname= itemname;
        model = singleton.getitemmodel(item,itemname,position);

        //region New Song Creating
        if (mediaPlayer != null) {
            //mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(app_context, Uri.parse(model.getPath()));

        try{
            playsong();
        }
        catch (Exception e){
            next(context);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next(context);
            }
        });
        //endregion

    }


    public void shownotification(){

        Intent plyayintent = new Intent(app_context,NotificationReceiver.class);
        plyayintent.putExtra(ACTION_NAME,PLAY_ACTION);

        Intent openactivity = new Intent(app_context, Playeractivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(app_context, 0, openactivity, 0);

        /*Intent previntent = new Intent(app_context,NotificationReceiver.class);
        previntent.putExtra(ACTION_NAME,PREV_ACTION);
        PendingIntent prevaction = PendingIntent.getBroadcast(app_context,0,previntent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent playaction = PendingIntent.getBroadcast(app_context,0,plyayintent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent nextaction = PendingIntent.getBroadcast(app_context,0,nextintent,PendingIntent.FLAG_UPDATE_CURRENT);*/



        Intent nextintent = new Intent(app_context, NotificationReceiver.class);
        nextintent.putExtra(ACTION_NAME,NEXT_ACTION);



        int resourse = mediaPlayer.isPlaying()?R.drawable.ic_pause: R.drawable.ic_play;

        Notification notification = new NotificationCompat.Builder(app_context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(model.getTitle())
                .setContentText(model.getAlbum())
                .setLargeIcon(model.getAlbumart())
//                .addAction(R.drawable.ic_previous, "Previous", PendingIntent.getBroadcast(app_context,0,previntent,PendingIntent.FLAG_UPDATE_CURRENT))
//                .addAction(R.drawable.ic_next, "Next", PendingIntent.getBroadcast(app_context,0,nextintent,PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(resourse, "Pause", PendingIntent.getBroadcast(app_context,0,plyayintent,PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOngoing(mediaPlayer.isPlaying())
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(app_context);
        notificationManager.notify(1, notification);
    }

    public void seek_Bar() {
        int duration = mediaPlayer.getDuration();
        System.out.print("duration"+duration);
        total_duration.setText(formattedTime(duration));
        seekBar.setMax(duration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int position;//em chesthunav??
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    position=i;
                }
            }


            //region Unused Overrides
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            //endregion
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(position);
                Player_Play.setImageResource(R.drawable.ic_pause);
                playsong();
            }
        });

        song_singleton.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPos = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPos);
                    played_duration.setText(formattedTime(currentPos));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void next(Context context) {
        if(!isrepeating) {
            if (isshuffled) {
                if (shufflingindex >= shufflingarray.size() - 1) {
                    generaterandonarray(shufflingarray.size());
                    shufflingindex = -1;
                }
                ++shufflingindex;
                position = getshufflingval(shufflingindex);
            /*
            positioon = getrndomint(size);
             */
            } else {
                if (position >= singleton.getsize(item, itemname) - 1) {
                    position = -1;
                }
                ++position;
            }
        }

        play(context,position, item,itemname);
    }

    public void prev(Context context) {
        if (position <= 0) {
            position = singleton.getsize(item,itemname);
        }
        play(context,--position, item,itemname);
    }

    public void playsong(){
        mediaPlayer.start();
        shownotification();
        showdata();
    }

    public void pausesong(){
        mediaPlayer.pause();
        shownotification();
        showdata();
    }

    public void shufflesong(Context context,String item, String itemname){
        shufflingindex = 0;
        this.itemname = itemname;
        this.item = item;
        setIsshuffled(true);
        play(context,getshufflingval(shufflingindex),item,itemname);
    }


    private void generaterandonarray(int size) {
        shufflingarray.clear();
        for(int i=0;i<size;i++){
            shufflingarray.add(i);
        }
        Collections.shuffle(shufflingarray);
    }

    private int getshufflingval(int shufflingindex) {
        return shufflingarray.get(shufflingindex);
    }

    public void showdata(){
        if(model!=null) {
            if (flag == PlAYERACTIVITY_FLAG) {
                Player_SongName.setText(model.getTitle());
                Player_Play.setImageResource(!mediaPlayer.isPlaying()?R.drawable.ic_play:R.drawable.ic_pause);
                Player_albumart.setImageBitmap(model.getAlbumart());
                Player_album.setText(model.getAlbum());
                seek_Bar();
            } else if (flag == MAINACTIVITY_FLAG) {
                Main_SongName.setText(model.getTitle());
                Main_albumart.setImageBitmap(model.getAlbumart());
                Main_Play.setImageResource(!mediaPlayer.isPlaying()?R.drawable.ic_play:R.drawable.ic_pause);
            } else if(flag == ALBUM_ARTIST_FLAG) {
                Album_Artist_albumart.setImageBitmap(model.getAlbumart());
                Album_Artist_title.setText(model.getTitle());
                Album_Artist_Play.setImageResource(!mediaPlayer.isPlaying()?R.drawable.ic_play:R.drawable.ic_pause);
            }
        }
    }

    private String formattedTime(int currentPos) {
        String total1;
        String total2;
        currentPos /= 1000;
        String seconds = String.valueOf(currentPos % 60);
        String minutes = String.valueOf(currentPos / 60);
        total1 = minutes + ":" + "0" + seconds;
        total2 = minutes + ":" + seconds;
        if (seconds.length() == 1)
            return total1;
        else
            return total2;
    }

    //region Getters and Setters
    public static song_singleton getInstance() {
        return instance;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public boolean getIsPlaying(){return mediaPlayer.isPlaying();}



    public boolean Isshuffled() {
        return isshuffled;
    }

    public void setIsshuffled(boolean isshuffled) {
        this.isshuffled = isshuffled;
        if(isshuffled) {
            generaterandonarray(singleton.getsize(item, itemname));
        }
    }

    public boolean Isrepeating() {
        return isrepeating;
    }

    public void setIsrepeating(boolean isrepeating) {
        this.isrepeating = isrepeating;
    }

    //endregion
}

