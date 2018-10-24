package com.example.almal.mp3tube.ui.AudioHandling.Profile.likes;

import android.content.Context;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.data.model.FirebaseTracks;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by almal on 2017-06-24.
 */

public class LikePresenter extends BasePresenter<LikeContract.View> implements LikeContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    List<FirebaseTracks> firebaseTracksList;


    public LikePresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(LikeContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }


    @Override
    public void getLikes() {
        FirebaseUser user = mAuth.getCurrentUser();
        // TODO add to database flag signed in or not
        if (user != null) {

            Log.i("likesFragment", "ok");
            databaseReference.child(GlobalEntities.DATABASE_REF_LIKES).child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            firebaseTracksList = new ArrayList<FirebaseTracks>();
                            Log.i("likesfragmetn", String.valueOf(dataSnapshot.getChildrenCount()));
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                if (postSnapshot.exists()) {
                                    Log.i("likesfragmetn", String.valueOf(postSnapshot.getValue().toString()));

                                    firebaseTracksList.add(postSnapshot.getValue(FirebaseTracks.class));


                                } else {

                                }
                            }
                            if (!firebaseTracksList.isEmpty()) {
                                getBaseView().showLikes(firebaseTracksList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        } else {
        }
    }
}
