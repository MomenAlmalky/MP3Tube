package com.example.almal.mp3tube.AudioHandling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.VideoInfo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by almal on 2017-05-18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VideoViewHolder> implements View.OnClickListener{
    List<VideoInfo> videoInfo;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(VideoInfo item);
    }

    public RecyclerViewAdapter(List<VideoInfo> videoInfo, OnItemClickListener listener) {
        this.videoInfo = videoInfo;
        this.listener = listener;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        VideoViewHolder pvh = new VideoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoInfo vi = videoInfo.get(position);
        /*VideoViewHolder.videoTitle.setText(vi.getTitle());
        VideoViewHolder.videoInfo.setText(vi.getInfo());
        Log.i("urlimage",vi.getImage());
        Picasso.with(vi.getContext()).load(vi.getImage()).into(VideoViewHolder.videoPhoto);
*/
        holder.bind(vi,listener);
    }

    @Override
    public int getItemCount() {
        return videoInfo.size();
    }

    @Override
    public void onClick(View view) {
        if(view instanceof CardView){
            Log.i("cardview","done");
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        static CardView cv;
        static TextView videoTitle;
        static TextView videoInfo;
        static ImageView videoPhoto;

        VideoViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            videoTitle = (TextView)itemView.findViewById(R.id.video_title);
            videoInfo = (TextView)itemView.findViewById(R.id.video_info);
            videoPhoto = (ImageView)itemView.findViewById(R.id.video_photo);
        }
        public void bind(final VideoInfo item, final OnItemClickListener listener) {
            VideoViewHolder.videoTitle.setText(item.getTitle());
            VideoViewHolder.videoInfo.setText(item.getInfo());
            Log.i("urlimage",item.getImage());
            Picasso.with(item.getContext()).load(item.getImage()).into(VideoViewHolder.videoPhoto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });

        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
