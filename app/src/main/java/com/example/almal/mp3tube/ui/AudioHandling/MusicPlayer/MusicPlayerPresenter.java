package com.example.almal.mp3tube.ui.AudioHandling.MusicPlayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.data.model.Item;
import com.example.almal.mp3tube.data.model.Response;
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
 * Created by almal on 2017-06-24.
 */

public class MusicPlayerPresenter extends BasePresenter<MusicPlayerContract.View> implements MusicPlayerContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    boolean liked = false;


    public MusicPlayerPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(MusicPlayerContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }



    public void isLiked(final FirebaseTracks firebaseTrack, final Boolean likeBtn) {

        FirebaseUser user = mAuth.getCurrentUser();
        // TODO add to database flag signed in or not
        if (user != null) {
            databaseReference.child(GlobalEntities.DATABASE_REF_LIKES).child(user.getUid())
                    .orderByChild(GlobalEntities.DATABASE_REF_TRACK_ID)
                    .equalTo(firebaseTrack.getTrack_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(firebaseTrack.getTrack_id())) {
                        getBaseView().isLikedCallBack(GlobalEntities.TRUE_VALUE, likeBtn);

                    } else {
                        getBaseView().isLikedCallBack(GlobalEntities.FALSE_VALUE, likeBtn);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    getBaseView().isLikedCallBack(GlobalEntities.FALSE_VALUE, likeBtn);


                }
            });

        } else {
            getBaseView().isLikedCallBack(GlobalEntities.FALSE_VALUE, likeBtn);
        }

    }

    public boolean likeTrack(FirebaseTracks firebaseTrack) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            databaseReference.child(GlobalEntities.DATABASE_REF_LIKES).child(user.getUid()).child(firebaseTrack.getTrack_id())
                    .setValue(firebaseTrack);

            return true;
        } else {
            return false;
        }
    }

    public boolean unLikeTrack(FirebaseTracks firebaseTrack) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            databaseReference.child(GlobalEntities.DATABASE_REF_LIKES).child(user.getUid())
                    .child(firebaseTrack.getTrack_id()).removeValue();

            return true;
        } else {
            return false;
        }
    }


}
