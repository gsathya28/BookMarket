package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActSellViewRequests extends AppCompatActivity {


    User mainUser;

    ArrayList<Request> requests = new ArrayList<>();
    ValueEventListener requestDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Request request = dataSnapshot.getValue(Request.class);
            if (request != null){
                requests.add(request);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_view_requests);

        mainLayout = findViewById(R.id.sell_request_layout);

        setToolbar();
        setMainUser();
    }

    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.sell_view_requests_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("Requests: ");
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setMainUser(){
        try {
            mainUser = WebServiceHandler.generateMainUser();
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void setButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );



        button.setLayoutParams(params);
        button.setGravity(Gravity.START);
        button.setGravity(Gravity.CENTER_VERTICAL);

        int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        button.setSingleLine();
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setPadding(valueInPx, button.getPaddingTop(), valueInPx, button.getPaddingBottom());
        button.setBackgroundColor(Color.parseColor("#267326"));
    }

    private void illegalAccess(){
        Intent intent = new Intent(this, ActLoginActivity.class);
        startActivity(intent);
    }

}
