package com.example.almal.mp3tube.ui.OnBoarding.SignUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almal.mp3tube.R;
import com.example.almal.mp3tube.data.DataManager;
import com.example.almal.mp3tube.ui.AudioHandling.AudioHandlingActivity;
import com.example.almal.mp3tube.ui.OnBoarding.SignIn.SignInActivity;
import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    SignUpPresenter signUpPresenter;
    EditText email_et,password_et,username_et;
    TextView signin_tv;
    Button signup_btn;
    String email_str,password_str,username_str;
    boolean logged_in = false;
    Toolbar toolbar;

    public static Intent getStartIntent(Context context){
        return new Intent(context,SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    public void init(){
        signUpPresenter = new SignUpPresenter(DataManager.getInstance(),this);
        signUpPresenter.attachView(this);

        email_et = (EditText) findViewById(R.id.signup_email_et);
        password_et = (EditText) findViewById(R.id.signup_password_et);
        username_et = (EditText) findViewById(R.id.signup_user_name_et);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        signin_tv = (TextView) findViewById(R.id.signup_signin_tv);
        toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signin_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SignInActivity.getStartIntent(SignUpActivity.this));
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email_et.getText().toString();
                password_str = password_et.getText().toString();
                username_str = username_et.getText().toString();

                email_str = email_et.getText().toString();
                password_str = password_et.getText().toString();

                if(email_str.isEmpty()){
                    email_et.setError(GlobalEntities.EMAIL_MALFORMED + "Email must not be empty");
                    email_et.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches()){
                    Log.i(GlobalEntities.SIGNUP_ACTIVITY+"malform",email_str);
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
                else if(username_str.isEmpty()){
                    username_et.setError("username must not be empty");
                    username_et.requestFocus();
                }
                else if(username_str.length()<3){
                    username_et.setError("username must be at least 3 characters");
                    username_et.requestFocus();
                }
                else{
                    Log.i(GlobalEntities.SIGNUP_ACTIVITY,email_str+password_str);

                    signUpPresenter.signUp(email_str,password_str,username_str,logged_in);
                }
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        signUpPresenter.detachView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void signUpSuccess(FirebaseUser firebaseUser) {
        Intent signinIntent = SignInActivity.getStartIntent(this);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signinIntent);
    }

    @Override
    public void signUpFailed(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            // thrown if there already exists an account with the given email address
            email_et.setError(GlobalEntities.EMAIL_EXISTS);
            email_et.requestFocus();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            // thrown if the email address is malformed
            email_et.setError(GlobalEntities.EMAIL_MALFORMED);
            email_et.requestFocus();
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            // thrown if the email address is malformed
            email_et.setError(GlobalEntities.EMAIL_MALFORMED);
            email_et.requestFocus();
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            // thrown if the password is not strong enough
            password_et.setError(GlobalEntities.WEAK_PASSWORD);
            password_et.requestFocus();
        }else {
            Toast.makeText(this, "Can't SignUp " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
