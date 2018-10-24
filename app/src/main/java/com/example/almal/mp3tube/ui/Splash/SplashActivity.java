package com.example.almal.mp3tube.ui.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.remote.SearchService;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.ui.OnBoarding.SignIn.SignInActivity;
import com.example.almal.mp3tube.ui.OnBoarding.SignUp.SignUpActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    int splashTimeout = 2000;
    SplashPresenter splashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SearchService service = SearchService.Creator.getService();
        DataManager dataManager = DataManager.getInstance(this, service);

        splashPresenter = new SplashPresenter(DataManager.getInstance(),this);
        splashPresenter.attachView(this);

        Timer splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               splashPresenter.isSigned();
            }
        },splashTimeout);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        splashPresenter.detachView();
    }

    @Override
    public void showSignUp() {
        Intent signupIntent = SignUpActivity.getStartIntent(this);
        signupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signupIntent);
        finish();
    }

    @Override
    public void showSignIn() {

        Intent signinIntent = SignInActivity.getStartIntent(this);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signinIntent);
        finish();
    }

    @Override
    public void showAudioHandling() {
        Intent audioHandlingIntent = AudioHandlingActivity.getStartIntent(this);
        audioHandlingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(audioHandlingIntent);
        finish();
    }


}
