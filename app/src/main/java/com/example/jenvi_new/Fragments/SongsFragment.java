package com.example.jenvi_new.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jenvi_new.Adapters.songsListAdapter;
import com.example.jenvi_new.Interfaces.SongitemClickListener;
import com.example.jenvi_new.R;
import com.example.jenvi_new.singleton.song_singleton;

import static com.example.jenvi_new.singleton.SongsList_singleton.SONGS;


public class SongsFragment extends Fragment {

    private final song_singleton song = song_singleton.getInstance();
    private final SongitemClickListener Listener = new SongitemClickListener() {
        @Override
        public void onClick(int pos,String item, String itemname) {
            song.play(getContext(), pos,item,itemname);
            song.setIsshuffled(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        //region Rec View
        RecyclerView recview = view.findViewById(R.id.recview);
        songsListAdapter adapter = new songsListAdapter(getContext(), Listener,SONGS,null);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));
        recview.setAdapter(adapter);
        //endregion

        return view;
    }
}