package com.artace.arthub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterEventOrganizerActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username, password, nama, email, nohp;
    Button daftar;
    int success;
    private static final String TAG = RegisterEventOrganizerActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private EditText register_eventorganizer_username;
    private EditText register_eventorganizer_password;
    private EditText register_eventorganizer_nama;
    private EditText register_eventorganizer_nohp;
    private EditText register_eventorganizer_email;
    private Button btn_register_eventorganizer_submit;
    String tag_json_obj = "json_obj_req";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event_organizer);

        register_eventorganizer_username = (EditText) findViewById(R.id.register_eventorganizer_username);
        register_eventorganizer_password = (EditText) findViewById(R.id.register_eventorganizer_password);
        register_eventorganizer_nama = (EditText) findViewById(R.id.register_eventorganizer_nama);
        register_eventorganizer_nohp = (EditText) findViewById(R.id.register_eventorganizer_nohp);
        register_eventorganizer_email = (EditText) findViewById(R.id.register_eventorganizer_email);

//        btn_register_eventorganizer_submit = (Button) findViewById(R.id.btn_register_eventorganizer_submit);
//
//        //Setting listeners to button
//        btn_register_eventorganizer_submit.setOnClickListener(this);
    }
    private void AddEO(){
        daftar = (Button) findViewById(R.id.btn_register_eventorganizer_submit);
        daftar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String uname = register_eventorganizer_username.getText().toString().trim();
                final String pass = register_eventorganizer_password.getText().toString().trim();
                final String nama = register_eventorganizer_nama.getText().toString().trim();
                final String nohp = register_eventorganizer_nohp.getText().toString().trim();
                final String email = register_eventorganizer_email.getText().toString().trim();

                String url = DatabaseConnection.CREATE_EVENT_ORGANIZER;
                JsonObjectRequest strReq = new JsonObjectRequest(url, null,new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            success = response.getInt(TAG_SUCCESS);
                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", response.toString());

                                Toast.makeText(RegisterEventOrganizerActivity.this, response.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(RegisterEventOrganizerActivity.this, response.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(RegisterEventOrganizerActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

            }

//            private void Daftar(){
//
//
//
//    }

        });
    }
    @Override
    public void onClick(View v) {
        if(v == daftar){
            AddEO();
        }
    }
}