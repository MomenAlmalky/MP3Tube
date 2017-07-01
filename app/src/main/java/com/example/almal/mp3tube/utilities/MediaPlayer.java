package com.example.almal.mp3tube.utilities;

import android.content.Context;

import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchFragment;

/**
 * Created by almal on 2017-06-14.
 */

public class MediaPlayer {
    private static android.media.MediaPlayer mediaPlayer;
    private Context context;

    private MediaPlayer(Context context) {
        this.context = context;
    }

    public static android.media.MediaPlayer getInstance(Context context){
        if(mediaPlayer == null){
            mediaPlayer = new android.media.MediaPlayer();

        }
        return mediaPlayer;
    }

    public static boolean isNull(){ return mediaPlayer == null; }




}
