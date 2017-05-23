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
    ResultFragment resultFragment;
    HistoryFragment historyFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedTag = "com.example.almal.mp3tube";
    ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();

    ArrayList<VideoInfo> videoInfoArray = new ArrayList<>();
    VideoInfo info;

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
        resultFragment = ResultFragment.newInstance("search","text");
        pagerAdapter.addFragment(resultFragment);
        musicPlayerFragment = MusicPlayerFragment.newInstance("search","",videoInfo,mediaPlayer);
        pagerAdapter.addFragment(musicPlayerFragment);
        historyFragment = HistoryFragment.newInstance("search","text");
        pagerAdapter.addFragment(historyFragment);
        viewPager.setAdapter(pagerAdapter);


        sharedPreferences = getSharedPreferences(sharedTag,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Log.i("sharedpref", String.valueOf(sharedPreferences.getInt("size",0)));

        Button searchButton = (Button) findViewById(R.id.search_button_search_fragment);
        final EditText searchText = (EditText) findViewById(R.id.edit_text_search_fragment);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("searchButton","isclicked");
                String text = searchText.getText().toString();
                text.trim();
                if(!text.isEmpty()){
                    View keyboard = AudioHandlingActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    //pagerAdapter.notifyDataSetChanged();
                    resultFragment.search(text);

                    viewPager.setCurrentItem(0);
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
                viewPager.setCurrentItem(1);
                return true;
            case R.id.history:
                videoInfoArray.clear();
                int size = sharedPreferences.getInt("size",0);
                for(int i =0;i<size;i++) {
                    String title = sharedPreferences.getString("title"+i, "");
                    Log.i("historyVideoInfo", title+i+"+"+sharedPreferences.getString("title"+i, ""));
                    String url = sharedPreferences.getString("url"+i, "");
                    String image = sharedPreferences.getString("image"+i, "");
                    info = new VideoInfo(title, "", image, url, getApplicationContext());
                    //Log.i("historyVideoInfo", histVideoInfo.toString());
                    videoInfoArray.add(info);
                }

                viewPager.setCurrentItem(2);
                historyFragment.getUpdated(videoInfoArray);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()) {
            musicPlayerFragment.mHandler.removeCallbacks(musicPlayerFragment.mUpdateTimeTask);
        }
        if(viewPager.getCurrentItem()==2){
            viewPager.setCurrentItem(0);
        }
        else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
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
        }, 1000);
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
            editor.putInt("size",videoInfoArrayList.size());
            editor.commit();
            for (int i=0;i<videoInfoArrayList.size();i++) {
                editor.putString("title"+i, videoInfoArrayList.get(i).getTitle());
                editor.putString("url"+i, videoInfoArrayList.get(i).getUrl());
                editor.putString("image"+i, videoInfoArrayList.get(i).getImage());
                editor.commit();
            }
            Log.i("resultinteraction",videoInfoArrayList.toString());



            viewPager.setCurrentItem(1);
            musicPlayerFragment.downloadSong(item);
        }
    }

    @Override
    public void onMusictInteraction(String action, String message) {
    }

    @Override
    public void onHistoryInteraction(String action, VideoInfo videoInfo) {
        if(action.equals("play")) {
            viewPager.setCurrentItem(1);

            musicPlayerFragment.downloadSong(videoInfo);
        }
    }
}
