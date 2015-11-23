package com.inerre.taskmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/21/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class AddProjectActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();

    EditText namaProject;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);

        namaProject = (EditText) findViewById(R.id.editTextNamaProject);

        // Hashmap for ListView
        //projectList = new ArrayList<>();
    }

    /*
     * method onClickSaveProject
     * save nama project
     */
    public void onClickSaveProject(View view){
        String n = namaProject.getText().toString();
        if(n.trim().length() > 0){
            new AddProject().execute();
            namaProject.setText("");
        }else{
            Toast.makeText(getApplicationContext(), "Nama Project tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * inner class
     * Class: AddProject
     */
    class AddProject extends AsyncTask<String, String, String>{

        int status;
        String message;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddProjectActivity.this);
            pDialog.setMessage("Saving project ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String namaproject = namaProject.getText().toString();
            Map<String, String> params = new HashMap<>();
            params.put("nama_project",namaproject);

            String endPointSetProject = "project/set_project";
            JSONObject json = APICon.setDataMultiParams(endPointSetProject, params);

            try{
                status = json.getInt("status");
                message = json.getString("message");
                /*projectList.clear();
                updatingData();*/
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
