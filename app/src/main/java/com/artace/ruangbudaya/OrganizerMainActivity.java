package com.artace.ruangbudaya;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.artace.ruangbudaya.ViewPlugins.DrawerMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class OrganizerMainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    String title;
    OrganizerSenimanFragment newFragment1;
    OrganizerEventsFragment newFragment2;
    OrganizerDiundangFragment newFragment3;
    EventMapsFragment newFragmentMaps;
    OrganizerInfoBidangSeniPenariFragment newFragmentPenari;
    OrganizerInfoBidangSeniTeaterFragment newFragmentTeater;
    OrganizerInfoBidangSeniWayangFragment newFragmentWayang;
    OrganizerInfoBidangSeniMusisiFragment newFragmentMusisi;
    OrganizerInfoBidangSeniKomedianFragment newFragmentKomedian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_main);

        DrawerMenu drawerMenu = new DrawerMenu();
        drawerMenu.createDrawer(this,this, null);

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            title = extras.getString("TITLE");
//            setToolbar(title);
        }
        else{
            title = savedInstanceState.getString("TITLE");
//            setToolbar(title);
        }

        initFragment(savedInstanceState);
        bottomBarListeners();


    }

//    public void initMaps(){
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentByTag("Maps");
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("TITLE",title);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void initFragment(Bundle savedInstanceState){
        if (findViewById(R.id.organizer_main_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            if (getIntent() != null){
                Bundle extras = getIntent().getExtras();
                title = extras.getString("TITLE");
//            setToolbar(title);
            }
            else{
                title = savedInstanceState.getString("TITLE");
//            setToolbar(title);
            }

            newFragment1 = new OrganizerSenimanFragment();
            newFragment2 = new OrganizerEventsFragment();
            newFragment3 = new OrganizerDiundangFragment();
            newFragmentMaps = new EventMapsFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.organizer_main_fragment_container, newFragment1,"Seniman");

            transaction.add(R.id.organizer_main_fragment_container, newFragment2,"Events");
            transaction.hide(newFragment2);
            transaction.add(R.id.organizer_main_fragment_container, newFragment3,"Diundang");
            transaction.hide(newFragment3);
            transaction.add(R.id.organizer_main_fragment_container, newFragmentMaps,"Maps");
            transaction.hide(newFragmentMaps);

            if (title.equals("Tari")){
                newFragmentPenari = new OrganizerInfoBidangSeniPenariFragment();
                transaction.add(R.id.organizer_main_fragment_container, newFragmentPenari,"Penari");
                transaction.hide(newFragmentPenari);
            }
            else if (title.equals("Wayang")){
                newFragmentWayang = new OrganizerInfoBidangSeniWayangFragment();
                transaction.add(R.id.organizer_main_fragment_container, newFragmentWayang,"Wayang");
                transaction.hide(newFragmentWayang);
            }
            else if (title.equals("Teater")){
                newFragmentTeater = new OrganizerInfoBidangSeniTeaterFragment();
                transaction.add(R.id.organizer_main_fragment_container, newFragmentTeater,"Teater");
                transaction.hide(newFragmentTeater);
            }
            else if (title.equals("Musisi")){
                newFragmentMusisi = new OrganizerInfoBidangSeniMusisiFragment();
                transaction.add(R.id.organizer_main_fragment_container, newFragmentMusisi,"Musisi");
                transaction.hide(newFragmentMusisi);
            }
            else if (title.equals("Komedian")){
                newFragmentKomedian = new OrganizerInfoBidangSeniKomedianFragment();
                transaction.add(R.id.organizer_main_fragment_container, newFragmentKomedian,"Komedian");
                transaction.hide(newFragmentKomedian);
            }


            transaction.commit();
        }
    }

    private void bottomBarListeners(){
        BottomBar bottomBar = findViewById(R.id.organizer_main_bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_seniman) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment1);

                    if(title.equals("Tari")){
                        transaction.hide(newFragmentPenari);
                    }
                    else if(title.equals("Teater")){
                        transaction.hide(newFragmentTeater);
                    }
                    else if(title.equals("Musisi")){
                        transaction.hide(newFragmentMusisi);
                    }
                    else if(title.equals("Wayang")){
                        transaction.hide(newFragmentWayang);
                    }
                    else{
                        transaction.hide(newFragmentKomedian);
                    }

                    transaction.hide(newFragment2);
                    transaction.hide(newFragment3);
                    transaction.hide(newFragmentMaps);

                    transaction.commit();
                }
                else if (tabId == R.id.tab_events) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment2);
                    transaction.hide(newFragment1);
                    transaction.hide(newFragment3);
                    transaction.hide(newFragmentMaps);

                    if(title.equals("Tari")){
                        transaction.hide(newFragmentPenari);
                    }
                    else if(title.equals("Teater")){
                        transaction.hide(newFragmentTeater);
                    }
                    else if(title.equals("Musisi")){
                        transaction.hide(newFragmentMusisi);
                    }
                    else if(title.equals("Wayang")){
                        transaction.hide(newFragmentWayang);
                    }
                    else{
                        transaction.hide(newFragmentKomedian);
                    }

                    transaction.commit();
                    newFragment2.setToolbar();
                }
                else if (tabId == R.id.tab_diundang) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment3);

                    transaction.hide(newFragment1);
                    transaction.hide(newFragment2);
                    transaction.hide(newFragmentMaps);

                    if(title.equals("Tari")){
                        transaction.hide(newFragmentPenari);
                    }
                    else if(title.equals("Teater")){
                        transaction.hide(newFragmentTeater);
                    }
                    else if(title.equals("Musisi")){
                        transaction.hide(newFragmentMusisi);
                    }
                    else if(title.equals("Wayang")){
                        transaction.hide(newFragmentWayang);
                    }
                    else{
                        transaction.hide(newFragmentKomedian);
                    }

                    transaction.commit();
                    newFragment3.setToolbar();
                }
                else if (tabId == R.id.tab_info) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(newFragmentMaps);
                    transaction.hide(newFragment1);
                    transaction.hide(newFragment2);
                    transaction.hide(newFragment3);

                    if (title.equals("Teater")){
                        transaction.show(newFragmentTeater);
                        transaction.commit();
                        newFragmentTeater.setToolbar();
                    }
                    else if(title.equals("Tari")){
                        transaction.show(newFragmentPenari);

                        transaction.commit();
                        newFragmentPenari.setToolbar();
                    }
                    else if (title.equals("Musisi")){
                        transaction.show(newFragmentMusisi);

                        transaction.commit();
                        newFragmentMusisi.setToolbar();
                    }
                    else if (title.equals("Wayang")){
                        transaction.show(newFragmentWayang);
                        transaction.commit();
                        newFragmentWayang.setToolbar();
                    }
                    else if (title.equals("Komedian")){
                        transaction.show(newFragmentKomedian);

                        transaction.commit();
                        newFragmentKomedian.setToolbar();
                    }

                }
            }
        });
    }

    public void showEventMaps(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(newFragment1);
        transaction.hide(newFragment2);
        transaction.hide(newFragment3);
        if(title.equals("Tari")){
            transaction.hide(newFragmentPenari);
        }
        else if(title.equals("Teater")){
            transaction.hide(newFragmentTeater);
        }
        else if(title.equals("Musisi")){
            transaction.hide(newFragmentMusisi);
        }
        else if(title.equals("Wayang")){
            transaction.hide(newFragmentWayang);
        }
        else{
            transaction.hide(newFragmentKomedian);
        }
        transaction.show(newFragmentMaps);
        transaction.commit();
        newFragmentMaps.setToolbar();
    }

    public void showEventList(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(newFragmentMaps);
        transaction.hide(newFragment1);
        transaction.hide(newFragment3);
        if(title.equals("Tari")){
            transaction.hide(newFragmentPenari);
        }
        else if(title.equals("Teater")){
            transaction.hide(newFragmentTeater);
        }
        else if(title.equals("Musisi")){
            transaction.hide(newFragmentMusisi);
        }
        else if(title.equals("Wayang")){
            transaction.hide(newFragmentWayang);
        }
        else{
            transaction.hide(newFragmentKomedian);
        }
        transaction.show(newFragment2);
        transaction.commit();
        newFragment2.setToolbar();
    }

//    private void setToolbar(String title){
//        mToolbar = (Toolbar) findViewById(R.id.organizer_main_toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(title);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OrganizerMainActivity","On Activity Result Main Activity");

    }
}
