package com.example.almal.mp3tube.ui.AudioHandling.Search;

import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;

import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface SearchContract {
    public interface View extends BaseView {
        void showResults(List<Item> itemList);
    }

    public interface Presenter {
        void search_youtube_API(String search_word);
    }

}
