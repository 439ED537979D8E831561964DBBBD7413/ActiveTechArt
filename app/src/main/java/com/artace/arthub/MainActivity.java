package com.artace.arthub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    TextView txt,txt1;
    String password, username;
    SharedPreferences sharedpreferences;

    public static final String TAG_PASSWORD = "password";
    public static final String TAG_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the value username and password from login
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        password = getIntent().getStringExtra(TAG_PASSWORD);
        username = getIntent().getStringExtra(TAG_USERNAME);
        //end

        //START : TOOLBAR
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //END : TOOLBAR

        //START : DRAWER MENU

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(
                        new ProfileDrawerItem().withName("Guest").withEmail("Silahkan Login").withIcon(getResources().getDrawable(R.drawable.profile_img_circle))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Login").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Daftar Sebagai Seniman").withIcon(GoogleMaterial.Icon.gmd_account_box);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Daftar Sebagai Event organizer").withIcon(GoogleMaterial.Icon.gmd_account_box);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("Logout").withIcon(GoogleMaterial.Icon.gmd_account_circle);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        new DividerDrawerItem(),
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2){
                            Intent intent = new Intent(MainActivity.this, RegisterSenimanActivity.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3){
                            Intent intent = new Intent(MainActivity.this, RegisterEventOrganizerActivity.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 5){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(LoginActivity.session_status, false);
                            editor.putString(TAG_PASSWORD, null);
                            editor.putString(TAG_USERNAME, null);
                            editor.commit();

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                })
                .build();

        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

