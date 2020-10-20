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
import com.skydrop.jenvy.Interfaces.SongItemClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.SongsList_singleton;
import com.skydrop.jenvy.singleton.song_singleton;

import static com.skydrop.jenvy.Applications.App.ALBUM_ARTIST_FLAG;
import static com.skydrop.jenvy.Applications.App.MAIN_ACTIVITY_FLAG;

public class Album_Artist extends AppCompatActivity {

    private final song_singleton song = song_singleton.getInstance();
    private final SongsList_singleton songsList = SongsList_singleton.getInstance();
    private final SongItemClickListener Listener = new SongItemClickListener() {
        @Override
        public void onClick(int pos, String item, String itemName) {
            song.play(getApplicationContext(), pos, item, itemName);
            song.setShuffled(false);
            song.setRepeating(false);
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
                    song.pauseSong();
                } else {
                    play.setImageResource(R.drawable.ic_pause);
                    song.playSong();
                }
            } else if (v == backBnt) {
                onBackPressed();
            } else if (v == shuffle) {
                song.shuffleSong(getApplicationContext(), item, itemName);
            } else if (v == play_album) {
                song.play(getApplicationContext(), 0, item, itemName);
                song.setShuffled(false);
                song.setRepeating(false);
            } else if (v == bottomBar) {
                if (song.isMediaPlayer()) {
                    Intent playerActivity = new Intent(getApplicationContext(), Playeractivity.class);
                    playerActivity.putExtra("extra", 2);
                    startActivity(playerActivity);
                }
            }
        }
    };

    private TextView album_artist_title;
    private ImageButton backBnt;
    private ImageButton shuffle;
    private ImageButton play_album;
    private RecyclerView recView;
    private ImageButton next;
    private ImageButton prev;
    private ImageButton play;
    private ConstraintLayout bottomBar;

    private String item;
    private String itemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__artist);
        Intent intent = getIntent();
        this.item = intent.getStringExtra("item");
        this.itemName = intent.getStringExtra("itemName");
        setMappings();
        song.setFlag(ALBUM_ARTIST_FLAG);
        songsListAdapter adapter = new songsListAdapter(getApplicationContext(), Listener, item, itemName);
        recView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recView.setAdapter(adapter);

        setData();
        song.showData();
    }

    private void setData() {
        album_artist_title.setText(itemName);
        ((ImageView) findViewById(R.id.art_album_artist)).setImageBitmap(songsList.getItemModel(item, itemName, 0).getAlbumArt());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        song.setFlag(MAIN_ACTIVITY_FLAG);
        song.showData();
    }

    private void setMappings() {
        recView = findViewById(R.id.album_artist_list);
        next = findViewById(R.id.next_album_artist);
        prev = findViewById(R.id.prev_album_artist);
        play = findViewById(R.id.play_album_artist);
        bottomBar = findViewById(R.id.bottombar_album_artist);
        backBnt = findViewById(R.id.album_artist_back);

        song.Album_Artist_Play = play;

        album_artist_title = findViewById(R.id.album_artist_title);
        shuffle = findViewById(R.id.album_artist_shuffle);
        play_album = findViewById(R.id.album_artist_play);
        song.Album_Artist_title = findViewById(R.id.title_album_artist);
        song.Album_Artist_album_art = findViewById(R.id.albumart_album_artist);

        next.setOnClickListener(listener);
        prev.setOnClickListener(listener);
        play.setOnClickListener(listener);
        bottomBar.setOnClickListener(listener);
        backBnt.setOnClickListener(listener);
        shuffle.setOnClickListener(listener);
        play_album.setOnClickListener(listener);

        album_artist_title.setSelected(true);
        song.Album_Artist_title.setSelected(true);
    }
}