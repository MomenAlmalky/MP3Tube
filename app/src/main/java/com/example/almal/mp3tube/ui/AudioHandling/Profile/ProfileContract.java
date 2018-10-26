package com.example.almal.mp3tube.ui.AudioHandling.Profile;

import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface ProfileContract {
    public interface View extends BaseView {
        void showHistory(List<FirebaseTracks> itemsList);

    }

    public interface Presenter {
        void getHistory();
    }


}
