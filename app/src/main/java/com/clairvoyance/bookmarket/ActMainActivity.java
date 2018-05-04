package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class ActMainActivity extends AppCompatActivity implements BookListFragment.OnListFragmentInteractionListener {

    User mainUser;
    ViewPager mainActViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        initMainActViewPager();
        setMainActViewPagerGUIAdapter();
    }

    private void setToolbar() {
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
    }

    private void initMainActViewPager(){
        mainActViewPager = findViewById(R.id.main_pager);
    }

    private void setMainActViewPagerGUIAdapter() {

        String uid;
        try{
            uid = FirebaseHandler.getUID();
        }catch (IllegalAccessException iae){
            illegalAccess();
            return;
        }
        setViewPagerAfterUserDataCollection(uid);

    }

    void setViewPagerAfterUserDataCollection(String uid){
        DatabaseReference userRef = FirebaseHandler.getRootRef().child("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    illegalAccess();
                } else {
                    // This works even after the initial data read since loadedUser's pointer is returned at the end of the method.
                    Log.d("MainActivityCycle", "mainUserSet");
                    mainUser = dataSnapshot.getValue(User.class);
                    if (mainUser == null) {
                        mainUser = FirebaseHandler.createNewUserData();
                    }
                    setViewPagerWithUserData(mainUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setViewPagerWithUserData(User mainUser){
        mainActViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mainUser));
        mainActViewPager.setCurrentItem(1);
    }

    @Override
    public void onListFragmentInteraction(Book item) {
        // List Fragment stuff
    }

    public void illegalAccess() {
        Intent intent = new Intent(ActMainActivity.this, ActLoginActivity.class);
        startActivity(intent);
    }
}