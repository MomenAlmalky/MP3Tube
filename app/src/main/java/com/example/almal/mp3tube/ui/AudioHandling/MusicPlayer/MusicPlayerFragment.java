package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.HandleProgressBar;
import com.example.almal.mp3tube.utilities.NotiService;
import com.example.almal.mp3tube.utilities.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.almal.mp3tube.utilities.NotiService.mBuilder;
import static com.example.almal.mp3tube.utilities.NotiService.mNotificationManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicPlayerFragment.OnMusicInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment implements MusicPlayerContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static VideoInfo videoInfo;
    public static boolean played;
    String sharedTag = "com.example.almal.m" +
            "p3tube";
    Button play;
    TextView current,duration,songTitle;
    SeekBar seekBar;
    ImageView imageView;

    HandleProgressBar handleProgressBar = new HandleProgressBar();
    public Handler mHandler = new Handler();
    RequestQueue requestQueue;
    ArrayList<VideoInfo> videoInfos = new ArrayList<>();
    MusicPlayerPresenter mMusicPresenter;

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


    // TODO: Rename and change types and number of parameters
    public static MusicPlayerFragment newInstance(String param1, String param2) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        Log.i(GlobalEntities.MUSIC_FRAGMENT_TAG+"onattach","done");
        if (context instanceof OnMusicInteractionListener) {
            mListener = (OnMusicInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.i(GlobalEntities.MUSIC_FRAGMENT_TAG+"onactivit","done");

        super.onActivityCreated(savedInstanceState);

        mMusicPresenter = new MusicPlayerPresenter(DataManager.getInstance(),getActivity());
        mMusicPresenter.attachView(this);

        play = (Button) getView().findViewById(R.id.btn_play_music_player);
        current = (TextView) getView().findViewById(R.id.current_tv_music_player);
        duration = (TextView) getView().findViewById(R.id.duration_tv_music_player);
        songTitle = (TextView) getView().findViewById(R.id.tv_title_song_music_player);
        seekBar = (SeekBar) getView().findViewById(R.id.seekBar);
        imageView = (ImageView) getView().findViewById(R.id.imageview_music_player);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void streamSong(Item item){

        mMusicPresenter.streamClickedMusic(item);
    }

    @Override
    public void playMusic(Item item) {
        Log.i(GlobalEntities.MUSIC_FRAGMENT_TAG+"item_inf:",item.toString());
        Picasso.with(getContext()).load(item.getSnippet().getThumbnails().getDefault().getUrl()).into(imageView);
        mListener.onMusictInteraction(GlobalEntities.PLAY_TAG,item);
    }


    public interface OnMusicInteractionListener {
        // TODO: Update argument type and name
        void onMusictInteraction(String action, Item item);
    }
}
