package com.example.almal.mp3tube.ui.OnBoarding.SignIn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import javax.microedition.khronos.opengles.GL;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    SignInPresenter signInPresenter;
    EditText email_et,password_et;
    Button signin_btn;
    String email_str,password_str;
    Toolbar signin_toolbar;

    public static Intent getStartIntent(Context context){
        return new Intent(context,SignInActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
    }

    public void init(){
        signInPresenter = new SignInPresenter(DataManager.getInstance(),this);
        signInPresenter.attachView(this);

        email_et = (EditText) findViewById(R.id.signin_email_et);
        password_et = (EditText) findViewById(R.id.signin_password_et);
        signin_btn = (Button) findViewById(R.id.signin_btn);

        signin_toolbar = (Toolbar) findViewById(R.id.signin_toolbar);
        setSupportActionBar(signin_toolbar);
        signin_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email_et.getText().toString();
                password_str = password_et.getText().toString();

                if(email_str.isEmpty()){
                    email_et.setError(GlobalEntities.EMAIL_MALFORMED + "Email must not be empty");
                    email_et.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()){
                    email_et.setError(GlobalEntities.EMAIL_MALFORMED );
                    email_et.requestFocus();
                }
                else if(password_str.isEmpty()){
                    password_et.setError("Password must not be empty");
                    password_et.requestFocus();
                }
                else if(password_str.length()<6){
                    password_et.setError(GlobalEntities.WEAK_PASSWORD );
                    password_et.requestFocus();
                }
                else{
                    signInPresenter.signIn(email_str,password_str);
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        signInPresenter.detachView();
    }

    @Override
    public void signInSuccess(FirebaseUser firebaseUser) {
        Intent audioHandlingIntent = AudioHandlingActivity. getStartIntent(this);
        audioHandlingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(audioHandlingIntent);
        finish();
    }

    @Override
    public void signInFailed(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // thrown if there already exists an account with the given email address
            password_et.setError(GlobalEntities.PASSWORD_NOT_MATCH);
            password_et.requestFocus();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // thrown if the email address is malformed
            email_et.setError(GlobalEntities.EMAIL_MALFORMED);
            email_et.requestFocus();
        }else {
            Toast.makeText(this,"Can't SignIn " + exception.getMessage() , Toast.LENGTH_LONG).show();
        }
    }
}
