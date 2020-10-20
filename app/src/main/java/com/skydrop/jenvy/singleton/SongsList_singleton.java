package com.skydrop.jenvy.singleton;

import com.skydrop.jenvy.models.SongModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SongsList_singleton {
    public final static String ALBUM = "Album";
    public final static String SONGS = "Songs";
    public final static String ARTIST = "Artist";

    private ArrayList<SongModel> songsList;
    private HashMap<String, List<Integer>> albums;
    private HashMap<String, List<Integer>> artists;

    private List<String> albumNames;
    private List<String> artistNames;

    private static SongsList_singleton Instance = new SongsList_singleton();

    public static SongsList_singleton getInstance() {
        return Instance;
    }

    private SongsList_singleton() {
        songsList = new ArrayList<>();
        albums = new HashMap<>();
        artists = new HashMap<>();
    }


    public void setSongsList(SongModel songItem) {
        songsList.add(songItem);

        if (!albums.containsKey(songItem.getAlbum())) {
            albums.put(songItem.getAlbum(), new ArrayList<Integer>());
        }
        Objects.requireNonNull(albums.get(songItem.getAlbum())).add(songsList.size() - 1);

        if (!artists.containsKey(songItem.getArtist())) {
            artists.put(songItem.getArtist(), new ArrayList<Integer>());
        }
        Objects.requireNonNull(artists.get(songItem.getArtist())).add(songsList.size() - 1);
    }

    public void setList() {
        artistNames = new ArrayList<>(artists.keySet());
        albumNames = new ArrayList<>(albums.keySet());
    }


    public String getItemName(String item, int pos) {
        if (item.equals(ARTIST)) {
            return artistNames.get(pos);
        } else if (item.equals(ALBUM)) {
            return albumNames.get(pos);
        }
        return null;
    }

    public SongModel getItemModel(String item, String itemName, int pos) {
        if (item.equals(ARTIST)) {
            return songsList.get(Objects.requireNonNull(artists.get(itemName)).get(pos));
        }
        if (item.equals(ALBUM)) {
            return songsList.get(Objects.requireNonNull(albums.get(itemName)).get(pos));
        }
        return songsList.get(pos);
    }


    public int getSize(String item) {
        if (item.equals(ARTIST)) {
            return artistNames.size();
        } else if (item.equals(ALBUM)) {
            return albumNames.size();
        }
        return songsList.size();
    }

    public int getSize(String item, String itemName) {
        if (item.equals(ARTIST)) {
            return Objects.requireNonNull(artists.get(itemName)).size();
        }
        if (item.equals(ALBUM)) {
            return Objects.requireNonNull(albums.get(itemName)).size();
        }
        return songsList.size();
    }
}
