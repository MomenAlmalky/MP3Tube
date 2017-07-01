package com.example.almal.mp3tube.data.remote;

import com.example.almal.mp3tube.data.model.Response;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by almal on 2017-06-14.
 */

public interface SearchService {
    //String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&type=video&q="+searchword.replaceAll(" ","%20")+"&key=AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";

    @GET("search?")
    rx.Observable<Response> searchVideo(@Query("q") String searchword, @Query("part") String snippet, @Query("maxResults") int maxResults, @Query("type") String type, @Query("key") String key);

    class Creator{
        public static Retrofit retrofit;

        public static SearchService getService(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalEntities.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(SearchService.class);
        }
    }
}
