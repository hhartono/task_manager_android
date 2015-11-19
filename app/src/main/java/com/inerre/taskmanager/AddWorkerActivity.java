package com.inerre.taskmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/21/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class AddWorkerActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();

    EditText namaWorker;
    EditText passwordWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addworker);

        namaWorker = (EditText) findViewById(R.id.editTextNamaWorker);
        passwordWorker = (EditText) findViewById(R.id.editTextPassWorker);

    }

    /*
     * method onClickSaveWorker
     * save worker
     */
    public void onClickSaveWorker(View view){
        String n = namaWorker.getText().toString();
        String p = passwordWorker.getText().toString();
        if(n.trim().length() > 0 && p.trim().length() > 0){
            new AddWorker().execute();
            namaWorker.setText("");
            passwordWorker.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Nama worker dan atau password tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * inner class add worker
     */
    class AddWorker extends AsyncTask<String, String, String>{
        int status;
        String message;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddWorkerActivity.this);
            pDialog.setMessage("Saving worker ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String name = namaWorker.getText().toString();
            String pass = passwordWorker.getText().toString();

            Map<String, String> params = new HashMap<>();
            params.put("nama", name);
            params.put("password", pass);
            String endPointSetProject = "worker/set_worker";
            JSONObject json = APICon.setDataMultiParams(endPointSetProject, params);

            try{
                status = json.getInt("status");
                message = json.getString("message");
            } catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "status: " + status + " message: " + message, Toast.LENGTH_LONG).show();
        }



    }

}
