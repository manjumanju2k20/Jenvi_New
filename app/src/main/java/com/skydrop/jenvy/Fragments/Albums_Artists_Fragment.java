package com.skydrop.jenvy.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skydrop.jenvy.Activities.Album_Artist;
import com.skydrop.jenvy.Adapters.AlbumsArtistsAdapter;
import com.skydrop.jenvy.Interfaces.AlbumArtistClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.SongsList_singleton;
import com.skydrop.jenvy.singleton.song_singleton;


public class Albums_Artists_Fragment extends Fragment {

    private final SongsList_singleton songsList = SongsList_singleton.getInstance();
    private final song_singleton song = song_singleton.getInstance();

    private String item;

    private final AlbumArtistClickListener Listener = new AlbumArtistClickListener() {
        @Override
        public void onClick(int type, String item, int pos) {
            String itemName = songsList.getItemName(item,pos);
            if(type == 0){
                Intent albumArtist = new Intent(getContext(), Album_Artist.class);
                albumArtist.putExtra("item",item);
                albumArtist.putExtra("itemName",itemName);
                startActivity(albumArtist);
            }
            else{
                song.play(getContext(),0,item,itemName);
                song.setShuffled(false);
                song.setRepeating(false);
            }
        }
    };

    public Albums_Artists_Fragment(String item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_artists, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recview_album);
        AlbumsArtistsAdapter albumsArtistsAdapter = new AlbumsArtistsAdapter(getContext(),item, Listener);
        recyclerView.setAdapter(albumsArtistsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }
}
