package com.example.almal.mp3tube.utilities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by almal on 2017-05-18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VideoViewHolder> implements View.OnClickListener{
    List<Item> itemList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public RecyclerViewAdapter(List<Item> snippet, OnItemClickListener listener) {
        this.itemList = snippet;
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
        Item vi = itemList.get(position);
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
        public void bind(final Item item, final OnItemClickListener listener) {
            VideoViewHolder.videoTitle.setText(item.getSnippet().getTitle());
            VideoViewHolder.videoInfo.setText(item.getSnippet().getChannelTitle());
            //Log.i("urlimage",item.getImage());
            Picasso.with(AudioHandlingActivity.context).load(item.getSnippet().getThumbnails().getDefault().getUrl()).into(VideoViewHolder.videoPhoto);
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