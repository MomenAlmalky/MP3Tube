package com.example.almal.mp3tube.ui.AudioHandling;

import android.media.MediaPlayer;

import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.List;

/**
 * Created by almal on 2017-06-14.
 */

public class AudioHandlingContract {

    public interface View extends BaseView {
    }

    public interface Presenter {
        void signout();
    }

}
