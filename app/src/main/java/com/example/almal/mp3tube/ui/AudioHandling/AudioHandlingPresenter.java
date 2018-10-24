package com.example.almal.mp3tube.ui.AudioHandling;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Response;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingContract;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by almal on 2018-10-17.
 */

public class AudioHandlingPresenter extends BasePresenter<AudioHandlingContract.View> implements AudioHandlingContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    public AudioHandlingPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(AudioHandlingContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }


    @Override
    public void signout() {
        FirebaseUser user = mAuth.getCurrentUser();
        // TODO add to database flag signed in or not
        if (user != null) {
            databaseReference.child(GlobalEntities.DATABASE_REF_USERS).child(user.getUid())
                    .child(GlobalEntities.DATABASE_REF_LOGGED_IN).setValue(GlobalEntities.FALSE_FLAG);

            mAuth.signOut();
        }
    }


}