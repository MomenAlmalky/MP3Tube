package com.example.almal.mp3tube.ui.OnBoarding.SignIn;

import com.example.almal.mp3tube.ui.base.BaseView;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by almal on 2017-06-24.
 */

public interface SignInContract {
    public interface View extends BaseView {
        void signInSuccess(FirebaseUser firebaseUser);
        void signInFailed(Exception exception);
    }

    public interface Presenter {
        void signIn(String email, String password);
    }


}
