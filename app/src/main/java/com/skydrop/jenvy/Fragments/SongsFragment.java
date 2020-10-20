package com.skydrop.jenvy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skydrop.jenvy.Adapters.songsListAdapter;
import com.skydrop.jenvy.Interfaces.SongItemClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.singleton.song_singleton;

import static com.skydrop.jenvy.singleton.SongsList_singleton.SONGS;


public class SongsFragment extends Fragment {

    private final song_singleton song = song_singleton.getInstance();
    private final SongItemClickListener Listener = new SongItemClickListener() {
        @Override
        public void onClick(int pos, String item, String itemName) {
            song.play(getContext(), pos, item, itemName);
            song.setShuffled(false);
            song.setRepeating(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        //region Rec View
        RecyclerView recView = view.findViewById(R.id.recview);
        songsListAdapter adapter = new songsListAdapter(getContext(), Listener, SONGS, null);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(adapter);
        //endregion

        return view;
    }
}