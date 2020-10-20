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
import com.skydrop.jenvy.Interfaces.AlbumArtistClickListner;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.SongsList_singleton;
import com.skydrop.jenvy.singleton.song_singleton;


public class Albums_Artists_Fragment extends Fragment {

    private final SongsList_singleton songslist = SongsList_singleton.getInstance();
    private final song_singleton song = song_singleton.getInstance();

    private String item;

    private final AlbumArtistClickListner Listner = new AlbumArtistClickListner() {
        @Override
        public void onClick(int type, String item, int pos) {
            String itemname = songslist.getitemname(item,pos);
            if(type == 0){
                Intent albumartist = new Intent(getContext(), Album_Artist.class);
                albumartist.putExtra("item",item);
                albumartist.putExtra("itemname",itemname);
                startActivity(albumartist);
            }
            else{
                song.play(getContext(),0,item,itemname);
                song.setIsshuffled(false);
            }
        }
    };

    public Albums_Artists_Fragment(String item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums_artists, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recview_album);
        AlbumsArtistsAdapter albumsArtistsAdapter = new AlbumsArtistsAdapter(getContext(),item,Listner);
        recyclerView.setAdapter(albumsArtistsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        return view;
    }
}
