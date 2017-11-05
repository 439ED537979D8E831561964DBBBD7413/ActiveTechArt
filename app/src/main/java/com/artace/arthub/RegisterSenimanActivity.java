package com.artace.arthub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSenimanActivity extends AppCompatActivity {

    Spinner jenis_seniman;
    EditText username, password, nama, keahlian;
    RadioButton laki, perempuan;
    Button portofolio, foto, daftar;
    RequestQueue queue;
    ImageView view_foto;
    Toolbar mToolbar;
    String[] listJenisSeniman;

    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    ConnectivityManager conMgr;

    private static final String TAG = MainActivity.class.getSimpleName();

    private String UPLOAD_URL = DatabaseConnection.INSERT_REGISTER_SENIMAN;
    String READ_JENIS_SENIMAN_URL;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seniman);
        //START : TOOLBAR

        READ_JENIS_SENIMAN_URL = DatabaseConnection.getReadJenisSeniman();

        mToolbar = (Toolbar) findViewById(R.id.register_seniman_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Register Seniman");

        //END : TOOLBAR
        //cek connection
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }
        //end cek

        jenis_seniman = (Spinner) findViewById(R.id.register_seniman_jenis);
        username = (EditText) findViewById(R.id.register_seniman_username);
        password = (EditText) findViewById(R.id.register_seniman_password);
        nama = (EditText) findViewById(R.id.register_seniman_nama);
        keahlian = (EditText) findViewById(R.id.register_seniman_keahlian);
        laki = (RadioButton) findViewById(R.id.register_seniman_laki);
        perempuan = (RadioButton) findViewById(R.id.register_seniman_perempuan);
        portofolio = (Button) findViewById(R.id.register_seniman_btnPortfolio);
        foto = (Button) findViewById(R.id.register_seniman_btnFoto);
        view_foto = (ImageView) findViewById(R.id.register_seniman_viewFoto);
        daftar = (Button) findViewById(R.id.register_choose_seniman);

        ReadJenisSeniman();

    }

    private void ReadJenisSeniman() {

        Log.e("man","uhuy");

        final Context contextFinal = this;
        queue = AppController.getInstance().getRequestQueue();

        JsonArrayRequest readRequest = new JsonArrayRequest(READ_JENIS_SENIMAN_URL, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray jr = response.getJSONArray(0);
                    List<Integer> listValue = new ArrayList<Integer>();
                    List<String> list = new ArrayList<String>();


                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);
                            listValue.add(obj.getInt("id_jenis_seniman"));

                            list.add(obj.getString("jenis_seniman"));

                            Log.e("man","tapppjiwaaa");


                        } catch (Exception e) {
                            Log.e("man","LOG gamao! = " + e.getMessage());
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(contextFinal,android.R.layout.simple_spinner_dropdown_item, list);

                    jenis_seniman.setAdapter(dataAdapter);
                } catch (Exception e) {
                    Log.e("man","LOG gamao diluar! = " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(readRequest, tag_json_obj);
        queue.add(readRequest);
    }
}