package com.example.almal.mp3tube.ui.AudioHandling.Profile.likes;

import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface LikeContract {
    public interface View extends BaseView {
        void showLikes(List<FirebaseTracks> itemsList);
        void playmusic(FirebaseTracks firebaseTrack);
    }

    public interface Presenter {
        void getLikes();
    }


}
