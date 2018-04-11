package com.clairvoyance.bookmarket;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class ActMainActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener {

    User mainUser;

    ViewPager actViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        actViewPager = findViewById(R.id.main_pager);
        actViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        // Todo: Change this to be more general
        actViewPager.setCurrentItem(1);

        // Load Data
        setMainUser();
        loadPublicData();
    }

    private void setToolbar() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
    }

    private void setMainUser() {
        try {
            mainUser = WebServiceHandler.generateMainUser();
        } catch (IllegalAccessException i) {
            illegalAccess();
        }
    }

    private void loadPublicData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Book item) {
        // List Fragment stuff
    }

    private void illegalAccess(){

    }


































}
