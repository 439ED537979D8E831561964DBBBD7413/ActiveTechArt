package com.artace.arthub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.artace.arthub.ViewPlugins.DrawerMenu;
import com.artace.arthub.constant.Field;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private SliderLayout mDemoSlider;
    ConstraintLayout main_first_row, cardPenari, cardMusisi, cardBondres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //START : TOOLBAR
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //END : TOOLBAR

        SharedPreferences sharedpreferences = this.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);

        Boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(), false);
        String jenisSeniman = sharedpreferences.getString(Field.getJenisUser(),null);

        if (session && jenisSeniman.equals("seniman")){
            Intent intent = new Intent(MainActivity.this,SenimanMainActivity.class);
            intent.putExtra("id_seniman",sharedpreferences.getString(Field.getIdSeniman(),null));
            startActivity(intent);
        }


        //START : DRAWER MENU

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(this,this, mToolbar);

        //END : DRAWER MENU

        //START : SLIDER LAYOUT

        mDemoSlider = (SliderLayout)findViewById(R.id.main_slider);

//        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("    _",R.drawable.header1);
        file_maps.put("    .",R.drawable.header2);
        file_maps.put("    ,",R.drawable.header3);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        //END : DRAWER MENU

        //START : CARDS

        main_first_row = (ConstraintLayout) findViewById(R.id.main_first_row);
        main_first_row.bringToFront();

        cardPenari = (ConstraintLayout) findViewById(R.id.main_card_penari);
        cardPenari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Penari");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        cardMusisi = (ConstraintLayout) findViewById(R.id.main_card_musisi);
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

        cardBondres = (ConstraintLayout) findViewById(R.id.main_card_bondres);
        cardBondres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OrganizerMainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("TITLE","Bondres");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        //END : CARD

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

