package com.example.almal.mp3tube.AudioHandling;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.Utilities.HandleProgressBar;
import com.example.almal.mp3tube.VideoInfo;
import com.squareup.picasso.Picasso;

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
     private static VideoInfo videoInfo;
    Button play;
    TextView current,duration,songTitle;
    SeekBar seekBar;
    ImageView imageView;
    HandleProgressBar handleProgressBar = new HandleProgressBar();
    public Handler mHandler = new Handler();
    boolean played =false;
    private static MediaPlayer mediaPlayer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMusicInteractionListener mListener;

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
        Picasso.with(getContext()).load(videoInfo.getImage()).into(imageView);
        songTitle.setText(videoInfo.getTitle());
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(played == false) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    played = true;
                    mListener.onMusictInteraction("play", "music");
                    play.setBackgroundResource(R.drawable.img_btn_pause);
                    seekBar.setProgress(0);
                    seekBar.setMax(100);
                    updateProgressBar();
                }else if(played == true) {
                    played =false;
                    mListener.onMusictInteraction("pause", "music");
                    play.setBackgroundResource(R.drawable.img_btn_play);
                }
            }
        });
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
