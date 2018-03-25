package com.artace.ruangbudaya;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


public class TambahEventActivity extends AppCompatActivity {

    EditText tambah_event_tanggalevent, mNama, mTempat, mKeterangan;
    String nama, tanggal, tempat, keterangan, id_eo, foto;
    ImageView mFoto;
    DatePickerDialog datePickerDialog;
    Button mSubmit, mBrowseFoto, mPick;
    Toolbar mToolbar;
    Bitmap bitmapFoto;
    Volley mPostCommentResponse;
    int status;
    int PLACE_PICKER_REQUEST;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);
        PLACE_PICKER_REQUEST = 1;

        googlePlayServiceCheck();

        setToolbar();

        mNama = (EditText) findViewById(R.id.tambah_event_namaevent);
        mTempat = (EditText) findViewById(R.id.tambah_event_lokasi);
        mKeterangan = (EditText) findViewById(R.id.tambah_event_keterangan);
        mFoto = (ImageView) findViewById(R.id.tambah_event_imgView);
        mBrowseFoto = (Button) findViewById(R.id.tambah_event_browse_foto);
        mSubmit = (Button) findViewById(R.id.tambah_event_submit);
        mBrowseFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm(bitmapFoto);
            }
        });

        mPick = (Button) findViewById(R.id.tambah_event_pick_map);
        mPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLocation();
            }
        });


        // initiate the date picker and a button
        tambah_event_tanggalevent = (EditText) findViewById(R.id.tambah_event_tanggalevent);
        // perform click event on edit text
        tambah_event_tanggalevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(TambahEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                tambah_event_tanggalevent.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
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

//                mSubmit.setEnabled(true);
//                mSubmit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                if (requestCode == 100){
                    status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                }
                if (requestCode == PLACE_PICKER_REQUEST){

                    Place place = PlacePicker.getPlace(data, this);
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void googlePlayServiceCheck(){
        status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this,
                        100).show();
            }
        }
    }

    private void pickLocation(){
        if (status == ConnectionResult.SUCCESS) {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tambah_event_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tambah Event");
    }

    private void submitForm(final Bitmap bitmap){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Menambah Event...");
        pDialog.show();
        Request volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, DatabaseConnection.getInsertEvent(),
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("resultMessage", "TAMBAH_EVENT");
                    setResult(TambahEventActivity.RESULT_OK,returnIntent);
                    try{
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Log.d("TambahEvent",res);
                    }
                    catch (Exception e){
                        Log.d("TambahEvent",e.getMessage().toString());
                    }
                    pDialog.dismiss();

            }},

            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                    }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                final String nama = mNama.getText().toString();
                final String tempat = mTempat.getText().toString();
                final String keterangan = mKeterangan.getText().toString();
                final String tanggal = tambah_event_tanggalevent.getText().toString();

                params.put("nama",nama);
                params.put("nama_lokasi",tempat);
                params.put("keterangan",keterangan);
                params.put("tanggal",tanggal);
                params.put("latitude",String.valueOf(latitude));
                params.put("longitude",String.valueOf(longitude));
                SharedPreferences sharedpreferences = getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
                id_eo = sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
                Log.d("LogTambahEvent","ID EO = "+id_eo);
                params.put("id_penyelenggara_acara",id_eo);

                return params;
            }
        @Override
        protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
            Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
            long imagename = System.currentTimeMillis();
            params.put("pic", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
            return params;
        }};
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}