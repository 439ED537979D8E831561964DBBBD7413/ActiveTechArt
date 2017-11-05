package com.artace.arthub;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.artace.arthub.ViewPlugins.DrawerMenu;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class SenimanMainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    String title;
    SenimanHomeProfileFragment newFragment1;
    SenimanListTawaranFragment newFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seniman_main);

        mToolbar = (Toolbar) findViewById(R.id.seniman_main_toolbar);
        setSupportActionBar(mToolbar);

        DrawerMenu drawer = new DrawerMenu();
        drawer.createDrawer(this,this, mToolbar);


        initFragment(savedInstanceState);
        bottomBarListeners();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("TITLE",title);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    private void bottomBarListeners(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.seniman_main_bottombar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_seniman_profile) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment1);

                    transaction.hide(newFragment2);
                    transaction.commit();
                }
                else if (tabId == R.id.tab_seniman_event) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.show(newFragment2);

                    transaction.hide(newFragment1);
                    transaction.commit();
                }
            }
        });
    }

    private void setToolbar(String title){
        mToolbar = (Toolbar) findViewById(R.id.seniman_main_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }

    private void initFragment(Bundle savedInstanceState){
        if (findViewById(R.id.seniman_main_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            newFragment1 = new SenimanHomeProfileFragment();
            newFragment2 = new SenimanListTawaranFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.seniman_main_fragment_container, newFragment1);

            transaction.add(R.id.seniman_main_fragment_container, newFragment2);
            transaction.hide(newFragment2);
            transaction.commit();
        }
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
