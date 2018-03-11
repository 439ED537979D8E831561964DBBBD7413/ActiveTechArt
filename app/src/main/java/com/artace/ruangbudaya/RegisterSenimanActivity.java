package com.artace.ruangbudaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSenimanActivity extends AppCompatActivity {

    EditText mUsername, mPassword, mNama, mNoHp, mPortfolio;
    Button mFoto, mDaftar;
    RequestQueue queue;
    ImageView mViewFoto;
    Toolbar mToolbar;
    String[] listJenisSeniman;
    Bitmap bitmapFoto;
    ProgressDialog pDialog;
    List<Integer> listValueJenisSeniman;
    List<String> listDisplayJenisSeniman, listDisplayGroupStatus;

    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    ConnectivityManager conMgr;

    private static final String TAG = MainActivity.class.getSimpleName();

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

        mPortfolio = (EditText) findViewById(R.id.register_seniman_portfolio);
        mUsername = (EditText) findViewById(R.id.register_seniman_username);
        mPassword = (EditText) findViewById(R.id.register_seniman_password);
        mNama = (EditText) findViewById(R.id.register_seniman_nama);
        mFoto = (Button) findViewById(R.id.register_seniman_btnFoto);
        mViewFoto = (ImageView) findViewById(R.id.register_seniman_viewFoto);
        mDaftar = (Button) findViewById(R.id.register_seniman_submit);
        mNoHp = (EditText) findViewById(R.id.register_seniman_nohp);

        mDaftar.setEnabled(false);
        mDaftar.setBackgroundColor(getResources().getColor(R.color.colorSecondaryText));

        mFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        mDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userS = mUsername.getText().toString();
                String passS = mPassword.getText().toString();
                String namaS = mNama.getText().toString();
                String nohpS = mNoHp.getText().toString();
                String portfolioS = mPortfolio.getText().toString();

                if (userS.trim().length()>0 && namaS.trim().length()>0 && nohpS.trim().length()>0 && portfolioS.trim().length()>0) {
                    if (passS.trim().length()>5){
                        submitForm(bitmapFoto);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterSenimanActivity.this, "Data harus lengkap", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        ReadJenisSeniman();

    }

    private void submitForm(final Bitmap bitmap){
        final Context contextFinal = this;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mendaftar...");
        if (!pDialog.isShowing())
            pDialog.show();

        //Multipart Volley Request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, DatabaseConnection.getInsertRegisterSeniman(),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Log.e("insertEo",obj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally{
                            pDialog.dismiss();
                            Intent intent = new Intent(contextFinal,LoginActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("jenisRegister","seniman");
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * PARMETER POST
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("tags", "RegisterEoTag");

                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String nama = mNama.getText().toString();
                String no_hp = mNoHp.getText().toString();

                final String portfolio = mPortfolio.getText().toString();

                params.put("username",username);
                params.put("password",password);

                params.put("nama",nama);

                params.put("portfolio",portfolio);
                params.put("no_hp",no_hp);

                return params;
            }

            /*
            * passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmapFoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                mViewFoto.setImageBitmap(bitmapFoto);

                mDaftar.setEnabled(true);
                mDaftar.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



//    private void ReadJenisSeniman() {
//
//        Log.e("man","uhuy");
//
//        final Context contextFinal = this;
//        queue = AppController.getInstance().getRequestQueue();
//
//        JsonArrayRequest readRequest = new JsonArrayRequest(READ_JENIS_SENIMAN_URL, new Response.Listener<JSONArray>() {
//
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d(TAG, response.toString());
//                try {
//                    JSONArray jr = response.getJSONArray(0);
//                    listValueJenisSeniman = new ArrayList<Integer>();
//                    listDisplayJenisSeniman = new ArrayList<String>();
//
//
//                    for (int i = 0; i < jr.length(); i++) {
//                        try {
//
//                            JSONObject obj = (JSONObject) jr.get(i);
//                            listValueJenisSeniman.add(obj.getInt("id_jenis_seniman"));
//
//                            listDisplayJenisSeniman.add(obj.getString("jenis_seniman"));
//
//                            Log.e("man","tapppjiwaaa");
//
//                        } catch (Exception e) {
//                            Log.e("man","LOG gamao! = " + e.getMessage());
//                        }
//                    }
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(contextFinal,android.R.layout.simple_spinner_dropdown_item, listDisplayJenisSeniman);
//
//                    mJenisSeniman.setAdapter(dataAdapter);
//                } catch (Exception e) {
//                    Log.e("man","LOG gamao diluar! = " + e.getMessage());
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
//            }
//        });
//
//        // Adding request to request queue
////        AppController.getInstance().addToRequestQueue(readRequest, tag_json_obj);
//        queue.add(readRequest);
//    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}