package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 9/18/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class LoadTaskbyCreationDateActivity extends ListActivity{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // tasks JSONArray
    JSONArray tasks = null;

    ArrayList<HashMap<String, String>> taskList;
    SessionManager session;
    String idWorker;
    HashMap<String, String> hmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadtask);

        // Hashmap for ListView
        taskList = new ArrayList<HashMap<String, String>>();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idWorker = extras.getString("idworker");
        }

        session = new SessionManager(getApplicationContext());
        hmUser = session.getUserData();

        String namaUser = hmUser.get("name");
        String statusUser = hmUser.get("status");

        Toast.makeText(getApplicationContext(), namaUser, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), statusUser, Toast.LENGTH_LONG).show();

        new LoadTaskCreationDate().execute();
    }

    class LoadTaskCreationDate extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadTaskbyCreationDateActivity.this);
            pDialog.setMessage("Loading Task, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String creation_date = null;

            String endPointGetAllTugas = "tugas/get_all_tugas_by_creationdate";
            JSONObject json = APICon.getDatabyPOSTParams(endPointGetAllTugas, creation_date, "tanggal");

            // Check your log cat for JSON reponse
            Log.d("All Task: ", json.toString());

            try{
                tasks = json.getJSONArray("tugas");
                Log.d("task: ", tasks.toString());
                for(int i = 0; i < tasks.length(); i++){
                    JSONObject c = tasks.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("nama_project");
                    String deskripsi = c.getString("deskripsi");
                    String keterangan = c.getString("keterangan");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("deskripsi", deskripsi);
                    map.put("keterangan", keterangan);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);

                    taskList.add(map);
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String url){
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            LoadTaskbyCreationDateActivity.this, taskList,
                            R.layout.list_item_task, new String[]{
                            "id", "name", "creation date"
                    }, new int[]{R.id.pid, R.id.name, R.id.creation});
                    setListAdapter(adapter);
                }
            });
            Toast.makeText(getApplicationContext(), "id worker yang login: " + idWorker, Toast.LENGTH_LONG).show();
        }


    }

}
