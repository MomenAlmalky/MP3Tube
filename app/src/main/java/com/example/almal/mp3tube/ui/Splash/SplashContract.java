package com.example.almal.mp3tube.ui.Splash;

import com.example.almal.mp3tube.ui.base.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by almal on 2017-06-24.
 */

public interface SplashContract {
    public interface View extends BaseView {
        void showSignUp();
        void showSignIn();
        void showAudioHandling();
    }

    public interface Presenter {
        void isSigned();
    }


}
