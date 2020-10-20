package com.skydrop.jenvy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.song_singleton;
import static com.skydrop.jenvy.Applications.App.ALBUM_ARTIST_FLAG;
import static com.skydrop.jenvy.Applications.App.MAINACTIVITY_FLAG;
import static com.skydrop.jenvy.Applications.App.PlAYERACTIVITY_FLAG;

public class Playeractivity extends AppCompatActivity {
    //region final var
    private final song_singleton song = song_singleton.getInstance();

    private final OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == play) {
                if (song.getIsPlaying()) {
                    play.setImageResource(R.drawable.ic_play);
                    song.pausesong();
                } else {
                    play.setImageResource(R.drawable.ic_pause);
                    song.playsong();
                }
            } else if (view == back) {
                onBackPressed();
            } else if (view == next) {
                song.next(getApplicationContext());
            } else if (view == prev) {
                song.prev(getApplicationContext());
            }
            else if(view == shuffle){
                song.setIsshuffled(!song.Isshuffled());
                seticons();
            }
            else if(view == repeat){
                song.setIsrepeating(!song.Isrepeating());
                seticons();
            }
        }
    };

    //endregion

    //region Private Variables
    private ImageButton back;
    private ImageButton play;
    private ImageButton prev;
    private ImageButton next;
    private ImageButton shuffle;
    private ImageButton repeat;
    private int intentval;
    //endregion

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);
        song.setFlag(PlAYERACTIVITY_FLAG);
        try{
            intentval =getIntent().getIntExtra("extra",0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        setMappings();

        song.handler = new Handler();
        song.seek_Bar();

        //region Listeners
        play.setOnClickListener(listener);
        back.setOnClickListener(listener);
        next.setOnClickListener(listener);
        prev.setOnClickListener(listener);
        shuffle.setOnClickListener(listener);
        repeat.setOnClickListener(listener);
        //endregion

        seticons();
        song.showdata();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(intentval == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if(intentval == 2){
            song.setFlag(ALBUM_ARTIST_FLAG);
            song.showdata();
        }
        else if(intentval == 1){
            song.setFlag(MAINACTIVITY_FLAG);
            song.showdata();
        }
    }

    private void seticons() {
        shuffle.setImageResource(song.Isshuffled()?R.drawable.ic_shuffle_on:R.drawable.ic_shuffle_off);
        repeat.setImageResource(song.Isrepeating()?R.drawable.ic_repeat_on:R.drawable.ic_repeat_off);
    }


    private void setMappings() {
        next = findViewById(R.id.next_player);
        prev = findViewById(R.id.prev_player);
        play = findViewById(R.id.play_player);
        back = findViewById(R.id.back_player);
        shuffle = findViewById(R.id.shuffle_player);
        repeat = findViewById(R.id.repeat_player);

        song.Player_album = findViewById(R.id.album_player);
        song.Player_SongName = findViewById(R.id.title_player);
        song.Player_albumart = findViewById(R.id.albumart_player);

        song.seekBar = findViewById(R.id.seekBar);
        song.played_duration = findViewById(R.id.playduration);
        song.total_duration = findViewById(R.id.totalduration);

        song.Player_Play = play;

        song.Player_SongName.setSelected(true);
    }
}

