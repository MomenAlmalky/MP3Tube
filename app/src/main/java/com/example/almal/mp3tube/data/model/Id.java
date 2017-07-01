
package com.example.almal.mp3tube.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id {

    @SerializedName("kind")

    private String kind;
    @SerializedName("videoId")

    private String videoId;
    @SerializedName("channelId")

    private String channelId;
    @SerializedName("playlistId")

    private String playlistId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Id() {
    }

    /**
     *
     * @param playlistId
     * @param channelId
     * @param videoId
     * @param kind
     */
    public Id(String kind, String videoId, String channelId, String playlistId) {
        super();
        this.kind = kind;
        this.videoId = videoId;
        this.channelId = channelId;
        this.playlistId = playlistId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

}
