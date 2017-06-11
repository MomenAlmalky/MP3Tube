package com.example.almal.mp3tube.AudioHandling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.Utilities.HandleProgressBar;
import com.example.almal.mp3tube.VideoInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.almal.mp3tube.AudioHandling.NotiService.mBuilder;
import static com.example.almal.mp3tube.AudioHandling.NotiService.mNotificationManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicPlayerFragment.OnMusicInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static VideoInfo videoInfo;
    public static boolean played;
    String sharedTag = "com.example.almal.mp3tube";
    Button play;
    TextView current,duration,songTitle;
    SeekBar seekBar;
    ImageView imageView;
    HandleProgressBar handleProgressBar = new HandleProgressBar();
    public Handler mHandler = new Handler();
    RequestQueue requestQueue;
    ArrayList<VideoInfo> videoInfos = new ArrayList<>();

    public static MediaPlayer mediaPlayer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMusicInteractionListener mListener;

    BroadcastReceiver receiver;
    Intent service;

    public MusicPlayerFragment() {
        // Required empty public constructor

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPlayerFragment newInstance(String param1, String param2,VideoInfo videoInfo1,MediaPlayer mediaPlayer1) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        mediaPlayer = mediaPlayer1;
        videoInfo = videoInfo1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMusicInteractionListener) {
            mListener = (OnMusicInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
        play = (Button) getView().findViewById(R.id.btn_play_music_player);
        current = (TextView) getView().findViewById(R.id.current_tv_music_player);
        duration = (TextView) getView().findViewById(R.id.duration_tv_music_player);
        songTitle = (TextView) getView().findViewById(R.id.tv_title_song_music_player);
        seekBar = (SeekBar) getView().findViewById(R.id.seekBar);

        service = new Intent(getActivity(),NotiService.class);
        getActivity().registerReceiver(receiver,new IntentFilter("play"));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = handleProgressBar.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();
            }
        });
        imageView = (ImageView) getView().findViewById(R.id.imageview_music_player);

    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            duration.setText(""+handleProgressBar.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            current.setText(""+handleProgressBar.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(handleProgressBar.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public void addToQueue(VideoInfo item){
        videoInfos.clear();
        requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&type=video&relatedToVideoId="+item.getUrl()+"&key=AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";
        JsonObjectRequest stringRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    jsonArray = (JSONArray) response.get("items");
                    Log.i("response",jsonObject.toString());
                    for(int i = 0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String title = ((JSONObject) jsonObject.get("snippet")).getString("title");
                        String info = ((JSONObject) jsonObject.get("snippet")).getString("channelTitle");
                        String imageurl = ((JSONObject)((JSONObject)((JSONObject) jsonObject.get("snippet")).get("thumbnails")).get("default")).getString("url");
                        String videourl = ((JSONObject) jsonObject.get("id")).getString("videoId");
                        VideoInfo videoInfo = new VideoInfo(title,info,imageurl,videourl,getContext());
                        Log.i("VideoInfo "+i+"= ",videoInfo.toString());
                        videoInfos.add(videoInfo);
                    }


                    Log.i("videoinfoarray",videoInfos.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);



    }

    public void handle_play(){
        Intent yesReceive = new Intent();
        yesReceive.setAction("play");
        PendingIntent pendingIntentYes;
        NotificationCompat.Action action1;
        ;
        if(played == false) {
            played = true;
            play.setBackgroundResource(R.drawable.img_btn_pause);

            //handling notifications
            yesReceive.putExtra("player","pause");
            pendingIntentYes = PendingIntent.getBroadcast(getContext(),12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            action1 = new NotificationCompat.Action.Builder(R.drawable.small_pause, "Pause", pendingIntentYes).build();
            mBuilder.mActions.clear();
            mBuilder.addAction(action1);
            mNotificationManager.notify(0,mBuilder.build());

            mediaPlayer.start();
            updateProgressBar();
        }else if(played == true) {
            played =false;
            play.setBackgroundResource(R.drawable.img_btn_play);

            //handling notifications
            yesReceive.putExtra("player","play");
            pendingIntentYes = PendingIntent.getBroadcast(getContext(),12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
            action1 = new NotificationCompat.Action.Builder(R.drawable.small_play, "Play", pendingIntentYes).build();
            mBuilder.mActions.clear();
            mBuilder.addAction(action1);
            mNotificationManager.notify(0,mBuilder.build());


            mediaPlayer.pause();
        }

    }

    public void downloadSong(VideoInfo item){

        getActivity().startService(service);
        played = true;
        play.setBackgroundResource(R.drawable.img_btn_pause);
        play.setVisibility(View.INVISIBLE);
        current.setText("0:00");
        mediaPlayer.reset();
        Log.i("videourlclick", "https://lysten-korayementality.c9users.io/music/" + item.getUrl());
        if(mediaPlayer.isPlaying() || mediaPlayer.isLooping()){
            Log.i("checkmedia","reset");
            try{
            mHandler.removeCallbacks(mUpdateTimeTask);
            }catch (Exception e){
                e.printStackTrace();
            }
            mediaPlayer.reset();
        }

        videoInfo = item;

        Picasso.with(getContext()).load(videoInfo.getImage()).into(imageView);
        songTitle.setText(videoInfo.getTitle());


        try {

            mediaPlayer.setDataSource("https://lysten-korayementality.c9users.io/music/" + videoInfo.getUrl());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(100f, 100f);
            mediaPlayer.setLooping(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                //musicPlayerFragment.mHandler.removeCallbacks(musicPlayerFragment.mUpdateTimeTask);
                updateProgressBar();
                //musicPlayerFragment = MusicPlayerFragment.newInstance("play",videoInfo.getTitle(),videoInfo,mediaPlayer);
                play.setVisibility(View.VISIBLE);
                seekBar.setProgress(0);
                seekBar.setMax(100);
                mediaPlayer.start();
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onMusictInteraction("notification","start");
                        handle_play();
                    }
                });

                /*
                pagerAdapter.addFragment(musicPlayerFragment);
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(pagerAdapter.getCount());*/
                Log.i("SongDuration", String.valueOf(mediaPlayer.getDuration()));
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer1) {
                try{
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(videoInfos.size()!=0){
                    Log.i("completion","start");
                    downloadSong(videoInfos.get(0));

                    videoInfos.remove(0);
                }
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMusicInteractionListener {
        // TODO: Update argument type and name
        void onMusictInteraction(String action,String message);
    }
}
