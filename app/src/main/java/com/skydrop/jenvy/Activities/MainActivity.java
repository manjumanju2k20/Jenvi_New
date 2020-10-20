package com.skydrop.jenvy.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.skydrop.jenvy.Fragments.Albums_Artists_Fragment;
import com.skydrop.jenvy.Fragments.SongsFragment;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.models.SongModel;
import com.skydrop.jenvy.singleton.SongsList_singleton;
import com.skydrop.jenvy.singleton.song_singleton;

import java.util.ArrayList;

import static com.skydrop.jenvy.Applications.App.MAINACTIVITY_FLAG;
import static com.skydrop.jenvy.singleton.SongsList_singleton.ALBUM;
import static com.skydrop.jenvy.singleton.SongsList_singleton.ARTIST;
import static com.skydrop.jenvy.singleton.SongsList_singleton.SONGS;

public class MainActivity extends AppCompatActivity {
    //region Final Variables
    private final SongsList_singleton songdata = SongsList_singleton.getInstance();
    private final song_singleton song = song_singleton.getInstance();
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
            } else if (v == bottombar) {
                if(song.ismediaplayer()) {
                    Intent playeractivity = new Intent(getApplicationContext(), Playeractivity.class);
                    playeractivity.putExtra("extra", 1);
                    startActivity(playeractivity);
                }
            }
        }
    };
    //endregion

    private ImageButton next;
    private ImageButton prev;
    private ImageButton play;
    private ConstraintLayout bottombar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song.setFlag(MAINACTIVITY_FLAG);

        setmappings();

        if (songdata.getsize(SONGS) == 0) {
            runtimepersimissions();
        }
        else {
            initViewPager();
        }


    }
    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Albums_Artists_Fragment(ALBUM),ALBUM);
        viewPagerAdapter.addFragments(new Albums_Artists_Fragment(ARTIST),ARTIST);
        viewPagerAdapter.addFragments(new SongsFragment(),SONGS);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    public static class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragments(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    private void setmappings() {

        play = findViewById(R.id.play_main);
        next = findViewById(R.id.next_main);
        prev = findViewById(R.id.prev_main);
        bottombar = findViewById(R.id.bottombar_main);

        song.Main_Play = play;
        song.Main_SongName =  findViewById(R.id.title_main);
        song.Main_albumart =  findViewById(R.id.albumart_main);
        song.Main_SongName.setSelected(true);
        next.setOnClickListener(listener);
        prev.setOnClickListener(listener);
        play.setOnClickListener(listener);
        bottombar.setOnClickListener(listener);

        song.showdata();
    }

    private void runtimepersimissions() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                getsongs(MainActivity.this);
                initViewPager();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getsongs(Context context) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projections = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
        };

        @SuppressLint("Recycle")
        Cursor cursor = context.getContentResolver().query(uri, projections, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String id = cursor.getString(5);

                SongModel tempmodel = new SongModel();

                tempmodel.setTitle(title);
                tempmodel.setAlbum(album);
                tempmodel.setArtist(artist);
                tempmodel.setDuration(duration);
                tempmodel.setPath(path);
                tempmodel.setId(id);

                System.out.println("main model datatitle:"+tempmodel.getTitle()+"\n album:"+tempmodel.getAlbum()+"\n duration:"+tempmodel.getDuration());
                // TODO: Remove after album art
                tempmodel.setAlbumart(BitmapFactory.decodeResource(getResources(), R.drawable.defaultalbumart));
                songdata.setSongslist(tempmodel);
            }
        }
        songdata.setlist();
    }
}
