package com.skydrop.jenvy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skydrop.jenvy.Adapters.songsListAdapter;
import com.skydrop.jenvy.Interfaces.SongitemClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.SongsList_singleton;
import com.skydrop.jenvy.singleton.song_singleton;

import static com.skydrop.jenvy.Applications.App.ALBUM_ARTIST_FLAG;
import static com.skydrop.jenvy.Applications.App.MAINACTIVITY_FLAG;

public class Album_Artist extends AppCompatActivity {

    private final song_singleton song = song_singleton.getInstance();
    private final SongsList_singleton songslist = SongsList_singleton.getInstance();
    private final SongitemClickListener Listener = new SongitemClickListener() {
        @Override
        public void onClick(int pos, String item, String itemname) {
            song.play(getApplicationContext(), pos, item, itemname);
            song.setIsshuffled(false);
        }
    };
    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == next) {
                song.next(getApplicationContext());
            } else if (v == prev) {
                song.prev(getApplicationContext());
            } else if (v == play) {
                if (song.getIsPlaying()) {
                    play.setImageResource(R.drawable.ic_play);
                    song.pausesong();
                } else {
                    play.setImageResource(R.drawable.ic_pause);
                    song.playsong();
                }
            } else if (v == backbnt) {
                onBackPressed();
            } else if (v == shuffle) {
                song.shufflesong(getApplicationContext(),item,itemname);
            } else if (v == play_album) {
                song.play(getApplicationContext(), 0, item, itemname);
                song.setIsshuffled(false);
            } else if (v == bottombar) {
                if(song.ismediaplayer()) {
                    Intent playeractivity = new Intent(getApplicationContext(), Playeractivity.class);
                    playeractivity.putExtra("extra", 2);
                    startActivity(playeractivity);
                }
            }
        }
    };

    private TextView album_artist_title;
    private ImageButton backbnt;
    private ImageButton shuffle;
    private ImageButton play_album;
    private RecyclerView recview;
    private ImageButton next;
    private ImageButton prev;
    private ImageButton play;
    private ConstraintLayout bottombar;

    private String item;
    private String itemname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__artist);
        Intent intent = getIntent();
        this.item = intent.getStringExtra("item");
        this.itemname = intent.getStringExtra("itemname");
        setmappings();
        song.setFlag(ALBUM_ARTIST_FLAG);
        songsListAdapter adapter = new songsListAdapter(getApplicationContext(), Listener, item, itemname);
        recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recview.setAdapter(adapter);

        setdata();
        song.showdata();
    }

    private void setdata() {
        album_artist_title.setText(itemname);
        ((ImageView) findViewById(R.id.art_album_artist)).setImageBitmap(songslist.getitemmodel(item, itemname, 0).getAlbumart());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        song.setFlag(MAINACTIVITY_FLAG);
        song.showdata();
    }

    private void setmappings() {
        recview = findViewById(R.id.album_artist_list);
        next = findViewById(R.id.next_album_artist);
        prev = findViewById(R.id.prev_album_artist);
        play = findViewById(R.id.play_album_artist);
        bottombar = findViewById(R.id.bottombar_album_artist);
        backbnt = findViewById(R.id.album_artist_back);

        song.Album_Artist_Play = play;

        album_artist_title = findViewById(R.id.album_artist_title);
        shuffle = findViewById(R.id.album_artist_shuffle);
        play_album = findViewById(R.id.album_artist_play);
        song.Album_Artist_title = findViewById(R.id.title_album_artist);
        song.Album_Artist_albumart = findViewById(R.id.albumart_album_artist);

        next.setOnClickListener(listener);
        prev.setOnClickListener(listener);
        play.setOnClickListener(listener);
        bottombar.setOnClickListener(listener);
        backbnt.setOnClickListener(listener);
        shuffle.setOnClickListener(listener);
        play_album.setOnClickListener(listener);

        album_artist_title.setSelected(true);
        song.Album_Artist_title.setSelected(true);
    }
}