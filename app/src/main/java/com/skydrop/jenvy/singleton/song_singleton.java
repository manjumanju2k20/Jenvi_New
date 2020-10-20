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
import static com.skydrop.jenvy.Applications.App.MAIN_ACTIVITY_FLAG;
import static com.skydrop.jenvy.Applications.App.PLAYER_ACTIVITY_FLAG;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.ACTION_NAME;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.NEXT_ACTION;
import static com.skydrop.jenvy.Notifications.NotificationReceiver.PLAY_ACTION;

public class song_singleton extends AppCompatActivity {

    //region Final Variables
    private final SongsList_singleton singleton = SongsList_singleton.getInstance();
    @SuppressLint("StaticFieldLeak")
    private static final song_singleton instance = new song_singleton();
    //endregion

    //region Private Variables
    private MediaPlayer mediaPlayer;
    private int flag;
    private int position;
    private SongModel model;
    private Context app_context;
    private String item;
    private String itemName;
    private boolean isShuffled;
    private boolean isRepeating;
    private int shufflingIndex;
    private List<Integer> shufflingArray = new ArrayList<>();
    //endregion

    //region Public Variables
    public TextView Player_SongName;
    public ImageButton Player_Play;
    public TextView Player_album;
    public ImageView Player_album_art;
    public TextView played_duration;
    public TextView total_duration;
    public SeekBar seekBar;
    public Handler handler;

    public TextView Main_SongName;
    public ImageView Main_album_art;
    public ImageButton Main_Play;

    public ImageButton Album_Artist_Play;
    public TextView Album_Artist_title;
    public ImageView Album_Artist_album_art;
    //endregion


    public void play(final Context context, int position, String item, String itemName) {
        this.position = position;
        this.app_context = context;
        this.item = item;
        this.itemName = itemName;
        model = singleton.getItemModel(item, itemName, position);

        //region New Song Creating
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(app_context, Uri.parse(model.getPath()));

        try {
            playSong();
        } catch (Exception e) {
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


    public void showNotification() {

        Intent playIntent = new Intent(app_context, NotificationReceiver.class);
        playIntent.putExtra(ACTION_NAME, PLAY_ACTION);

        Intent openActivity = new Intent(app_context, Playeractivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(app_context, 0, openActivity, 0);

        /*Intent prevIntent = new Intent(app_context,NotificationReceiver.class);
        prevIntent.putExtra(ACTION_NAME,PREV_ACTION);
        PendingIntent prevAction = PendingIntent.getBroadcast(app_context,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent playAction = PendingIntent.getBroadcast(app_context,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        PendingIntent nextAction = PendingIntent.getBroadcast(app_context,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);*/


        Intent nextIntent = new Intent(app_context, NotificationReceiver.class);
        nextIntent.putExtra(ACTION_NAME, NEXT_ACTION);


        int resource = mediaPlayer.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play;

        Notification notification = new NotificationCompat.Builder(app_context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle(model.getTitle())
                .setContentText(model.getAlbum())
                .setLargeIcon(model.getAlbumArt())
//                .addAction(R.drawable.ic_previous, "Previous", PendingIntent.getBroadcast(app_context,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT))
//                .addAction(R.drawable.ic_next, "Next", PendingIntent.getBroadcast(app_context,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(resource, "Pause", PendingIntent.getBroadcast(app_context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT))
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
        total_duration.setText(formattedTime(duration));
        seekBar.setMax(duration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int position;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b) {
                    position = i;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(position);
                Player_Play.setImageResource(R.drawable.ic_pause);
                playSong();
            }

            //region Unused Overrides
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            //endregion
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
        if (!isRepeating) {
            if (isShuffled) {
                if (shufflingIndex >= shufflingArray.size() - 1) {
                    generateRandomArray(shufflingArray.size());
                    shufflingIndex = -1;
                }
                ++shufflingIndex;
                position = getShufflingVal(shufflingIndex);
            } else {
                if (position >= singleton.getSize(item, itemName) - 1) {
                    position = -1;
                }
                ++position;
            }
        }
        play(context, position, item, itemName);
    }

    public void prev(Context context) {
        if (isShuffled) {
            if (shufflingIndex <= 0) {
                shufflingIndex = shufflingArray.size() - 1;
                generateRandomArray(shufflingArray.size());
            } else {
                --shufflingIndex;
                position = getShufflingVal(shufflingIndex);
            }
        } else {
            if (position <= 0) {
                position = singleton.getSize(item, itemName);
            }
            --position;
        }
        play(context, position, item, itemName);
    }

    public void playSong() {
        mediaPlayer.start();
        showNotification();
        showData();
    }

    public void pauseSong() {
        mediaPlayer.pause();
        showNotification();
        showData();
    }

    //region shuffling
    public void shuffleSong(Context context, String item, String itemName) {
        shufflingIndex = 0;
        this.itemName = itemName;
        this.item = item;
        setShuffled(true);
        setRepeating(false);
        play(context, getShufflingVal(shufflingIndex), item, itemName);
    }


    private void generateRandomArray(int size) {
        shufflingArray.clear();
        for (int i = 0; i < size; i++) {
            shufflingArray.add(i);
        }
        Collections.shuffle(shufflingArray);
    }

    private int getShufflingVal(int shufflingIndex) {
        return shufflingArray.get(shufflingIndex);
    }
    //endregion

    public void showData() {
        if (model != null) {
            if (flag == PLAYER_ACTIVITY_FLAG) {
                Player_SongName.setText(model.getTitle());
                Player_Play.setImageResource(!mediaPlayer.isPlaying() ? R.drawable.ic_play : R.drawable.ic_pause);
                Player_album_art.setImageBitmap(model.getAlbumArt());
                Player_album.setText(model.getAlbum());
                seek_Bar();
            } else if (flag == MAIN_ACTIVITY_FLAG) {
                Main_SongName.setText(model.getTitle());
                Main_album_art.setImageBitmap(model.getAlbumArt());
                Main_Play.setImageResource(!mediaPlayer.isPlaying() ? R.drawable.ic_play : R.drawable.ic_pause);
            } else if (flag == ALBUM_ARTIST_FLAG) {
                Album_Artist_album_art.setImageBitmap(model.getAlbumArt());
                Album_Artist_title.setText(model.getTitle());
                Album_Artist_Play.setImageResource(!mediaPlayer.isPlaying() ? R.drawable.ic_play : R.drawable.ic_pause);
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

    public boolean getIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean IsShuffled() {
        return isShuffled;
    }

    public void setShuffled(boolean shuffled) {
        this.isShuffled = shuffled;
        if (shuffled) {
            generateRandomArray(singleton.getSize(item, itemName));
        }
    }

    public boolean IsRepeating() {
        return isRepeating;
    }

    public void setRepeating(boolean repeating) {
        this.isRepeating = repeating;
    }

    public boolean isMediaPlayer() {
        return mediaPlayer != null;
    }

    //endregion
}
