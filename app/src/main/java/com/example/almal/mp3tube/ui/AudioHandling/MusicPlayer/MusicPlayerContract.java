package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface MusicPlayerContract {
    public interface View extends BaseView {
        void playMusic(Item item);
    }

    public interface Presenter {
        void streamClickedMusic(Item item);
    }

}
