package com.example.almal.mp3tube.ui.AudioHandling.Search;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Response;
import com.example.almal.mp3tube.data.model.Snippet;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by almal on 2017-06-24.
 */

public class SearchPresenter extends BasePresenter<SearchContract.View>implements SearchContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;


    public SearchPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
    }

    @Override
    public void attachView(SearchContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscriptions!=null) mSubscriptions.unsubscribe();
    }



    @Override
    public void search_youtube_API(String search_word) {
        checkViewAttached();
        Log.i(GlobalEntities.SEARCH_PRRESENTER,"SearchYoutube: "+search_word);
        search_word = search_word.replaceAll(" ","%20");
        mSubscriptions = mDataManager.searchVideo(search_word,25)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {
                        Log.i(GlobalEntities.SEARCH_PRRESENTER,"onNext:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.i(GlobalEntities.SEARCH_PRRESENTER,"onNext:");
                        List<Item> items = response.getItems();
                        getBaseView().showResults(items);
                    }
                });


    }
}
