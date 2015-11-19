package com.inerre.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

/**
 * Created by root on 10/21/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class DashboardActivity extends Activity {

    SessionManager session;
    String idWorker;
    HashMap<String, String> hmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        session = new SessionManager(getApplicationContext());
        hmUser = session.getUserData();
    }

    /*
     * click project button
     */
    public void onClickProject(View view){
        Intent i = new Intent(DashboardActivity.this, LoadProjectActivity.class);
        /*i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        DashboardActivity.this.startActivity(i);
    }

    /*
     * click worker button
     */
    public void onClickWorker(View view){
        Intent i = new Intent(DashboardActivity.this, LoadWorkerActivity.class);
        DashboardActivity.this.startActivity(i);
    }

    public void onClickAddTask(View view){
        Intent i = new Intent(DashboardActivity.this, AddTaskActivity.class);
        DashboardActivity.this.startActivity(i);
    }

    /*
     * click logout button
     */
    public void onClickLogout(View view){
        session.Logout();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
