package com.inerre.taskmanager;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 9/16/15.
 * willi
 * email: willi@inerre.com | willi.ilmukomputer@gmail.com
 */
public class APIConnector {

    static JSONObject jObj = null;
    static String json = "";
    private String hostUrl = "http://api.inerre.com/";

    /*
     * constructor
     */
    public APIConnector(){
        // APIConnector constructor here.
    }

    /*
     * method checkUser
     * input from user using method POST
     * to get user data for login
     * return json object
     */
    public JSONObject checkUser(String hostEndPoint, String name, String password){
        String endURL = hostUrl + hostEndPoint;
        try{
            // input param (username login)
            String userName = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
            String userPass = URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            String NamePass = userName + "&" + userPass;

            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(NamePass);
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();
            os.close();
            br.close();
            conn.disconnect();

            jObj = new JSONObject(json);
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    /*
     * method getAll
     * parameter: end point of API
     * example: "project/get_all_project"
     * return json object
     */
    public JSONObject getAllData(String hostEndPoint){
        // set URL
        String endUrl = hostUrl + hostEndPoint;
        try{
            URL url = new URL(endUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // get response code
           /* if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }*/

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine())!= null){
                sb.append(line + "\n");
            }
            json = sb.toString();

            br.close();
            conn.disconnect();

            jObj = new JSONObject(json);

        } catch(IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }

    /*
     * method getDatabyURLParams
     * parameter: end point of API & url parameter
     * example: "tugas/get_all_tugas_by_worker_id/1"
     * return json object
     */
    public JSONObject getDatabyURLParams(String hostEndPoint, String params){
        String endURL = hostUrl + hostEndPoint + "/" + params;
        try{
            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();

            br.close();
            conn.disconnect();
            jObj = new JSONObject(json);
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }

    /*
     * method getDatabyPostParams
     * parameter: end point of API & POST params
     * example: "tugas/get_all_tugas_by_creationdate
     * return json object
     */
    public JSONObject getDatabyPOSTParams(String hostEndPoint, String params, String nameParam){
        String endURL = hostUrl + hostEndPoint;
        try{
            String postParams = URLEncoder.encode(nameParam, "UTF-8") + "=" + URLEncoder.encode(params, "UTF-8");

            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(postParams);
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();
            os.close();
            br.close();
            conn.disconnect();
            jObj = new JSONObject(json);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }

    /*
     * method setDataOneParam
     * input from user using method POST
     * example: "tugas/update_status_selesai
     */
    public JSONObject setDataOneParam(String hostEndPoint, String param, String nameParam){
        String endURL = hostUrl + hostEndPoint;
        try{
            String postParam = URLEncoder.encode(nameParam, "UTF-8") + "=" + URLEncoder.encode(param, "UTF-8");

            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(postParam);
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();
            conn.disconnect();

            jObj = new JSONObject(json);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }

    /*
     * method setDataMultiParams
     * input from user using method POST
     * example: "tugas/set_tugas" or "project/set_project"
     */
    public JSONObject setDataMultiParams(String hostEndPoint, Map<String, String> params){
        String endURL = hostUrl + hostEndPoint;
        try{
            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(paramsToString(params));
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();
            os.close();
            br.close();
            conn.disconnect();

            jObj = new JSONObject(json);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }


    /*
     * method setDataMultiParamsWithCheckbox
     * input from user using method POST + set input with multi checkbox
     */
    public JSONObject setDataMultiParamsWithCheckbox(String hostEndPoint, Map<String, String> params, List<String> checkboxParams, String nameCheckbox){

        String endURL = hostUrl + hostEndPoint;
        try{
            URL url = new URL(endURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(paramsCheckboxToString(params, checkboxParams, nameCheckbox));
            os.flush();

            Log.d("parameter: ", paramsCheckboxToString(params, checkboxParams, nameCheckbox));

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            json = sb.toString();
            os.close();
            br.close();
            conn.disconnect();

            jObj = new JSONObject(json);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("JSON Parser", "Error Parsing Data" + e.toString());
        }
        return jObj;
    }

    /*
     * method paramsToString
     * build string parameter from hashmap<>
     */
    private String paramsToString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    /*
     * method paramsCheckboxToString
     * build string parameter from hashmap<>, list id of checkbox input, and name of checkbox
     */
    private String paramsCheckboxToString(Map<String, String> params, List<String> checkbox, String nameParam) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first){
                first = false;
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        int i = 1;
        result.append("&");
        for(String temp : checkbox){
            if(i == 1){
                i++;
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(nameParam, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(temp, "UTF-8"));
        }
        return result.toString();
    }
}