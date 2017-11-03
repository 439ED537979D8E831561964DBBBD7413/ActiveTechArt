package com.artace.arthub;

import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class OrganizerMainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_main);

        if (getIntent() != null){
            Bundle extras = getIntent().getExtras();
            title = extras.getString("TITLE");
            setToolbar(title);
        }
        else{
            title = savedInstanceState.getString("TITLE");
            setToolbar(title);
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
                if (tabId == R.id.tab_events) {
                    OrganizerEventsFragment newFragment = new OrganizerEventsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.organizer_main_fragment_container, newFragment);
                    transaction.commit();
                }
                else if (tabId == R.id.tab_portfolio) {
                    OrganizerPortfolioFragment newFragment = new OrganizerPortfolioFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.organizer_main_fragment_container, newFragment);
                    transaction.commit();
                }
                else if (tabId == R.id.tab_seniman) {
                    OrganizerSenimanFragment newFragment = new OrganizerSenimanFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.organizer_main_fragment_container, newFragment);
                    transaction.commit();
                }
                else if (tabId == R.id.tab_diundang) {
                    OrganizerDiundangFragment newFragment = new OrganizerDiundangFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.organizer_main_fragment_container, newFragment);
                    transaction.commit();
                }
            }
        });
    }

    private void setToolbar(String title){
        mToolbar = (Toolbar) findViewById(R.id.organizer_main_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(title);
    }

    private void initFragment(Bundle savedInstanceState){
        if (findViewById(R.id.organizer_main_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            OrganizerEventsFragment eventsFragment = new OrganizerEventsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            eventsFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.organizer_main_fragment_container, eventsFragment).commit();
        }
    }

}
