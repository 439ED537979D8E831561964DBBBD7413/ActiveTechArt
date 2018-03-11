package com.artace.ruangbudaya;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.ruangbudaya.adapter.EventKirimTawaranTampilAdapter;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventKirimTawaranTampilActivty extends AppCompatActivity {

    public String id_kelompok_seniman;

    RequestQueue queue;
    String urlRead = DatabaseConnection.getReadEventorganizerEvents();
    RecyclerView recyclerView;
    List<PojoEvent> eventList = new ArrayList<PojoEvent>();
    public EventKirimTawaranTampilAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mLoadingAnim;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_kirim_tawaran_tampil_activty);

        Bundle extras = getIntent().getExtras();
        id_kelompok_seniman = extras.getString("id_kelompok_seniman");

        recyclerView = (RecyclerView) findViewById(R.id.event_ktt_recyclerview);
        adapter = new EventKirimTawaranTampilAdapter(this, eventList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.event_ktt_swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
            }
        });

        setToolbar();

        getEvents();
        
    }
    
    private void getEvents(){
        //Set loading anim
        mLoadingAnim = (ProgressBar) findViewById(R.id.event_ktt_progressbar);
        mLoadingAnim.setVisibility(View.VISIBLE);

        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        //empty eventList
        eventList.clear();

        SharedPreferences sharedpreferences = getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        if (session && sharedpreferences.getString(Field.getJenisUser(),null).equals("event_organizer")){
            urlRead += "?id_penyelenggara_acara=" + sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null);
            urlRead += "&id_kelompok_seniman=" + id_kelompok_seniman;
            urlRead += "&ref=EventKirimTawaranTampilActivity";
        }

        Log.d("EventKtt","id_eo = "+sharedpreferences.getString(Field.getIdPenyelenggaraAcara(),null)+" id_kelompok_seniman = "+id_kelompok_seniman);

        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoEvent event = new PojoEvent(obj.getInt("id_acara"),obj.getInt("id_penyelenggara_acara"),obj.getString("nama"),obj.getString("tanggal"),obj.getString("keterangan"),DatabaseConnection.getBaseUrl() + obj.getString("foto"),obj.getString("nama_eo"));

                            // adding event to events array
                            eventList.add(event);

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                }
                finally {
                    mLoadingAnim.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : "+error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }
    
    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.event_ktt_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Pilih Event");
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
}
