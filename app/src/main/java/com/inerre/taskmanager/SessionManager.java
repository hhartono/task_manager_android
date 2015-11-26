package com.inerre.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by root on 9/18/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class SessionManager {

    // SharedPreferences reference
    SharedPreferences SP;

    // Editor reference for SharedPreferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String SP_NAME = "TaskManagerUser";

    // All Shared Preferences keys
    private static final String IS_LOGIN = "isLogin";

    // idworker
    public static final String KEY_IDWORKER = "idWorker";

    // iduser
    public static final String KEY_IDUSER = "idUser";

    // name
    public static final String KEY_NAME = "name";

    // status user
    public static final String KEY_STATUS = "status";

    // password user
    public static final String KEY_PASSWORD = "password";

    // constructor
    public SessionManager(Context context){
        this._context = context;
        SP = _context.getSharedPreferences(SP_NAME, PRIVATE_MODE);
        editor = SP.edit();
    }

    /*
     * method CreateLoginSession
     * create login session
     */
    public void CreateLoginSession(String idworker, String iduser, String name, String status, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_IDWORKER, idworker);
        editor.putString(KEY_IDUSER, iduser);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_STATUS, status);
        editor.putString(KEY_PASSWORD, password);
        // commit changes
        editor.commit();
    }

    /*
     * method checkLogin
     * check login status
     */
    public void checkLogin(){
        if(this.isLoggedIn()){
            // buat flow agar melakukan pengecekan terhadap status user, admin atau worker
            String sts = SP.getString(KEY_STATUS, "worker");
            if(sts.equals("worker")){
                // login sebagai worker
                Intent i = new Intent(_context, LoadTaskbyWorkerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // add new flag to start new activity
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
            }else if(sts.equals("admin")){
                // login sebagai admin
                Intent i = new Intent(_context, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);
            }
        }
    }

    /*
     * method getUserData
     * return user data
     */
    public HashMap<String, String> getUserData(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_IDWORKER, SP.getString(KEY_IDWORKER, null));
        user.put(KEY_IDUSER, SP.getString(KEY_IDUSER, null));
        user.put(KEY_NAME, SP.getString(KEY_NAME, null));
        user.put(KEY_STATUS, SP.getString(KEY_STATUS, null));
        return user;
    }

    /*
     * method Logout
     * clear all sharedpreferences data
     */
    public void Logout(){
        // clearing all data from SharedPreferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        // closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // add new flag to start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // starting new activity
        _context.startActivity(i);
    }

    /*
     * method isLoggedIn
     * check status user, return boolean
     * default value: false
     */
    private boolean isLoggedIn() {
        return SP.getBoolean(IS_LOGIN, false);
    }

}
