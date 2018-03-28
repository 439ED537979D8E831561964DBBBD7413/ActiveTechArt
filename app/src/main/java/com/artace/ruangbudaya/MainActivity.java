package com.artace.ruangbudaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.artace.ruangbudaya.ViewPlugins.DrawerMenu;
import com.artace.ruangbudaya.adapter.ListEventMainAdapter;
import com.artace.ruangbudaya.adapter.ListSenimanMainAdapater;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoEvent;
import com.artace.ruangbudaya.pojo.PojoSeniman;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private SliderLayout mDemoSlider;
    CardView cardTari,cardTeater, cardMusisi, cardWayang, cardKomedian;
    TextView statistikKelompokSeniman, statistikAcara;
    RecyclerView recyclerView,recyclerViewEvent;
    ListSenimanMainAdapater adapter;
    ListEventMainAdapter adapterEvent;
    List<PojoSeniman> senimanList = new ArrayList<PojoSeniman>();
    List<PojoEvent> eventList = new ArrayList<PojoEvent>();
    RequestQueue queue;
    SharedPreferences sharedpreferences;
    Boolean session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runIntro();

        //START : TOOLBAR
        mToolbar = findViewById(R.id.main_toolbar);
        this.setSupportActionBar(mToolbar);
        ActionBar ab = this.getSupportActionBar();
        ab.setTitle("");

//        //START : DRAWER MENU
//
        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(this, this, mToolbar);

//        //END : DRAWER MENU
//
//        //END : TOOLBAR
//

        //start : recylcerviewSeniman

        recyclerView = findViewById(R.id.main_recyclerview_seniman);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ListSenimanMainAdapater(this, senimanList);
        recyclerView.setAdapter(adapter);

        getData();

        //end   : recyclerviewSeniman

        //start : recylcerviewEvent

        recyclerViewEvent = findViewById(R.id.main_recyclerview_acara);
        LinearLayoutManager layoutManagerEvent
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewEvent.setLayoutManager(layoutManagerEvent);
        recyclerViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapterEvent = new ListEventMainAdapter(this, eventList);
        recyclerViewEvent.setAdapter(adapterEvent);

        getDataEvent();

        //end   : recyclerviewEvent

        sharedpreferences = this.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);

        session = sharedpreferences.getBoolean(Field.getSessionStatus(), false);

        if (session && sharedpreferences.getString(Field.getJenisUser(),null).equals("seniman")){
            Intent intent = new Intent(MainActivity.this,SenimanMainActivity.class);
            intent.putExtra("id_kelompok_seniman",sharedpreferences.getString(Field.getIdKelompokSeniman(),null));
            startActivity(intent);
        }

        //START : CARDS

        cardTari = findViewById(R.id.main_card_view_tari);
        cardTari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Tari");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        cardTeater = findViewById(R.id.main_card_view_teater);
        cardTeater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Teater");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        cardMusisi = findViewById(R.id.main_card_view_musisi);
        cardMusisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Musisi");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        cardWayang = findViewById(R.id.main_card_view_wayang);
        cardWayang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Wayang");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        cardKomedian = findViewById(R.id.main_card_view_komedian);
        cardKomedian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Komedian");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
            //END : CARD
        getStatistik();
    }



    public void getData(){

        String url = DatabaseConnection.getReadSenimanListMain();

        queue = AppController.getInstance().getRequestQueue();

        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoSeniman seniman = new PojoSeniman(obj.getInt("id_kelompok_seniman"),obj.getInt("id_bidang_seni"),obj.getInt("id_user"),obj.getString("nama"),obj.getString("portfolio"),obj.getString("no_hp"), DatabaseConnection.getBaseUrl() + obj.getString("foto"));

                            // adding event to events array
                            senimanList.add(seniman);
                            Log.d("OSFragment","Iterasi pertama");

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapter.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    Log.e("OSFragment","LOG gamao diluar! = " + e.getMessage());
                }
//                finally {
//                    mLoadingAnim.setVisibility(View.GONE);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
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

    public void getDataEvent(){

        String url = DatabaseConnection.getReadEventListMain();

        queue = AppController.getInstance().getRequestQueue();

        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            PojoEvent event = new PojoEvent(obj.getInt("id_acara"),obj.getString("nama"),obj.getString("tanggal"),DatabaseConnection.getBaseUrl() + obj.getString("foto"));

                            // adding event to events array
                            eventList.add(event);
                            Log.d("OSFragment","Iterasi pertama");

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        } finally {
                            //Notify adapter about data changes
                            adapterEvent.notifyItemChanged(i);
                        }
                    }
                }catch (Exception e){
                    Log.e("OSFragment","LOG gamao diluar! = " + e.getMessage());
                }
//                finally {
//                    mLoadingAnim.setVisibility(View.GONE);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
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

    private void runIntro(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }

    private void getStatistik(){
        String urlRead1;

        urlRead1 = DatabaseConnection.getStatistikMain();
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

//                            int idAcara = obj.getInt("id_acara");
//                            int idPenyelenggaraAcara = obj.getInt("id_penyelenggara_acara");

                            statistikAcara = findViewById(R.id.main_statistik_acara);
                            statistikKelompokSeniman = findViewById(R.id.main_statistik_seniman);

                            statistikAcara.setText(obj.getString("jumlah_acara"));
                            statistikKelompokSeniman.setText(obj.getString("jumlah_seniman"));

                            Log.d("EventDetail","keterangan = "+obj.getInt("jumlah_acara"));

                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                            Log.e("Log asd3 = ", e.getMessage());
                        }
                    }
                }catch (Exception e){
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                    Log.e("Log asd2 = ", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : "+error.getMessage());
                Log.e("Log asd1 = ", error.getMessage());
            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
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

    //langsung keluar
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

