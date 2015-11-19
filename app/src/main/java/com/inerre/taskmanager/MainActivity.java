package com.inerre.taskmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // user JSONArray
    // JSONArray user = null;
    EditText etName;
    EditText etPassword;
    String resultnull = null;
    private int success;

    // User Session
    SessionManager session;

    // HashMap user
    HashMap<String, String> hmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // session manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        hmUser = session.getUserData();

        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        session.checkLogin();

    }

    /*
     * method onClickLogin
     */
    public void onClickLogin(View view){
        String n = etName.getText().toString();
        String p = etPassword.getText().toString();
        if(n.trim().length() > 0 && p.trim().length() > 0){
            new Login(n, p).execute();
        }else{
            Toast.makeText(getApplicationContext(), "Nama dan Password tidak boleh kosong", Toast.LENGTH_LONG).show();
        }

    }

    /*
     * inner class
     * Class: Login
     * extends AsyncTask to send user input login
     */

    private class Login extends AsyncTask<String, String, String> {
        String uname;
        String password;

        public Login(String username, String password){
            this.uname = username;
            this.password = password;
        }

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login Process, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String statusWorker = "worker";
            // String statusAdmin = "admin";
            String noResult;
            String endPointLogin = "user/user_in";

            JSONObject json = APICon.checkUser(endPointLogin, uname, password);
            //Log.d("username: ", etName.getText().toString());
            Log.d("result user: ", json.toString());

            try{

                success = json.getInt("status");
                if(success == 1){
                    String u = json.getString("user");
                    JSONObject jUser = new JSONObject(u);
                    String iduser = jUser.getString("iduser");
                    String nameUser = json.getString("nama");
//                    String idworker = jUser.getString("idworker");
                    String statusUser = jUser.getString("status");

                    session.CreateLoginSession(iduser, iduser, nameUser, statusUser, password);
                    Log.d("status user :", statusUser);

                    if(statusUser.equals(statusWorker)){
                        // if user is worker
                        Intent i = new Intent(MainActivity.this, LoadTaskbyWorkerActivity.class);
                        i.putExtra("idworker", iduser);
                        Log.d("id worker :", iduser);
                        startActivity(i);
                        finish();
                    }else{
                        // if user is admin
                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }

                }else{
                    noResult = json.getString("message");
                    resultnull = noResult;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){
            pDialog.dismiss();
            if(success == 0){
                Toast.makeText(getApplicationContext(), resultnull, Toast.LENGTH_LONG).show();
            }
        }
    }
}
