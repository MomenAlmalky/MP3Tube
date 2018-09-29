package com.example.almal.mp3tube.ui.AudioHandling;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
import com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer.MusicPlayerPresenter;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchFragment;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchPresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.MediaPlayerService;
import com.example.almal.mp3tube.utilities.NotiService;
import com.example.almal.mp3tube.utilities.PagerAdapter;

import java.util.ArrayList;


public class AudioHandlingActivity extends AppCompatActivity implements SearchFragment.OnResultInteractionListener,MusicPlayerFragment.OnMusicInteractionListener,HistoryFragment.OnHistoryInteractionListener{
    //String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=momen&key={YOUR_API_KEY}";
    //String key = "AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";
    PagerAdapter pagerAdapter;
    MusicPlayerFragment musicPlayerFragment;
    SearchFragment searchFragment;
    HistoryFragment historyFragment;
    private MediaPlayerService player;
    BottomNavigationView bottomNavigationView;
    boolean serviceBound = false;



    ArrayList<VideoInfo> videoInfoArray = new ArrayList<>();
    public static Context context;

    VideoInfo videoInfo;
    boolean doubleBackToExitPressedOnce = false;


    //SearchPresenter mSearchPresenter;
    //MusicPlayerPresenter mMusicPresenter;


    public static Intent getStartIntent(Context context){
        Intent intent = new Intent(context,AudioHandlingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_handling);
        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        init();
    }


    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();

            serviceBound = true;

            Toast.makeText(AudioHandlingActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };



    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.menu_nav_search:
                    selectedFragment = searchFragment;
                    break;
                case R.id.menu_nav_song_player:
                    selectedFragment = musicPlayerFragment;
                    break;
                case R.id.menu_nav_history:
                    selectedFragment = historyFragment;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commitNow();
            return true;

        }
    };

    private void init() {
        context = getApplicationContext();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_widget);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);

        //create new search fragment instance and make it shown first.
        searchFragment = SearchFragment.newInstance("search","text");
        musicPlayerFragment = MusicPlayerFragment.newInstance("search","");
        historyFragment = HistoryFragment.newInstance("search","text");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,searchFragment).commitNow();

       /* pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(searchFragment);
        musicPlayerFragment = MusicPlayerFragment.newInstance("search","");
        pagerAdapter.addFragment(musicPlayerFragment);
        historyFragment = HistoryFragment.newInstance("search","text");
        pagerAdapter.addFragment(historyFragment);
        viewPager.setAdapter(pagerAdapter);*/

        /*mSearchPresenter = new SearchPresenter(DataManager.getInstance(),context);
        mSearchPresenter.attachView(searchFragment);*/

    /*    mMusicPresenter = new MusicPlayerPresenter(DataManager.getInstance(),context);
        mMusicPresenter.attachView(musicPlayerFragment);
*/



    }




   /* @Override
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
                viewPager.setCurrentItem(2);
                //historyFragment.getUpdated(videoInfoArray);
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


   /* @Override
    public void onBackPressed() {
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
    }*/


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }
    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            media = GlobalEntities.TEST_SONG_URL;
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }


    @Override
    public void onResultInteraction(String action, Item item) {
        if(action.equals(GlobalEntities.PLAY_TAG)){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,musicPlayerFragment).commitNow();
            musicPlayerFragment.streamSong(item);
            //mMusicPresenter.playClickedMusic(item);
        }

    }

    @Override
    public void onMusictInteraction(String action, Item item) {
        if(action.equals(GlobalEntities.PLAY_TAG)){
            Log.i(GlobalEntities.AudioHandling_ACTIVITY,"onmusicinteraction: GlobalEntities.PLAY_MUSIC_API+item.getId().getVideoId()");
            playAudio(GlobalEntities.PLAY_MUSIC_API+item.getId().getVideoId());
        }
    }

    @Override
    public void onHistoryInteraction(String action, VideoInfo videoInfo) {
        if(action.equals("play")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,musicPlayerFragment).commitNow();
        }
    }
}


