package com.example.almal.mp3tube.ui.Splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.remote.SearchService;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    int splashTimeout = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SearchService service = SearchService.Creator.getService();
        DataManager dataManager = DataManager.getInstance(this, service);

        Timer splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(AudioHandlingActivity.getStartIntent(getApplicationContext()));
                finish();
            }
        },splashTimeout);
    }
}
