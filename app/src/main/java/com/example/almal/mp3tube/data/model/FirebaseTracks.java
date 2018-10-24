package com.example.almal.mp3tube.data.model;

import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by almal on 2018-10-10.
 */
@IgnoreExtraProperties
public class FirebaseTracks {

    public String track_id;
    public String track_title;
    public String track_author;
    public String track_image;
    public String track_url;

    public FirebaseTracks(){

    }


    public FirebaseTracks(String track_id, String track_title, String track_image,String track_url,String track_author){

        this.track_id = track_id;
        this.track_title = track_title;
        this.track_image = track_image;
        this.track_url = track_url;
        this.track_author = track_author;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(GlobalEntities.DATABASE_REF_TRACK_ID, track_id);
        result.put(GlobalEntities.DATABASE_REF_TRACK_TITLE, track_title);
        result.put(GlobalEntities.DATABASE_REF_TRACK_IMAGE, track_image);
        result.put(GlobalEntities.DATABASE_REF_TRACK_URL, track_url);
        result.put(GlobalEntities.DATABASE_REF_TRACK_AUTHOR, track_author);

        return result;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public String getTrack_title() {
        return track_title;
    }

    public void setTrack_title(String track_title) {
        this.track_title = track_title;
    }

    public String getTrack_author() {
        return track_author;
    }

    public void setTrack_author(String track_author) {
        this.track_author = track_author;
    }

    public String getTrack_image() {
        return track_image;
    }

    public void setTrack_image(String track_image) {
        this.track_image = track_image;
    }

    public String getTrack_url() {
        return track_url;
    }

    public void setTrack_url(String track_url) {
        this.track_url = track_url;
    }

    @Override
    public String toString() {
        return "FirebaseTracks{" +
                "track_id='" + track_id + '\'' +
                ", track_title='" + track_title + '\'' +
                ", track_author='" + track_author + '\'' +
                ", track_image='" + track_image + '\'' +
                ", track_url='" + track_url + '\'' +
                '}';
    }
}
