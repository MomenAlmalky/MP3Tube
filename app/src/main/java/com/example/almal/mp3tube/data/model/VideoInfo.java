package com.example.almal.mp3tube.data.model;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by almal on 2017-05-18.
 */

public class VideoInfo {
    String title;
    String info;
    String image;
    String url;
    Context context;

    public VideoInfo(String title, String info, String image,String url,Context context) {
        this.title = title;
        this.info = info;
        this.image = image;
        this.url = url;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", context=" + context +
                '}';
    }
}
