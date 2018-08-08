package com.example.dipto.jobschedulerpractice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessSync {

    private OnResponseListener responseListener ;
    private Context context ;
    private HashMap<String, String> params ;
    private String URL = "http://13.229.26.199/StoreMate_Dev/public/api/v1/login" ;
    private String errorMsg = "failed" ;

    public ProcessSync(Context context){
        this.context = context ;
        this.responseListener = (OnResponseListener) context;
        this.params = new HashMap<String, String>() ;
    }

    public void parseData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showLog("login_success", response);
                        ProcessJSON processJSON = new ProcessJSON(response);
                        processJSON.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //showLog("login_error", String.valueOf(error));
                        responseListener.onRequestError("failed");
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("authentication", "SM.online.app.dev");
                params.put("phone", "01715404405");
                params.put("password", "111111");
                return params;
            }
        };
        VolleySingleTone.getInstance(context).addToRequestQueue(stringRequest);
    }


    @SuppressLint("StaticFieldLeak")
    private class ProcessJSON extends AsyncTask<String, String, Boolean> {

        String responseString = "";

        ProcessJSON(String responseString) {
            this.responseString = responseString;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result = false;
            try {
                JSONObject response = new JSONObject(responseString);
                int error = response.getInt("error");
                if(error == 0){
                    JSONObject data = response.getJSONObject("data");
                    JSONArray shopsJsonArray = data.getJSONArray("shops") ;

                    if(data.getJSONObject("users") != null){
                        result = true ;
                        JSONObject users = data.getJSONObject("users");
                    }


                }
                if(error == 1){
                    result = false ;
                    errorMsg = response.getString("errorMsg");
                }

            } catch (JSONException e) {
                result = false ;
                e.printStackTrace();
            }
            return result ;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                if (responseListener != null) {
                    responseListener.onResponseSuccess("success");
                }
            }
            else {
                if (responseListener != null) {
                    responseListener.onRequestError("failed");
                }
                //Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}
