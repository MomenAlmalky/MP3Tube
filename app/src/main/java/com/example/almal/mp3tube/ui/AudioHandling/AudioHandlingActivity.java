package com.example.almal.mp3tube.ui.AudioHandling;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.VideoInfo;
import com.example.almal.mp3tube.data.remote.SearchService;
import com.example.almal.mp3tube.ui.AudioHandling.History.HistoryFragment;
import com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer.MusicPlayerFragment;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchFragment;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.example.almal.mp3tube.utilities.HandleProgressBar;
import com.example.almal.mp3tube.utilities.MediaPlayerService;
import com.example.almal.mp3tube.utilities.NotificationGenerator;
import com.example.almal.mp3tube.utilities.PagerAdapter;

import java.util.ArrayList;


public class AudioHandlingActivity extends AppCompatActivity implements SearchFragment.OnResultInteractionListener,
        MusicPlayerFragment.OnMusicInteractionListener, HistoryFragment.OnHistoryInteractionListener,
        MusicPlayerFragment.OnHandlingSeekBarListener {
    //String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=momen&key={YOUR_API_KEY}";
    //String key = "AIzaSyAsIU3kmaiwxqnEbtI0IvvdVCxxNixbE6c";

    MusicPlayerFragment musicPlayerFragment;
    SearchFragment searchFragment;
    HistoryFragment historyFragment;

    private MediaPlayerService mediaPlayerService;
    BottomNavigationView bottomNavigationView;

    boolean serviceBound = false;
    Boolean pauseState = false;

    BroadcastReceiver broadcastReceiver;
    NotificationGenerator notificationGenerator;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager notificationManager;


    HandleProgressBar handleProgressBar = new HandleProgressBar();
    public Handler mHandler = new Handler();


    ArrayList<VideoInfo> videoInfoArray = new ArrayList<>();
    public Context context;

    VideoInfo videoInfo;


    //SearchPresenter mSearchPresenter;
    //MusicPlayerPresenter mMusicPresenter;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AudioHandlingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_handling);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            serviceBound = savedInstanceState.getBoolean("ServiceState");
            pauseState = savedInstanceState.getBoolean(GlobalEntities.PLAY_TAG);

            Log.i(GlobalEntities.AudioHandling_ACTIVITY, "service is" + String.valueOf(serviceBound));
        }

        Log.i(GlobalEntities.AudioHandling_ACTIVITY, "service is bara if " + String.valueOf(serviceBound));


        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if service is not bound then start it and bind this activity to it
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        }


        // register broadcast receiver and add actions to it
        IntentFilter intentFilter = new IntentFilter(GlobalEntities.NOTIFY_PLAY);
        intentFilter.addAction(GlobalEntities.NOTIFY_PAUSE);
        intentFilter.addAction(GlobalEntities.NOTIFY_NEXT);
        intentFilter.addAction(GlobalEntities.NOTIFY_PREVIOUS);
        intentFilter.addAction(GlobalEntities.DELETE_INTENT);
        intentFilter.addAction(GlobalEntities.NOTIFICATION_BAR);
        registerReceiver(broadcastReceiver, intentFilter);

    }


    // save the last state of service and pause state before the activty was destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(GlobalEntities.SERVICE_STATE, serviceBound);
        savedInstanceState.putBoolean(GlobalEntities.PLAY_TAG, pauseState);
        super.onSaveInstanceState(savedInstanceState);
    }

    // get the last state of service and pause state before the activty was destroyed
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean(GlobalEntities.SERVICE_STATE);
        pauseState = savedInstanceState.getBoolean(GlobalEntities.PLAY_TAG);

        Log.i(GlobalEntities.AudioHandling_ACTIVITY, "restore valuse " + String.valueOf(serviceBound) + "  pause:" + String.valueOf(pauseState));
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = binder.getService();

            serviceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
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

        //create new search fragment ,music and library fragment instances.
        searchFragment = SearchFragment.newInstance("search", "text");
        musicPlayerFragment = MusicPlayerFragment.newInstance("search", "");
        historyFragment = HistoryFragment.newInstance("search", "text");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commitNow();

        // create new instance from notification builder and manager
        notificationBuilder = new NotificationCompat.Builder(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // create new instance from notification generator and pass to it notification manager and builder
        notificationGenerator = new NotificationGenerator(getApplicationContext(), notificationBuilder, notificationManager);

        // receive actions with broadcast reveiver from notification and take action based on action tag
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleMusicPlayerStates(intent.getAction());
            }
        };

    }

    public void handleMusicPlayerStates(String action) {
        // if action received from notification was pause button and state of pause was true so pause track and change button in notification
        if (action.equals(GlobalEntities.NOTIFY_PAUSE) && pauseState) {
            startStreaming(GlobalEntities.RESUME_PLAYER_TAG, "", "", "");
            pauseState = false;
            musicPlayerFragment.resume();
            notificationGenerator.changePlayPauseButton(GlobalEntities.PLAY_TAG);
        }

        // if action received from notification was pause state of pause was false so play track and change button in notification
        else if (action.equals(GlobalEntities.NOTIFY_PAUSE) && !pauseState) {
            startStreaming(GlobalEntities.PAUSE_PLAYER_TAG, "", "", "");
            pauseState = true;
            musicPlayerFragment.pause();
            notificationGenerator.changePlayPauseButton(GlobalEntities.PAUSE_PLAYER_TAG);
        }

        // if action received from notification was next so play next track
        else if (action.equals(GlobalEntities.NOTIFY_NEXT)) {
            startStreaming(GlobalEntities.NEXT_PLAYER_TAG, "", "", "");
        }

        // if action received from notification was previous so play previous track
        else if (action.equals(GlobalEntities.NOTIFY_PREVIOUS)) {
            startStreaming(GlobalEntities.PREVIOUS_PLAYER_TAG, "", "", "");
        }

        // if the notification bar is dismissed finish this activity
        else if (action.equals(GlobalEntities.DELETE_INTENT)) {
            finish();
        }

        // if notification bar was clicked open the recent activity

        else if (action.equals(GlobalEntities.NOTIFICATION_BAR)) {

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        unregisterReceiver(broadcastReceiver);
        notificationManager.cancelAll();
        if (serviceBound) {
            // first unbind service and after stop it
            serviceBound = false;
            unbindService(serviceConnection);
            //service is active
            mediaPlayerService.stopSelf();
        }
    }


    private void startStreaming(String tag, String media, String url, String song_name) {

        media = GlobalEntities.TEST_SONG_URL;

        if (tag.equals(GlobalEntities.STREAM_PLAYER_TAG)) {
            // create custom notification when play music
            notificationGenerator.customBigNotification(song_name, url);
            updateProgressBar();
            mediaPlayerService.streamMedia(media);
        } else if (tag.equals(GlobalEntities.RESUME_PLAYER_TAG)) {
            mediaPlayerService.resumeMedia();
        } else if (tag.equals(GlobalEntities.PAUSE_PLAYER_TAG)) {

            mediaPlayerService.pauseMedia();
        } else if (tag.equals(GlobalEntities.NEXT_PLAYER_TAG)) {

        } else if (tag.equals(GlobalEntities.PREVIOUS_PLAYER_TAG)) {

        }


    }


    @Override
    public void onResultInteraction(String action, Item item) {
        if (action.equals(GlobalEntities.PLAY_TAG)) {
            bottomNavigationView.setSelectedItemId(R.id.menu_nav_song_player);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, musicPlayerFragment).commitNow();
            musicPlayerFragment.streamSong(item);
            //mMusicPresenter.playClickedMusic(item);
        }

    }

    @Override
    public void onMusictInteraction(String action, Item item) {
        if (action.equals(GlobalEntities.PLAY_TAG)) {
            Log.i(GlobalEntities.AudioHandling_ACTIVITY, "onmusicinteraction: GlobalEntities.PLAY_MUSIC_API+item.getId().getVideoId()");
            startStreaming(GlobalEntities.STREAM_PLAYER_TAG,
                    GlobalEntities.PLAY_MUSIC_API + item.getId().getVideoId(),
                    item.getSnippet().getThumbnails().getDefault().getUrl(),
                    item.getSnippet().getTitle());
        } else if (action.equals(GlobalEntities.NOTIFY_PAUSE)) {
            handleMusicPlayerStates(action);
        } else if (action.equals(GlobalEntities.NOTIFY_NEXT)) {
            handleMusicPlayerStates(action);
        } else if (action.equals(GlobalEntities.NOTIFY_PREVIOUS)) {
            handleMusicPlayerStates(action);
        }
    }

    @Override
    public void onHistoryInteraction(String action, VideoInfo videoInfo) {
        if (action.equals("play")) {

            bottomNavigationView.setSelectedItemId(R.id.menu_nav_history);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, musicPlayerFragment).commitNow();
        }
    }


    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayerService.getTotalMediaDuration();
            long currentDuration = mediaPlayerService.getCurrentMediaPosition();

            String total_duration = handleProgressBar.milliSecondsToTimer(totalDuration);
            String current_duration= handleProgressBar.milliSecondsToTimer(currentDuration);
            int percent = handleProgressBar.getProgressPercentage(currentDuration, totalDuration);
            musicPlayerFragment.handleSeekBar(total_duration,current_duration,percent);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void OnHandlingSeekBarListener(String action,int progress) {
        if (action.equals(GlobalEntities.ON_START_TRACKING)){
            mHandler.removeCallbacks(mUpdateTimeTask);
        }else{
            mHandler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mediaPlayerService.getTotalMediaDuration();
            int currentPosition = handleProgressBar.progressToTimer(progress, totalDuration);

            // forward or backward to certain seconds
            mediaPlayerService.seekTo(currentPosition);

            // update timer progress again
            updateProgressBar();

        }

    }
}


