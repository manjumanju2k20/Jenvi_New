package com.example.jenvi_new.singleton;

import com.example.jenvi_new.models.SongModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SongsList_singleton {
    public final static String ALBUM = "Album";
    public final static String SONGS = "Songs";
    public final static String ARTIST = "Artist";

    private ArrayList<SongModel> songslist;
    private HashMap<String, List<Integer>> albums;
    private HashMap<String, List<Integer>> artists;

    private List<String> albumnames;
    private List<String> artistnames;

    private static SongsList_singleton Instance = new SongsList_singleton();

    public static SongsList_singleton getInstance(){
        return Instance;
    }

    private SongsList_singleton() {
        songslist = new ArrayList<>();
        albums = new HashMap<>();
        artists = new HashMap<>();
    }



    public void setSongslist(SongModel songitem) {
        songslist.add(songitem);

        if(!albums.containsKey(songitem.getAlbum())){
            albums.put(songitem.getAlbum(),new ArrayList<Integer>());
        }
        Objects.requireNonNull(albums.get(songitem.getAlbum())).add(songslist.size()-1);

        if(!artists.containsKey(songitem.getArtist())){
            artists.put(songitem.getArtist(),new ArrayList<Integer>());
        }
        Objects.requireNonNull(artists.get(songitem.getArtist())).add(songslist.size()-1);
    }

    public void setlist(){
        artistnames = new ArrayList<>(artists.keySet());
        albumnames = new ArrayList<>(albums.keySet());
    }


    public String getitemname(String item, int pos){
        if(item.equals(ARTIST)){
            return artistnames.get(pos);
        }
        else if(item.equals(ALBUM)){
            return albumnames.get(pos);
        }
        return null;
    }

    public SongModel getitemmodel(String item, String itemname,int pos){
        if(item.equals(ARTIST)){
            return songslist.get(Objects.requireNonNull(artists.get(itemname)).get(pos));
        }
        if(item.equals(ALBUM)){
            return songslist.get(Objects.requireNonNull(albums.get(itemname)).get(pos));
        }
        return songslist.get(pos);
    }


    public int getsize(String item){
        if(item.equals(ARTIST)){
            return artistnames.size();
        }
        else if(item.equals(ALBUM)){
            return albumnames.size();
        }
        return songslist.size();
    }

    public int getsize(String item,String itemname){
        if(item.equals(ARTIST)){
            return Objects.requireNonNull(artists.get(itemname)).size();
        }
        if(item.equals(ALBUM)){
            return Objects.requireNonNull(albums.get(itemname)).size();
        }
        return songslist.size();
    }
}

