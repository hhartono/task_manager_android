package com.inerre.taskmanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
 * Created by root on 9/18/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class LoadTaskbyWorkerActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // tasks JSONArray
    JSONArray tasks = null;

    //arraylist
    ArrayList<HashMap<String, String>> taskList;
    SessionManager session;
    String idWorker;
    HashMap<String, String> hmUser;
//    String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadtaskbyworker);

        // Hashmap for ListView
        taskList = new ArrayList<>();

//        Bundle extras = getIntent().getExtras();
        /*if(extras != null){
            idWorker = extras.getString("idworker");
        }*/

        session = new SessionManager(getApplicationContext());
        hmUser = session.getUserData();

        String namaUser = hmUser.get("name");
        idWorker = hmUser.get("idWorker");

//        String statusUser = hmUser.get("status");

        TextView tvTitle = (TextView)findViewById(R.id.title_alltask);
        tvTitle.setText("All Task by " + namaUser);

        new LoadTask().execute();

        ListView lv = getListView();

        /*
         * click / press listview item
         */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                Toast.makeText(getApplicationContext(), "item yang pilih nomor: "+pid, Toast.LENGTH_LONG).show();
//                new UpdateStatusTask(pid).execute();
            }
        });

        /*
         * long press listview item
         */
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                Toast.makeText(getApplicationContext(), "item yang pilih nomor: "+pid, Toast.LENGTH_LONG).show();
                new UpdateStatusTask(pid).execute();
                return false;
            }
        });
    }

    public void onClickLogout(View view){
        session.Logout();
        finish();
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    /*
     * inner class
     * LoadTask for load task by worker id
     */
    class LoadTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadTaskbyWorkerActivity.this);
            pDialog.setMessage("Loading Task, please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String endPointGetAllTugas = "tugas/get_all_tugas_by_worker_id";
            JSONObject json = APICon.getDatabyURLParams(endPointGetAllTugas, idWorker);

            // Check your log cat for JSON reponse
            Log.d("All Task: ", json.toString());

            try{
                tasks = json.getJSONArray("tugas");
                Log.d("task: ", tasks.toString());
                for(int i = 0; i < tasks.length(); i++){
                    JSONObject c = tasks.getJSONObject(i);
                    String id = c.getString("task_id");
                    String name = c.getString("project");
                    String deskripsi = c.getString("deskripsi");
                    String keterangan = c.getString("keterangan");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");
                    String tglselesai = c.getString("tanggal_selesai");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("deskripsi", deskripsi);
                    map.put("keterangan", keterangan);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);
                    map.put("tgl selesai", tglselesai);

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
            runOnUiThread(new Runnable()    {
                @Override
                public void run() {
//                    ListAdapter adapter = new SimpleAdapter(
//                            LoadTaskbyWorkerActivity.this, taskList,
//                            R.layout.list_item_taskbyworker,
//                            new String[]{"id", "deskripsi", "name", "keterangan", "tgl selesai"},
//                            new int[]{R.id.pid, R.id.deskripsi, R.id.name, R.id.keterangan, R.id.tglselesai}
//                    );
                    TaskWorkerAdapter adapter = new TaskWorkerAdapter(
                            LoadTaskbyWorkerActivity.this, taskList,
                            R.layout.list_item_taskbyworker,
                            new String[]{"id", "deskripsi", "name", "keterangan", "tgl selesai"},
                            new int[]{R.id.pid, R.id.deskripsi, R.id.name, R.id.keterangan, R.id.tglselesai}
                    );
                    setListAdapter(adapter);
                }
            });
//            Toast.makeText(getApplicationContext(), "id worker yang login: " + idWorker, Toast.LENGTH_LONG).show();
        }

    }

    /*
     * inner class
     * UpdateStatusTask for update status of task
     */
    class UpdateStatusTask extends AsyncTask<String, String, String>{
        String pid;
        int status;
        String message;
        // constructor of inner class - UpdateStatusTask
        private UpdateStatusTask(String task_id){
            pid = task_id;
        }

        private void updatingData(){
            String endPointGetAllTugas = "tugas/get_all_tugas_by_worker_id";
            JSONObject json = APICon.getDatabyURLParams(endPointGetAllTugas, idWorker);

            // Check your log cat for JSON reponse
            Log.d("All Task: ", json.toString());

            try{
                tasks = json.getJSONArray("tugas");
                Log.d("task: ", tasks.toString());
                for(int i = 0; i < tasks.length(); i++){
                    JSONObject c = tasks.getJSONObject(i);
                    String id = c.getString("task_id");
                    String name = c.getString("project");
                    String deskripsi = c.getString("deskripsi");
                    String keterangan = c.getString("keterangan");
                    String creationDate = c.getString("creation_date");
                    String lastUpdate = c.getString("last_update_timestamp");
                    String tglselesai = c.getString("tanggal_selesai");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("deskripsi", deskripsi);
                    map.put("keterangan", keterangan);
                    map.put("creation date", creationDate);
                    map.put("last update", lastUpdate);
                    map.put("tgl selesai", tglselesai);

                    taskList.add(map);
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadTaskbyWorkerActivity.this);
            pDialog.setMessage("Update Status Tugas...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            String nameParam = "task_id";
            String endPointUpdateStatus = "tugas/update_status_selesai";
            JSONObject json = APICon.setDataOneParam(endPointUpdateStatus, pid, nameParam);
            try{
                status = json.getInt("status");
                message = json.getString("message");
                taskList.clear();
                updatingData();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String url){
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "status: " + status + " message: " + message, Toast.LENGTH_LONG).show();

            //TaskWorkerAdapter adapter = new TaskWorkerAdapter();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //updatingData();

                    TaskWorkerAdapter adapter = new TaskWorkerAdapter(
                            LoadTaskbyWorkerActivity.this, taskList,
                            R.layout.list_item_taskbyworker,
                            new String[]{"id", "deskripsi", "name", "keterangan", "tgl selesai"},
                            new int[]{R.id.pid, R.id.deskripsi, R.id.name, R.id.keterangan, R.id.tglselesai}
                    );
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }



    class TaskWorkerAdapter extends SimpleAdapter{
        private ArrayList<HashMap<String, String>> al;

        public TaskWorkerAdapter(Context context, ArrayList<HashMap<String, String>> items, int resource, String[] from, int[] to){
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
