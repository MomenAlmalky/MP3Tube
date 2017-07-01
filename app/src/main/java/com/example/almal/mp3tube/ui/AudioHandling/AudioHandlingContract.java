package com.example.almal.mp3tube.ui.AudioHandling;

import android.media.MediaPlayer;

import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by almal on 2017-06-14.
 */

public class AudioHandlingContract {

    public interface View {
        void playMusic(MediaPlayer mediaPlayer, VideoInfo videoInfo);
        void stopMusic(MediaPlayer mediaPlayer, VideoInfo videoInfo);
        void nextMusic(MediaPlayer mediaPlayer, VideoInfo videoInfo);
        void updateNotification(MediaPlayer mediaPlayer,VideoInfo videoInfo);
        void showResults(List<VideoInfo> videoInfoList);
    }

    public interface Presenter {
        void playMusic(MediaPlayer mediaPlayer, VideoInfo videoInfo);
        void stopMusic(MediaPlayer mediaPlayer, VideoInfo videoInfo);
    }

}
