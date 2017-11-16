package com.artace.arthub;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.artace.arthub.ViewPlugins.DrawerMenu;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class OrganizerMainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    String title;
    OrganizerSenimanFragment newFragment1;
    OrganizerPortfolioFragment newFragment2;
    OrganizerEventsFragment newFragment3;
    OrganizerDiundangFragment newFragment4;

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("TITLE",title);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void bottomBarListeners(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.organizer_main_bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_seniman) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment1);

                    transaction.hide(newFragment2);
                    transaction.hide(newFragment3);
                    transaction.hide(newFragment4);

                    transaction.commit();
                }
                else if (tabId == R.id.tab_portfolio) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment2);

                    transaction.hide(newFragment1);
                    transaction.hide(newFragment3);
                    transaction.hide(newFragment4);

                    transaction.commit();
                }
                else if (tabId == R.id.tab_events) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment3);

                    transaction.hide(newFragment1);
                    transaction.hide(newFragment2);
                    transaction.hide(newFragment4);

                    transaction.commit();
                }
                else if (tabId == R.id.tab_diundang) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment4);
                    if (newFragment4.senimanList.size() == 0){
                        Toast.makeText(getApplicationContext() ,"Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                    newFragment4.getData();

                    transaction.hide(newFragment1);
                    transaction.hide(newFragment2);
                    transaction.hide(newFragment3);

                    transaction.commit();
                }
            }
        });
    }

//    private void setToolbar(String title){
//        mToolbar = (Toolbar) findViewById(R.id.organizer_main_toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(title);
//    }

    private void initFragment(Bundle savedInstanceState){
        if (findViewById(R.id.organizer_main_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            newFragment1 = new OrganizerSenimanFragment();
            newFragment2 = new OrganizerPortfolioFragment();
            newFragment3 = new OrganizerEventsFragment();
            newFragment4 = new OrganizerDiundangFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.organizer_main_fragment_container, newFragment1,"Events");

            transaction.add(R.id.organizer_main_fragment_container, newFragment2,"Portfolio");
            transaction.hide(newFragment2);
            transaction.add(R.id.organizer_main_fragment_container, newFragment3,"Seniman");
            transaction.hide(newFragment3);
            transaction.add(R.id.organizer_main_fragment_container, newFragment4,"Diundang");
            transaction.hide(newFragment4);
            transaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OrganizerMainActivity","On Activity Result Main Activity");

    }
}
