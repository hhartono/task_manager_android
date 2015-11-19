package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 9/16/15.
 * willi
 * email: willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class LoadWorkerActivity extends ListActivity{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();

    ArrayList<HashMap<String, String>> workList;

    // worker JSONArray
    JSONArray worker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadworker);

        // Hashmap for ListView
        workList = new ArrayList<>();

        new LoadWorker().execute();
    }

    public void onClickAddWorker(View view){
        Intent i = new Intent(LoadWorkerActivity.this, AddWorkerActivity.class);
        LoadWorkerActivity.this.startActivity(i);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    class LoadWorker extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadWorkerActivity.this);
            pDialog.setMessage("Loading Worker, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            // set end point
            String endPointGetAllWorker = "worker/get_all_worker";
            JSONObject json = APICon.getAllData(endPointGetAllWorker);

            // Check your log cat for JSON reponse
            Log.d("All Worker: ", json.toString());

            try{
                worker = json.getJSONArray("worker");
                Log.d("worker: ", worker.toString());
                for(int i = 0; i < worker.length(); i++){
                    JSONObject c = worker.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("nama");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);

                    workList.add(map);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String url){
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    ListAdapter adapter = new SimpleAdapter(
                            LoadWorkerActivity.this, workList,
                            R.layout.list_item_worker, new String[]{
                            "id", "name", "creation date"
                    },new int[]{R.id.pid, R.id.name, R.id.creation});
                    setListAdapter(adapter);
                }
            });
        }
    }

}
