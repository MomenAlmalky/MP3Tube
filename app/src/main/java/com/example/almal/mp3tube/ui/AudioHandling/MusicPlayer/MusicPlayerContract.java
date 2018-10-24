package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface MusicPlayerContract {
    public interface View extends BaseView {
        void playMusic(FirebaseTracks firebaseTrack);
        void isLikedCallBack(boolean isLiked,boolean btn);
    }

    public interface Presenter {
    }

}
