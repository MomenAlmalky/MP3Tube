package com.example.almal.mp3tube.ui.AudioHandling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.ui.AudioHandling.History.HistoryFragment;
import com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer.MusicPlayerFragment;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchFragment;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchPresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.NotiService;
import com.example.almal.mp3tube.utilities.PagerAdapter;

import java.util.ArrayList;


public class AudioHandlingActivity extends AppCompatActivity implements SearchFragment.OnResultInteractionListener,MusicPlayerFragment.OnMusicInteractionListener,HistoryFragment.OnHistoryInteractionListener{
    ViewPager viewPager;
    //String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=momen&key={YOUR_API_KEY}";
    //String key = "AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";
    PagerAdapter pagerAdapter;
    MusicPlayerFragment musicPlayerFragment;
    SearchFragment searchFragment;
    HistoryFragment historyFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedTag = "com.example.almal.mp3tube";
    ArrayList<VideoInfo> videoInfoArrayList = new ArrayList<>();

    ArrayList<VideoInfo> videoInfoArray = new ArrayList<>();
    VideoInfo info;
    public static Context context;

    boolean exit = false;
    VideoInfo videoInfo;
    boolean doubleBackToExitPressedOnce = false;

    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;

    BroadcastReceiver receiver;
    SearchPresenter mSearchPresenter;

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
        context = getApplicationContext();
        mediaPlayer = new MediaPlayer();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        searchFragment = SearchFragment.newInstance("search","text");
        pagerAdapter.addFragment(searchFragment);
        musicPlayerFragment = MusicPlayerFragment.newInstance("search","",videoInfo,mediaPlayer);
        pagerAdapter.addFragment(musicPlayerFragment);
        historyFragment = HistoryFragment.newInstance("search","text");
        pagerAdapter.addFragment(historyFragment);
        viewPager.setAdapter(pagerAdapter);
        mSearchPresenter = new SearchPresenter(DataManager.getInstance(),context);
        mSearchPresenter.attachView(searchFragment);

        final EditText searchText = (EditText) findViewById(R.id.edit_text_search_fragment);
        searchText.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String text = searchText.getText().toString();
                    text.trim();
                    if(!text.isEmpty()) {
                        View keyboard = AudioHandlingActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        mSearchPresenter.search_youtube_API(text);
                        viewPager.setCurrentItem(0);
                    }else{
                        Toast.makeText(getApplicationContext(),"You must enter a word to start search",Toast.LENGTH_LONG).show();

                    }
                    return true;
                }
                return false;
            }
        });

        Button searchButton = (Button) findViewById(R.id.search_button_search_fragment);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(GlobalEntities.AudioHandling_ACTIVITY,"searchButton: isclicked");
                String text = searchText.getText().toString();
                text.trim();
                if(!text.isEmpty()){
                    View keyboard = AudioHandlingActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    //NotificationCompat.Builder mBuilder;
                    //pagerAdapter.notifyDataSetChanged();

/*
                    Intent service = new Intent(AudioHandlingActivity.this,NotiService.class);
                    startService(service);*/

                    /*receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            musicPlayerFragment.handle_play();
                            *//* Log.i("helloreceived","done");
                            String status = intent.getStringExtra("player");

                            Log.i("broadcaststatus",status);
                            if(status.equals("pause")){
                                MusicPlayerFragment.mediaPlayer.pause();
                            }
                            else if(status.equals("play")){
                                MusicPlayerFragment.mediaPlayer.start();
                            }
*//*
                        }
                    };

                    AudioHandlingActivity.this.registerReceiver(receiver,new IntentFilter("play"));*/
                    mSearchPresenter.search_youtube_API(text);
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
                /*int size = sharedPreferences.getInt("size",0);
                for(int i =videoInfoArray.size();i<size;i++) {
                    String title = sharedPreferences.getString("title"+i, "");
                    Log.i("historyVideoInfo", title+i+"+"+sharedPreferences.getString("title"+i, ""));
                    String url = sharedPreferences.getString("url"+i, "");
                    String image = sharedPreferences.getString("image"+i, "");
                    info = new VideoInfo(title, "", image, url, getApplicationContext());
                    //Log.i("historyVideoInfo", histVideoInfo.toString());
                    videoInfoArray.add(info);
                }*/

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
        unregisterReceiver(receiver);
        stopService(new Intent(this,NotiService.class));
    }

    @Override
    public void onResultInteraction(String action, Item item) {
/*
        if(action.equals("play")) {
            videoInfoArrayList.add(item);
            editor.putInt("size",videoInfoArrayList.size());
            editor.commit();
            int i = videoInfoArrayList.size()-1;
                editor.putString("title"+i, videoInfoArrayList.get(i).getTitle());
                editor.putString("url"+i, videoInfoArrayList.get(i).getUrl());
                editor.putString("image"+i, videoInfoArrayList.get(i).getImage());
                editor.commit();
            Log.i("resultinteraction",videoInfoArrayList.toString());


            musicPlayerFragment.addToQueue(item);
            viewPager.setCurrentItem(1);
            musicPlayerFragment.downloadSong(item);
        }*/
    }

    @Override
    public void onMusictInteraction(String action, String message) {
        if(action.equals("notification")){

        }
    }

    @Override
    public void onHistoryInteraction(String action, VideoInfo videoInfo) {
        if(action.equals("play")) {
            viewPager.setCurrentItem(1);

            musicPlayerFragment.downloadSong(videoInfo);
        }
    }
}


