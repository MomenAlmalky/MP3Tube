package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Response;
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

public class MusicPlayerPresenter extends BasePresenter<MusicPlayerContract.View>implements MusicPlayerContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;


    public MusicPlayerPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
    }

    @Override
    public void attachView(MusicPlayerContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscriptions!=null) mSubscriptions.unsubscribe();
    }


    @Override
    public void streamClickedMusic(Item item) {
        getBaseView().playMusic(item);
    }
}
