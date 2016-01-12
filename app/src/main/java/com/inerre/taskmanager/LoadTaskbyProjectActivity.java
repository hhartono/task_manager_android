package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Created by root on 11/30/15.
 * willi
 * email: willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class LoadTaskbyProjectActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // tasks JSONArray
    JSONArray tasks = null;

    String idProject;
    String nameProject;
    //arraylist
    ArrayList<HashMap<String, String>> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadtaskbyproject);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            idProject = extras.getString("idproject");
            nameProject = extras.getString("nameproject");
        }

        TextView tvTitle = (TextView)findViewById(R.id.title_alltask);
        tvTitle.setText("Task (" + nameProject + ")");

        // Hashmap for ListView
        taskList = new ArrayList<>();

        new LoadTask().execute();

        ListView lv = getListView();

        /*
         * click / press listview item
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                Toast.makeText(getApplicationContext(), "item yang pilih nomor: " + pid, Toast.LENGTH_LONG).show();
            }
        });

    }

    /*
     * inner class
     * Class: LoadTask
     * LoadTask for load task by project id
     */
    class LoadTask extends AsyncTask<String, String, String>{

        private ArrayList<String> listOfWorker = new ArrayList<>();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadTaskbyProjectActivity.this);
            pDialog.setMessage("Loading Task, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String endPointGetTugasByProject = "tugas/get_all_tugas_by_project_id";
            JSONObject json = APICon.getDatabyPOSTParams(endPointGetTugasByProject, idProject, "project_id");

            // Check your log cat for JSON reponse
            Log.d("All Task: ", json.toString());

            try{

                tasks = json.getJSONArray("tugas");
                Log.d("task: ", tasks.toString());

                for(int i = 0; i < tasks.length(); i++){
                    JSONObject c = tasks.getJSONObject(i);

//                    String idproject = c.getString("project_id");
//                    String tugas_id = c.getString("tugas_id");

                    String id = c.getString("id");
                    //String name = c.getString("project");
                    String deskripsi = c.getString("deskripsi");
                    String keterangan = c.getString("keterangan");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");
                    String tglselesai = c.getString("tanggal_selesai");
                    String worker = c.getString("worker");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    //map.put("name", name);
                    map.put("deskripsi", deskripsi);
                    map.put("keterangan", keterangan);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);
                    map.put("tgl selesai", tglselesai);
                    map.put("worker", "worker : "+worker);

                    taskList.add(map);
                }

            }catch(JSONException e){
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
                    /*SimpleAdapter adapter = new SimpleAdapter(
                            LoadTaskbyProjectActivity.this, taskList,
                            R.layout.list_item_taskbyproject,
                            new String[]{"id", "deskripsi", "name", "keterangan", "tgl selesai", "worker"},
                            new int[]{R.id.pid, R.id.deskripsi, R.id.name, R.id.keterangan, R.id.tglselesai, R.id.namaWorker}
                    );*/

                    TaskAdminAdapter adapter = new TaskAdminAdapter(
                            LoadTaskbyProjectActivity.this, taskList,
                            R.layout.list_item_taskbyproject,
                            new String[]{"id", "deskripsi", "name", "keterangan", "tgl selesai", "worker"},
                            new int[]{R.id.pid, R.id.deskripsi, R.id.name, R.id.keterangan, R.id.tglselesai, R.id.namaWorker}
                    );
                    setListAdapter(adapter);
                }
            });
        }

    }

    class TaskAdminAdapter extends SimpleAdapter {
        private ArrayList<HashMap<String, String>> al;

        public TaskAdminAdapter(Context context, ArrayList<HashMap<String, String>> items, int resource, String[] from, int[] to){
            super(context, items, resource, from, to);
            this.al = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position, convertView, parent);
            HashMap<String, String> task = al.get(position);
            String ts = task.get("tgl selesai");

            if(ts.equals("0000-00-00")){
                view.setBackgroundColor(0xffffffff);
            }else{
                view.setBackgroundColor(0x30ff2020);
            }
            return view;
        }
    }

}
