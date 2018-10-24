package com.example.almal.mp3tube.utilities;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by almal on 2017-05-18.
 */

public class LikesRecyclerViewAdapter extends RecyclerView.Adapter<LikesRecyclerViewAdapter.VideoViewHolder> implements View.OnClickListener{
    List<FirebaseTracks> itemList;
    private final OnItemClickListener listener;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(FirebaseTracks firebaseTrack);
    }

    public LikesRecyclerViewAdapter(Context context, List<FirebaseTracks> firebaseTrackses, OnItemClickListener listener) {
        this.itemList = firebaseTrackses;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        VideoViewHolder pvh = new VideoViewHolder(v,context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        FirebaseTracks vi = itemList.get(position);
        /*VideoViewHolder.videoTitle.setText(vi.getTitle());
        VideoViewHolder.snippet.setText(vi.getInfo());
        Log.i("urlimage",vi.getImage());
        Picasso.with(vi.getContext()).load(vi.getImage()).into(VideoViewHolder.videoPhoto);
*/
        holder.bind(vi,listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(View view) {
        if(view instanceof CardView){

        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        static CardView cv;
        static TextView videoTitle;
        static TextView videoInfo;
        static ImageView videoPhoto;
        Context context;

        VideoViewHolder(View itemView,Context context) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            videoTitle = (TextView)itemView.findViewById(R.id.video_title);
            videoInfo = (TextView)itemView.findViewById(R.id.video_info);
            videoPhoto = (ImageView)itemView.findViewById(R.id.video_photo);
            this.context = context;
        }
        public void bind(final FirebaseTracks item, final OnItemClickListener listener) {
            VideoViewHolder.videoTitle.setText(item.getTrack_title());
            VideoViewHolder.videoInfo.setText(item.getTrack_author());
            Picasso.with(context).load(item.getTrack_image()).into(VideoViewHolder.videoPhoto);

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
	//added to remove duplicated items
	
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
