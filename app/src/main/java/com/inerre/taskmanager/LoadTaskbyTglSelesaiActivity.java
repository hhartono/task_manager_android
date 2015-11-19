package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 9/22/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class LoadTaskbyTglSelesaiActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();

    ArrayList<HashMap<String, String>> taskList;

    // tasks JSONArray
    JSONArray tasks = null;

    String idWorker;

    SessionManager session;
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

        new LoadTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class LoadTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadTaskbyTglSelesaiActivity.this);
            pDialog.setMessage("Loading Task, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            // set end point
            String endPointGetAllTugas = "tugas/get_all_tugas_by_tglselesai_hariini";
            JSONObject json = APICon.getAllData(endPointGetAllTugas);

            // Check your log cat for JSON reponse
            Log.d("All Task: ", json.toString());

            try{
                tasks = json.getJSONArray("tugas");
                Log.d("task: ", tasks.toString());
                for(int i = 0; i < tasks.length(); i++){
                    JSONObject c = tasks.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("project");
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
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String url){
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            LoadTaskbyTglSelesaiActivity.this, taskList,
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
