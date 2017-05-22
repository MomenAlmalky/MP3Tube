package com.example.almal.mp3tube.AudioHandling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.VideoInfo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AudioHandlingActivity extends AppCompatActivity implements SearchFragment.OnSearchInteractionListener, ResultFragment.OnResultInteractionListener,MusicPlayerFragment.OnMusicInteractionListener,HistoryFragment.OnHistoryInteractionListener{
    ViewPager viewPager;
    //String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=momen&key={YOUR_API_KEY}";
    //String key = "AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";
    com.example.almal.mp3tube.PagerAdapter pagerAdapter;
    MusicPlayerFragment musicPlayerFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedTag = "com.example.almal.mp3tube";
    ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();

    boolean exit = false;
    VideoInfo videoInfo;
    boolean doubleBackToExitPressedOnce = false;

    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;

    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context,AudioHandlingActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_handling);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        pagerAdapter = new com.example.almal.mp3tube.PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(SearchFragment.newInstance("momen","momen"));
        viewPager.setAdapter(pagerAdapter);



        Button searchButton = (Button) findViewById(R.id.search_button_search_fragment);
        final EditText searchText = (EditText) findViewById(R.id.edit_text_search_fragment);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("searchButton","isclicked");
                String text = searchText.getText().toString();
                if(!text.isEmpty()){
                    View keyboard = AudioHandlingActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    pagerAdapter.addFragment(ResultFragment.newInstance("search",text));
                    pagerAdapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
                else {
                    Toast.makeText(getApplicationContext(),"You must enter a word to start search",Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.song_play:
                viewPager.setCurrentItem(viewPager.getChildCount());
                return true;
            case R.id.history:
                pagerAdapter.addFragment(new HistoryFragment().newInstance("ali","ali"));
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(viewPager.getChildCount());
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()) {
            musicPlayerFragment.mHandler.removeCallbacks(musicPlayerFragment.mUpdateTimeTask);
        }
        viewPager.setCurrentItem(viewPager.getChildCount()-1);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.release();
    }
        @Override
    public void OnSearchInteractionListener(String action, String message) {
            if(action.equals("search")) {
                Log.i("ListenerAction = ", action);
                Log.i("ListenerMessage = ", message);


            }else if(action.equals("")){

            }
    }

    @Override
    public void onResultInteraction(String action, VideoInfo item) {
        if(action.equals("play")) {
            videoInfoArrayList.add(item);

            Log.i("videourlclick", "https://lysten-korayementality.c9users.io/music/" + item.getUrl());
            if(mediaPlayer.isPlaying()){
                Log.i("checkmedia","reset");
                //mediaPlayer.release();
                viewPager.removeView(viewPager.getRootView());
                musicPlayerFragment.mHandler.removeCallbacks(musicPlayerFragment.mUpdateTimeTask);
                mediaPlayer.release();
            }


            sharedPreferences = getSharedPreferences(sharedTag,Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            Set<VideoInfo> set = new HashSet<>();

            Gson gson = new Gson();

            String json = gson.toJson(videoInfoArrayList);
            editor.putString("videoInfo",json);
            editor.commit();


            videoInfo = item;

            try {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource("https://lysten-korayementality.c9users.io/music/" + videoInfo.getUrl());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(100f, 100f);
                mediaPlayer.setLooping(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //musicPlayerFragment.mHandler.removeCallbacks(musicPlayerFragment.mUpdateTimeTask);

                    musicPlayerFragment = MusicPlayerFragment.newInstance("play",videoInfo.getTitle(),videoInfo,mediaPlayer);
                    pagerAdapter.addFragment(musicPlayerFragment);
                    pagerAdapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(pagerAdapter.getCount());
                    Log.i("SongDuration", String.valueOf(mediaPlayer.getDuration()));
                }
            });


        }
    }

    @Override
    public void onMusictInteraction(String action, String message) {
        if(action.equals("play")) {
            mediaPlayer.start();
        }
        else if(action.equals("pause")){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onHistoryInteraction(String action, VideoInfo videoInfo) {

    }
}
