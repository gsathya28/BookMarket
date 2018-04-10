package com.clairvoyance.bookmarket;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActMainActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener{

    User mainUser;
    ViewPager viewPager;

    // Public Book Data
    ArrayList<Book> mSellBooks = new ArrayList<>();
    ArrayList<Book> mBuyBooks = new ArrayList<>();
    ValueEventListener mSellBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot d: dataSnapshot.getChildren()){
                Book book = d.getValue(Book.class);
                if (book != null){
                    mSellBooks.add(book);
                    mBuyBooks.add(book);
                }
            }
            viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), ActMainActivity.this, mSellBooks, mBuyBooks));
            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        viewPager = findViewById(R.id.sell_buy_pager);


        // Load Data
        setMainUser();
        loadPublicData();
        loadPrivateData();

        if(mSellBooks.size() == 0){
            Log.d("Book Data", "No Books Loaded!");
        }
    }

    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
    }

    private void setMainUser() {
        try {
            mainUser = WebServiceHandler.generateMainUser();
        }catch (IllegalAccessException i) {
            illegalAccess();
        }
    }

    private void loadPublicData(){

    }

    private void loadPrivateData(){
        try {
            Query query = WebServiceHandler.getRootRef().child("books").orderByChild("uid").equalTo(WebServiceHandler.getUID());
            query.addListenerForSingleValueEvent(mSellBookData);
        }
        catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void illegalAccess(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onListFragmentInteraction(Book item) {
        // List Fragment stuff
    }
}
