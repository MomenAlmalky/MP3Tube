package com.example.almal.mp3tube.ui.OnBoarding.SignUp;

import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.ui.base.BaseView;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by almal on 2017-06-24.
 */

public interface SignUpContract {
    public interface View extends BaseView {
        void signUpSuccess(FirebaseUser firebaseUser);
        void signUpFailed(Exception exception);
    }

    public interface Presenter {
        void signUp(String email, String password, String name,boolean logged_in);
    }


}
