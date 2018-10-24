package com.example.almal.mp3tube.ui.OnBoarding.SignIn;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.ui.OnBoarding.SignUp.SignUpContract;
import com.example.almal.mp3tube.ui.base.BasePresenter;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Subscription;

/**
 * Created by almal on 2017-06-24.
 */

public class SignInPresenter extends BasePresenter<SignInContract.View> implements SignInContract.Presenter {

    private Subscription mSubscriptions;
    private DataManager mDataManager;
    private Context mContext;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;


    public SignInPresenter(DataManager mDataManager, Context mContext) {
        this.mSubscriptions = mSubscriptions;
        this.mDataManager = mDataManager;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void attachView(SignInContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscriptions != null) mSubscriptions.unsubscribe();
    }


    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            databaseReference.child(GlobalEntities.DATABASE_REF_USERS).child(user.getUid())
                                    .child(GlobalEntities.DATABASE_REF_LOGGED_IN).setValue(GlobalEntities.TRUE_FLAG);

                            getBaseView().signInSuccess(user);
                        } else {
                            Log.w(GlobalEntities.SIGNIN_ACTIVITY, "createUserWithEmail:failure",task.getException());
                            getBaseView().signInFailed(task.getException());
                        }
                    }
                });
    }



}
