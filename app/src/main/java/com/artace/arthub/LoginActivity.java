package com.artace.arthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView btnRegister;
    Button btnRegisterSeniman,btnRegisterEO,btnLogin;

    int success;
    ConnectivityManager conMgr;

    ProgressDialog pDialog;
    EditText user, pass;
    Intent intent;

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String password1, username1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.login_username);
        pass = (EditText) findViewById(R.id.login_password);

        //Cek intent dari register
        checkFromRegister();

        //cek koneksi
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

        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);
        password1 = sharedpreferences.getString(Field.getTagPassword(), null);
        username1 = sharedpreferences.getString(Field.getTagUsername(), null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Field.getTagPassword(), password1);
            intent.putExtra(Field.getTagUsername(), username1);
            startActivity(intent);
            finish();
        }

        actionBtnLogin();
        viewActions();

    }

    private void actionBtnLogin(){
        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                // mengecek kolom yang kosong
                String username = user.getText().toString();
                String password = pass.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void viewActions(){
        btnRegister = (TextView) findViewById(R.id.login_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.register_choose);

                btnRegisterSeniman = (Button) findViewById(R.id.register_choose_seniman);
                btnRegisterSeniman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoginActivity.this,RegisterSenimanActivity.class);
                        startActivity(intent);
                    }
                });

                btnRegisterEO = (Button) findViewById(R.id.register_choose_eo);
                btnRegisterEO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LoginActivity.this,RegisterEventOrganizerActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        //TODO : ONCLICK REGISTER IO
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();

        String urlLogin = DatabaseConnection.getLogin(username,password);

        JsonObjectRequest loginRequest = new JsonObjectRequest(urlLogin,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                hideDialog();
                try {

                    success = response.getInt(TAG_SUCCESS);
                    // Check for error node in json
                    if (success == 1) {

                        String username = Field.getTagUsername();
                        String password = Field.getTagPassword();
                        String id_user = Field.getIdUser();
                        String nama = Field.getNAMA();
                        String no_hp = Field.getNoHp();
                        String foto = Field.getFOTO();
                        String jenis_user = Field.getJenisUser();

                        // menyimpan login ke session
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Field.getSessionStatus(), true);
                        editor.putString(username, response.getString(password));
                        editor.putString(password, response.getString(username));
                        editor.putString(id_user, response.getString(id_user));
                        editor.putString(nama, response.getString(nama));
                        editor.putString(no_hp, response.getString(no_hp));
                        editor.putString(foto, DatabaseConnection.getBaseUrl() + response.getString(foto));
                        editor.putString(jenis_user, response.getString(jenis_user));


                        if(response.getString(Field.getJenisUser()).equals("seniman")){
                            String id_seniman = Field.getIdSeniman();
                            String id_jenis_seniman = Field.getIdJenisSeniman();
                            String jenis_kelamin = Field.getJenisKelamin();
                            String portfolio = Field.getPORTFOLIO();
                            String umur = Field.getUMUR();
                            String keahlian_spesifik = Field.getKeahlianSpesifik();
                            String format_solo_group = Field.getFormatSoloGrup();

                            editor.putString(id_seniman, response.getString(id_seniman));
                            editor.putString(id_jenis_seniman, response.getString(id_jenis_seniman));
                            editor.putString(jenis_kelamin, response.getString(jenis_kelamin));
                            editor.putString(portfolio, response.getString(portfolio));
                            editor.putString(umur, response.getString(umur));
                            editor.putString(keahlian_spesifik, response.getString(keahlian_spesifik));
                            editor.putString(format_solo_group, response.getString(format_solo_group));
                        }
                        else{ //Jika event organizer
                            String id_event_organizer = Field.getIdEventOrganizer();
                            String email = Field.getEMAIL();

                            editor.putString(id_event_organizer, response.getString(id_event_organizer));
                            editor.putString(email, response.getString(email));
                        }

                        editor.commit();

                        Log.e("Successfully Login!", response.toString());

                        // Memanggil main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Username dan Password Salah", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(loginRequest, tag_json_obj);
    }

    private void checkFromRegister(){

        if (getIntent() != null){
            Intent intent = getIntent();


            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey("jenisRegister")) {
                    String jenisRegister = extras.getString("jenisRegister");

                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(this)
                                    .setTitle("Terdaftar")
                                    .setMessage("Silahkan login untuk melanjutkan.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    alertDialogBuilder.show();

                    // TODO: Do something with the value of isNew.
                }
            }
        }

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}