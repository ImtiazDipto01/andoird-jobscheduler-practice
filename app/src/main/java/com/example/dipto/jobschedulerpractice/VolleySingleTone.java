package com.example.dipto.jobschedulerpractice;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by annanovas on 2/28/2018.
 */

public class VolleySingleTone {

    private static VolleySingleTone volleySingleTone ;
    private RequestQueue requestQueue ;
    private Context context ;

    private VolleySingleTone(Context context){
        this.context = context ;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this.context.getApplicationContext()) ;
        }
        return requestQueue ;
    }

    public static synchronized VolleySingleTone getInstance(Context context){

        if(volleySingleTone == null){
            volleySingleTone = new VolleySingleTone(context) ;
        }
        return  volleySingleTone ;
    }

    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request) ;
    }
}
