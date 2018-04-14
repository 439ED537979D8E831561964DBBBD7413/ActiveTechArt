package com.artace.ruangbudaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.ViewPlugins.GenerateQR;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.utils.YoutubeId;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONObject;

public class EoSenimanActivity extends AppCompatActivity {

    RequestQueue queue;
    String urlRead;
    public SharedPreferences sharedpreferences;
    public NetworkImageView portfolio;
    private Context context;
    Button beriTawaran;
//    ImageView iv;
//    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eo_seniman);

        Bundle extras = getIntent().getExtras();
        int id_kelompok_seniman = extras.getInt("id_kelompok_seniman");
        urlRead = DatabaseConnection.getReadDetailSeniman() + "?id_kelompok_seniman=" + String.valueOf(id_kelompok_seniman);


        Toolbar toolbar = (Toolbar) findViewById(R.id.eo_seniman_toolbar);
        ((AppCompatActivity)this).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity)this).getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

//        iv = (ImageView) findViewById(R.id.qr);
//


        initCollapsingToolbar();

        getDetailSeniman();
    }



    private void getDetailSeniman() {
        //Getting Instance of Volley Request Queue
        queue = AppController.getInstance().getRequestQueue();

        Button btnBeriTawaran = (Button) findViewById(R.id.eo_seniman_btnBeriTawaranTampil);

        SharedPreferences sharedpreferences = this.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);
        if (session && sharedpreferences.getString(Field.getJenisUser(),null).equals("event_organizer")){
            btnBeriTawaran.setVisibility(View.VISIBLE);
        }
        else{
            btnBeriTawaran.setVisibility(View.GONE);
        }

        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(urlRead, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray jr = response.getJSONArray(0);
                    for (int i = 0; i < jr.length(); i++) {
                        try {

                            JSONObject obj = (JSONObject) jr.get(i);

                            int idKelompokSeniman = obj.getInt("id_kelompok_seniman");
                            final int idKelompokSenimanFinal = idKelompokSeniman;
                            final String fotoFinal = DatabaseConnection.BASE_URL + obj.getString("foto");

//                            int idEventOrganizer = obj.getInt("id_event_organizer");

                            beriTawaran = (Button) findViewById(R.id.eo_seniman_btnBeriTawaranTampil);
                            beriTawaran.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(EoSenimanActivity.this, EventKirimTawaranTampilActivty.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("id_kelompok_seniman",String.valueOf(idKelompokSenimanFinal));
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            });

                            TextView namaSeniman = (TextView) findViewById(R.id.eo_seniman_namaSeniman);
                            namaSeniman.setText(obj.getString("nama"));

                            EditText nohpSeniman = (EditText) findViewById(R.id.eo_seniman_noTelp);
                            nohpSeniman.setText(obj.getString("no_hp"));

                            EditText ketSeniman = (EditText) findViewById(R.id.eo_seniman_keterangan);
                            ketSeniman.setText(obj.getString("keterangan"));

                            NetworkImageView fotoSeniman = (NetworkImageView) findViewById(R.id.eo_seniman_imageSeniman);
                            fotoSeniman.setImageUrl(DatabaseConnection.getBaseUrl() + obj.getString("foto"), AppController.getInstance().getImageLoader());
                            fotoSeniman.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(EoSenimanActivity.this ,ShowPictureActivity.class);
                                    intent.putExtra("imageViewExtra",fotoFinal);
                                    startActivity(intent);
                                }
                            });


                            String Sportfolio = obj.getString("portfolio");

                            YoutubeId generateYoutubeId = new YoutubeId();
                            final String idThumbnail = generateYoutubeId.generateId(Sportfolio);

                            NetworkImageView portfolioSeniman = (NetworkImageView) findViewById(R.id.eo_seniman_videoSeniman);
                            portfolioSeniman.setImageUrl("https://img.youtube.com/vi/" + idThumbnail + "/0.jpg", AppController.getInstance().getImageLoader());

//                            fotoSeniman = (NetworkImageView) findViewById(R.id.fragment_seniman_home_profile_videoSeniman);
                            portfolioSeniman.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(EoSenimanActivity.this, YoutubePlayerViewActivity.class);
                                    intent.putExtra("idThumbnailExtra", idThumbnail);
                                    EoSenimanActivity.this.startActivity(intent);
                                }
                            });



                        } catch (Exception e) {
                            System.out.println("LOG gamao! = " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("LOG gamao diluar! = " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("LOG_OrganizerEventsFragment : " + error.getMessage());

            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.eo_seniman_collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.eo_seniman_appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Detail Seniman");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

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

