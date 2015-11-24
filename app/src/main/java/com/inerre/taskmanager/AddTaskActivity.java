package com.inerre.taskmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 10/29/15.
 * willi
 * willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class AddTaskActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    APIConnector APICon = new APIConnector();
    // project JSONArray
    JSONArray projects = null;
    // worker JSONArray
    JSONArray worker = null;

    ArrayList<StringWithTag> projectList;
    final List<KeyPairBoolData> workerList = new ArrayList<>();

    HashMap<String, String> mapProject = new HashMap<>();
//    HashMap<String, String> mapWorker = new HashMap<>();

    Spinner spinnerProject;
    MultiSpinnerSearch spinnerWorker;
    EditText etDeskripsi;
    EditText etKeterangan;

    String tag;

    List<String> listID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        projectList = new ArrayList<>();

        spinnerProject = (Spinner) findViewById(R.id.spinnerProject);
        spinnerWorker = (MultiSpinnerSearch) findViewById(R.id.spinnerWorker);
        etDeskripsi = (EditText) findViewById(R.id.editTextDeskripsi);
        etKeterangan = (EditText) findViewById(R.id.editTextKeterangan);

        new LoadProjectWorker().execute();

        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag s = (StringWithTag) parent.getItemAtPosition(position);
                tag = s.tag;
                Toast.makeText(getApplicationContext(), tag, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * spinner worker set worker list to multi selection spinner
         */
        spinnerWorker.setItems(workerList, "Select Worker", -1, new MultiSpinnerSearch.MultiSpinnerSearchListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getName() + " (" + items.get(i).getIdString() + ") : " + items.get(i).isSelected());
                    }
                }
                Log.i("list ID ", spinnerWorker.getListID().toString());
                listID = spinnerWorker.getListID();
//                Toast.makeText(getApplicationContext(), listID.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
     * method onClickAddTask
     * execute AddTask
     */
    public void onClickAddTask(View view){
        int sizeListID = listID.size();
        if(sizeListID == 0){
            Toast.makeText(getApplicationContext(), "Pilih dahulu worker...", Toast.LENGTH_LONG).show();
        }else{
            new AddTask().execute();
            etDeskripsi.setText("");
            etKeterangan.setText("");
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /*
     * inner class
     * Class: LoadProject
     */
    class LoadProjectWorker extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddTaskActivity.this);
            pDialog.setMessage("Loading please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            // load project to Spinner Project
            getProject();
            // load worker to Spinner Worker
            getWorker();
            return null;
        }

        protected void onPostExecute(String url){
            pDialog.dismiss();
            Log.d("project list: ", projectList.toString());
            Log.d("worker list: ", workerList.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<StringWithTag> sAdapterProject = new ArrayAdapter<>(AddTaskActivity.this, android.R.layout.simple_spinner_item, projectList);
                    sAdapterProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProject.setAdapter(sAdapterProject);
                }
            });
        }

        /*
         * method getProject
         * load project for spinner
         */
        private void getProject(){
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
                    mapProject.put(id, name);
                    projectList.add(new StringWithTag(name, id));
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        /*
         * method getWorker
         * load worker for multi selection spinner
         */
        private void getWorker(){
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

//                    mapWorker.put(id, name);

                    KeyPairBoolData kpb = new KeyPairBoolData();
                    kpb.setId(i+1);
                    kpb.setName(name);
                    kpb.setIdString(id);
                    kpb.setSelected(false);
                    workerList.add(kpb);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

    /*
     * inner class
     * Class: AddTask
     */
    class AddTask extends AsyncTask<String, String, String>{

        int status;
        String message;
        String lid;
        String idproject;
        String keterangan;
        String deskripsi;
        String desk = etDeskripsi.getText().toString();
        String keter = etKeterangan.getText().toString();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(AddTaskActivity.this);
            pDialog.setMessage("Saving task ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            idproject = tag;
            deskripsi = desk;
            keterangan = keter;

            Map<String, String> params = new HashMap<>();
            params.put("project",idproject);
            params.put("deskripsi", deskripsi);
            params.put("keterangan", keterangan);

            String endPointSetTugas = "tugas/set_tugas";
            JSONObject jsonSetTugas = APICon.setDataMultiParamsWithCheckbox(endPointSetTugas, params, listID, "worker[]");

            // Check your log cat for JSON reponse
            Log.d("set tugas: ", jsonSetTugas.toString());

            lid = listID.toString();
            try{
                status = jsonSetTugas.getInt("status");
                message = jsonSetTugas.getString("message");
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            pDialog.dismiss();
            Log.d("status : ", String.valueOf(status));
            Log.d("message : ", message);
            Log.d("id worker : ", lid);
            Log.d("deskripsi : ", deskripsi);
            Log.d("keterangan : ", keterangan);
        }

    }
}
