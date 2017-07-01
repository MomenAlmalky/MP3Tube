package com.example.almal.mp3tube.data;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.model.Response;
import com.example.almal.mp3tube.data.remote.SearchService;
import com.example.almal.mp3tube.utilities.GlobalEntities;

import java.util.List;

/**
 * Created by almal on 2017-06-14.
 */

public class DataManager {

    private static DataManager dataManager;

    private final SearchService mSearchService;
    private final Context mContext;


    public DataManager(SearchService mSearchService, Context mContext) {
        Log.i(GlobalEntities.DATA_MANAGER_TAG, "DataManager: created");

        this.mSearchService = mSearchService;
        this.mContext = mContext;
    }
    public static DataManager getInstance(Context mContext, SearchService mSearchService){
        if(dataManager == null){
            dataManager = new DataManager(mSearchService,mContext);
        }
        return dataManager;
    }
    public static DataManager getInstance() {
        if(dataManager != null){
            return dataManager;
        }

        throw new IllegalArgumentException("Should use getInstance(Context mContext, Service mService at least once before using this method.");
    }

    public static boolean isNull(){ return dataManager == null; }

    public rx.Observable<Response> searchVideo(String q , int maxResults){
        return mSearchService.searchVideo(q,GlobalEntities.SNIPPET,maxResults,GlobalEntities.VIDEO_TYPE,GlobalEntities.YOUTUBE_KEY);
    }


}
