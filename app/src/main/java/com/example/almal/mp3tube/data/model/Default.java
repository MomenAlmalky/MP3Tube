
package com.example.almal.mp3tube.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Default {

    @SerializedName("url")

    private String url;
    @SerializedName("width")

    private Integer width;
    @SerializedName("height")
    
    private Integer height;

    /**
     * No args constructor for use in serialization
     *
     */
    public Default() {
    }

    /**
     *
     * @param height
     * @param width
     * @param url
     */
    public Default(String url, Integer width, Integer height) {
        super();
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}
