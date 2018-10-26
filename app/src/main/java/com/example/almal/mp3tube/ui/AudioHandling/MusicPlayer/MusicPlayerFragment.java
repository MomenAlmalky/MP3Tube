package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.squareup.picasso.Picasso;

public class MusicPlayerFragment extends Fragment implements MusicPlayerContract.View {

    public boolean pause_flag = false;;
    Button play, next_btn, previous_btn, like_btn, add_to_playlist_btn;
    TextView current, duration, songTitle, songArtist;
    SeekBar seekBar;
    ImageView imageView;

    MusicPlayerPresenter mMusicPresenter;
    FirebaseTracks firebaseTrack;


    private OnMusicInteractionListener mListener;
    private OnHandlingSeekBarListener handlingSeekBarListener;


    public MusicPlayerFragment() {
        // Required empty public constructor

    }


    public static MusicPlayerFragment newInstance() {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize non-graphical variables

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        //initialize graphical variables
        next_btn = (Button) view.findViewById(R.id.btn_next_music_player);
        previous_btn = (Button) view.findViewById(R.id.btn_prev_music_player);
        like_btn = (Button) view.findViewById(R.id.btn_like_player_player);
        add_to_playlist_btn = (Button) view.findViewById(R.id.btn_playlist_music_player);

        current = (TextView) view.findViewById(R.id.current_tv_music_player);
        duration = (TextView) view.findViewById(R.id.duration_tv_music_player);
        songTitle = (TextView) view.findViewById(R.id.tv_title_song_music_player);
        songArtist = (TextView) view.findViewById(R.id.tv_artist_song_music_player);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        imageView = (ImageView) view.findViewById(R.id.imageview_music_player);
        play = (Button) view.findViewById(R.id.btn_play_music_player);

        // set onclick listner to like button
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check from presenter if it was liked before
                mMusicPresenter.isLiked(firebaseTrack, GlobalEntities.TRUE_VALUE);
            }
        });

        // set onclick listner to play button and send the state of play/pause to
        // audioHandling activity
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseTrack != null) {
                    if (pause_flag == true) {
                        pause_flag = false;
                        resume();
                        mListener.onMusictInteraction(GlobalEntities.NOTIFY_PAUSE, firebaseTrack);

                    } else {
                        pause_flag = true;
                        pause();
                        mListener.onMusictInteraction(GlobalEntities.NOTIFY_PAUSE, firebaseTrack);

                    }
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = 0;
                handlingSeekBarListener.OnHandlingSeekBarListener(GlobalEntities.ON_START_TRACKING, progress);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                handlingSeekBarListener.OnHandlingSeekBarListener(GlobalEntities.ON_STOP_TRACKING, progress);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mMusicPresenter = new MusicPlayerPresenter(DataManager.getInstance(), getActivity());
        mMusicPresenter.attachView(this);

        Log.i(GlobalEntities.MUSIC_FRAGMENT_TAG + "onattach", "done");
        if (context instanceof OnMusicInteractionListener) {
            mListener = (OnMusicInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentMusicListener");
        }

        if (context instanceof OnHandlingSeekBarListener) {
            handlingSeekBarListener = (OnHandlingSeekBarListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentHandlingSeekbarListener");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mMusicPresenter.detachView();
        handlingSeekBarListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrackUI(firebaseTrack);
        if (pause_flag == true) {
            pause();
        } else {
            resume();
        }
    }


    public void streamSong(FirebaseTracks firebaseTrack) {
        this.firebaseTrack = firebaseTrack;
        playMusic(GlobalEntities.PLAY_TAG, firebaseTrack);
    }

    @Override
    public void playMusic(String tag, FirebaseTracks firebaseTrack) {
        if (firebaseTrack != null) {
            loadTrackUI(firebaseTrack);
            mMusicPresenter.isLiked(firebaseTrack, GlobalEntities.FALSE_VALUE);
            mMusicPresenter.addToHistory(firebaseTrack);
            mListener.onMusictInteraction(tag, firebaseTrack);
        }
    }

    public void loadTrackUI(FirebaseTracks firebaseTrack) {
        if (firebaseTrack != null) {
            Picasso.with(getContext()).load(firebaseTrack.getTrack_image()).into(imageView);
            songTitle.setText(firebaseTrack.getTrack_title());
            songArtist.setText(firebaseTrack.getTrack_author());
        }
    }

    @Override
    public void isLikedCallBack(boolean isliked, boolean likeBtn) {
        if (isliked && likeBtn) {
            mMusicPresenter.unLikeTrack(firebaseTrack);
            like_btn.setBackgroundResource(R.drawable.icons8_unlike_heart);
        } else if (!isliked && likeBtn) {
            mMusicPresenter.likeTrack(firebaseTrack);
            like_btn.setBackgroundResource(R.drawable.icons8_liked_heart);
        } else if (isliked && !likeBtn) {
            like_btn.setBackgroundResource(R.drawable.icons8_liked_heart);
        } else {
            like_btn.setBackgroundResource(R.drawable.icons8_unlike_heart);
        }
    }

    public void pause() {
        play.setBackgroundResource(R.drawable.ic_play_button_player);
    }

    public void resume() {
        play.setBackgroundResource(R.drawable.ic_pause_button_player_7);
    }


    public interface OnMusicInteractionListener {
        // TODO: Update argument type and name
        void onMusictInteraction(String action, FirebaseTracks firebaseTrack);
    }

    public interface OnHandlingSeekBarListener {
        // TODO: Update argument type and name
        void OnHandlingSeekBarListener(String action, int progress);
    }

    public void handleSeekBar(String totalDuration, String currentDuration, int percent) {

        // Displaying Total Duration time
        duration.setText("" + totalDuration);
        // Displaying time completed playing
        current.setText("" + currentDuration);

        // Updating progress bar
        int progress = (int) (percent);

        seekBar.setProgress(progress);

    }

}
