package com.example.almal.mp3tube.data.model;

import com.example.almal.mp3tube.utilities.GlobalEntities;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by almal on 2018-10-10.
 */
@IgnoreExtraProperties
public class FirebaseUsers {

    public String user_name;
    public String email;
    public String password;
    public boolean logged_in;

    public FirebaseUsers(){

    }


    public FirebaseUsers(String email,String password,String user_name,boolean logged_in){

        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.logged_in = logged_in;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(GlobalEntities.DATABASE_REF_USERNAME, user_name);
        result.put(GlobalEntities.DATABASE_REF_EMAIL, email);
        result.put(GlobalEntities.DATABASE_REF_PASSWORD, password);
        result.put(GlobalEntities.DATABASE_REF_LOGGED_IN,logged_in);

        return result;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }
}
