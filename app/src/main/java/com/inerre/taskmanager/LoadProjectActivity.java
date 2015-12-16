package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
public class LoadProjectActivity extends ListActivity{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // products JSONArray
    JSONArray projects = null;

    ArrayList<HashMap<String, String>> projectList;
    SessionManager session;
    HashMap<String, String> hmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadproject);

        session = new SessionManager(getApplicationContext());
        hmUser = session.getUserData();

        // Hashmap for ListView
        projectList = new ArrayList<>();

        new LoadProject().execute();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                String nameProject = ((TextView) view.findViewById(R.id.name)).getText().toString();

                Intent i = new Intent(LoadProjectActivity.this, LoadTaskbyProjectActivity.class);
                i.putExtra("idproject", pid);
                i.putExtra("nameproject", nameProject);

                Toast.makeText(getApplicationContext(), "id project: " + pid , Toast.LENGTH_LONG).show();

                LoadProjectActivity.this.startActivity(i);
            }
        });
    }

    public void onClickAddProject(View view){
        Intent i = new Intent(LoadProjectActivity.this, AddProjectActivity.class);
        LoadProjectActivity.this.startActivity(i);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /*
     * inner class
     * LoadProject; load all project
     */
    class LoadProject extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadProjectActivity.this);
            pDialog.setMessage("Loading Projects, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            // set end point
            String endPointGetAllProjects = "project/get_all_project";
            JSONObject json = APICon.getAllData(endPointGetAllProjects);

            // Check your log cat for JSON reponse
            Log.d("All Projects: ", json.toString());

            try{
                projects = json.getJSONArray("project");
                Log.d("project: ", projects.toString());
                for(int i = 0; i < projects.length(); i++){
                    JSONObject c = projects.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("nama_project");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);

                    projectList.add(map);
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
                            LoadProjectActivity.this, projectList,
                            R.layout.list_item_project, new String[]{
                            "id", "name", "creation date"
                    },new int[]{R.id.pid, R.id.name, R.id.creation});
                    setListAdapter(adapter);
                }
            });
        }

    }

}
