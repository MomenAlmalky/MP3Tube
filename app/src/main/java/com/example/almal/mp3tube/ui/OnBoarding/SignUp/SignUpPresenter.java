package com.example.almal.mp3tube.ui.OnBoarding.SignUp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.FirebaseUsers;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Response;
import com.example.almal.mp3tube.ui.AudioHandling.Search.SearchContract;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by almal on 2017-06-24.
 */

public class SignUpPresenter extends BasePresenter<SignUpContract.View> implements SignUpContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRefrence;

    public SignUpPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseRefrence = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(SignUpContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }


    @Override
    public void signUp(final String email, final String password, final String name, final boolean logged_in) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(GlobalEntities.SIGNUP_ACTIVITY,"trial");

                        if (task.isSuccessful()) {
                            Log.i(GlobalEntities.SIGNUP_ACTIVITY,email+"success");

                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseUsers firebaseUser = new FirebaseUsers(email,password,name,logged_in);

                            databaseRefrence.child(GlobalEntities.DATABASE_REF_USERS).child(user.getUid()).setValue(firebaseUser);
                            databaseRefrence.child(GlobalEntities.DATABASE_REF_LIKES).child(user.getUid()).setValue("");
                            databaseRefrence.child(GlobalEntities.DATABASE_REF_PLAYLISTS).child(user.getUid()).setValue("");
                            databaseRefrence.child(GlobalEntities.DATABASE_REF_History).child(user.getUid()).setValue("");


                            getBaseView().signUpSuccess(user);

                        } else {

                            getBaseView().signUpFailed(task.getException());
                        }
                    }
                });
    }



}
