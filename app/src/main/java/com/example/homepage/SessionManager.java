package com.example.homepage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private String SHARED_PREF_NAME = "session";
    String SESSION_UID = "uid";
    String SESSION_PASS = "pass";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Driverr driver) {
        // Save User Session
        String uid = driver.getUid();
        String pass = driver.getPass();
        editor.putString(SESSION_UID,uid);
        editor.putString(SESSION_PASS,pass);
        editor.commit();
    }

    public String getSession() {
        // return user session
        return sharedPreferences.getString(SESSION_UID, "-1");
    }

    public void removeSession() {
        editor.putString(SESSION_UID, "-1").commit();
    }
}
