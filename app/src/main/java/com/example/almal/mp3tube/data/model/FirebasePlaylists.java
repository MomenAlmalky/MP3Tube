package com.example.almal.mp3tube.data.model;

import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by almal on 2018-10-10.
 */
@IgnoreExtraProperties
public class FirebasePlaylists {

    public String playlist_id;
    public String title;
    public String url;
    public ArrayList<FirebaseTracks> tracks;

    public FirebasePlaylists(){

    }


    public FirebasePlaylists(String playlist_id, String title, String url,ArrayList<FirebaseTracks> tracks){

        this.playlist_id = playlist_id;
        this.title = title;
        this.url = url;
        this.tracks = tracks;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(GlobalEntities.DATABASE_REF_PLAYLIST_ID, playlist_id);
        result.put(GlobalEntities.DATABASE_REF_PLAYLIST_TITLE, title);
        result.put(GlobalEntities.DATABASE_REF_PLAYLIST_COVER_URL, url);
        result.put(GlobalEntities.DATABASE_REF_PLAYLIST_TRACKS, tracks);

        return result;
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<FirebaseTracks> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<FirebaseTracks> tracks) {
        this.tracks = tracks;
    }
}
