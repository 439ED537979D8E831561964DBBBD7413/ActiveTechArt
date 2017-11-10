package com.artace.arthub.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artace.arthub.connection.DatabaseConnection;

import java.util.HashMap;
import java.util.Map;

public class StringPostRequest {

    public StringPostRequest() {
    }

    public void sendPost(Context context, Map<String,String> params, String url, final VolleyResponseListener listener){

        final Map<String,String> paramsFinal = params;
        final Context contextFinal = context;

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mohon Tunggu...");
        pDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("POSTREQUEST",response);
                pDialog.dismiss();
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POSTREQUEST",error.getMessage());
                listener.onError(error.getMessage());
                pDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){

                return paramsFinal;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
}