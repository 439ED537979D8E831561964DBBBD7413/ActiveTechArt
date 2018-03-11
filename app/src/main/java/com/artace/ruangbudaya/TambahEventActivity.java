package com.artace.ruangbudaya;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.widget.EditText;

import com.android.volley.toolbox.Volley;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;


public class TambahEventActivity extends AppCompatActivity {

    EditText tambah_event_tanggalevent, mNama, mTempat, mKeterangan;
    String nama, tanggal, tempat, keterangan, id_eo;
    DatePickerDialog datePickerDialog;
    Button mSubmit;
    Toolbar mToolbar;
    Volley mPostCommentResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_event);

        setToolbar();

        mNama = (EditText) findViewById(R.id.tambah_event_namaevent);
        mTempat = (EditText) findViewById(R.id.tambah_event_lokasi);
        mKeterangan = (EditText) findViewById(R.id.tambah_event_keterangan);
        mSubmit = (Button) findViewById(R.id.tambah_event_submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
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

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tambah_event_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tambah Event");
    }

    private void submitForm(){
        nama = mNama.getText().toString();
        tempat = mTempat.getText().toString();
        keterangan = mKeterangan.getText().toString();
        tanggal = tambah_event_tanggalevent.getText().toString();

        Map<String,String> params = new HashMap<String, String>();
        params.put("nama",nama);
        params.put("tanggal",tanggal);
        params.put("lokasi",tempat);
        params.put("keterangan",keterangan);

        SharedPreferences sharedpreferences = getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        id_eo = sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
        Log.d("LogTambahEvent","ID EO = "+id_eo);
        params.put("id_penyelenggara_acara",id_eo);


        StringPostRequest strReq = new StringPostRequest();
        strReq.sendPost(this, params, DatabaseConnection.getInsertEvent(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                finish();
            }

            @Override
            public void onError(String message) {
                Log.e("TambahEventActivity","Ada error : "+message);
            }
        });
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
        Intent returnIntent = new Intent();
        returnIntent.putExtra("resultMessage", "TAMBAH_EVENT");
        setResult(TambahEventActivity.RESULT_OK,returnIntent);

        super.finish();
    }
}