package com.example.almal.mp3tube.ui.Splash;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.ui.OnBoarding.SignUp.SignUpContract;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rx.Subscription;

/**
 * Created by almal on 2017-06-24.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    Boolean logged_in = false;


    public SplashPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(SplashContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }


    @Override
    public void isSigned() {
        FirebaseUser user = mAuth.getCurrentUser();
        // TODO add to database flag signed in or not
        if (user != null) {
            databaseReference.child(GlobalEntities.DATABASE_REF_USERS).child(user.getUid())
                    .child(GlobalEntities.DATABASE_REF_LOGGED_IN).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String data = String.valueOf(dataSnapshot.getValue());
                        logged_in = Boolean.parseBoolean(data);
                        if (!logged_in) {
                            getBaseView().showSignIn();
                        } else {
                            getBaseView().showAudioHandling();
                        }
                    }else{
                        getBaseView().showSignUp();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    getBaseView().showSignUp();
                }
            });



        } else {
            getBaseView().showSignUp();
        }
    }
}
