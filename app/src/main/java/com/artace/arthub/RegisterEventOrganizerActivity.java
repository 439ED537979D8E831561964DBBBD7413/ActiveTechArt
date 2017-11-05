package com.artace.arthub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.artace.arthub.connection.DatabaseConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterEventOrganizerActivity extends AppCompatActivity{

    EditText mUsername, mPassword, mNama, mEmail, mNoHp;
    Button mSubmit, mBrowseFoto;
    ImageView mFoto;
    Bitmap bitmapFoto;
    ProgressDialog pDialog;
    Toolbar mToolbar;
    int success;
    private static final String TAG = RegisterEventOrganizerActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event_organizer);

        setToolbar();

        mUsername = (EditText) findViewById(R.id.register_eventorganizer_username);
        mPassword = (EditText) findViewById(R.id.register_eventorganizer_password);
        mNama = (EditText) findViewById(R.id.register_eventorganizer_nama);
        mEmail = (EditText) findViewById(R.id.register_eventorganizer_nohp);
        mNoHp = (EditText) findViewById(R.id.register_eventorganizer_email);
        mFoto = (ImageView) findViewById(R.id.register_eventorganizer_foto);
        mSubmit = (Button) findViewById(R.id.btn_register_eventorganizer_submit);

        mSubmit.setEnabled(false);
        mSubmit.setBackgroundColor(getResources().getColor(R.color.colorSecondaryText));

        mBrowseFoto = (Button) findViewById(R.id.register_eventorganizer_browse_foto);
        mBrowseFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        mSubmit = (Button) findViewById(R.id.btn_register_eventorganizer_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                submitForm(bitmapFoto);
            }

        });

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
                mFoto.setImageBitmap(bitmapFoto);

                mSubmit.setEnabled(true);
                mSubmit.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void submitForm(final Bitmap bitmap) {
        final Context contextFinal = this;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mendaftar...");
        if (!pDialog.isShowing())
            pDialog.show();

        //Multipart Volley Request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, DatabaseConnection.getCreateEventOrganizer(),
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
                            extras.putString("jenisRegister","eventorganizer");
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
                final String uname = mUsername.getText().toString();
//                if (mUsername.getText().toString().toString().isEmpty()) {
//                    Toast.makeText(RegisterEventOrganizerActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
//                    return params;
//                }
                final String pass = mPassword.getText().toString();
                final String nama = mNama.getText().toString();
                final String nohp = mNoHp.getText().toString();
                final String email = mEmail.getText().toString();
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                mEmail.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {

                        if (email.matches(emailPattern) && s.length() > 0)
                        {
                            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // other stuffs
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // other stuffs
                    }
                });

                params.put("username",uname);
                params.put("password",pass);
                params.put("nama",nama);
                params.put("no_hp",nohp);
                params.put("email",email);
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

    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.register_eventorganizer_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Register Event Organizer");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}