package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class ActSellViewRequests extends AppCompatActivity {

    // Load Request Data and basic User/Book Data with the Request.

    User mainUser;
    ArrayList<String> requestIDs = new ArrayList<>();
    ArrayList<String> displayRequestIDs = new ArrayList<>();
    ArrayList<Request> requests = new ArrayList<>();
    ValueEventListener requestDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            Request request = dataSnapshot.getValue(Request.class);
            if (request != null){
                requests.add(request);
                updateRequestUI(request);
            }
            else{
                removeRequestUI(key);
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
        loadData();
    }

    private void setToolbar(){

    }

    private void setRequestLayout(LinearLayout layout){

    }

    private void loadData(){

        mainUser = WebServiceHandler.generateMainUser();
        if (mainUser == null){
            Intent intent = new Intent(getApplicationContext(), ActLoginActivity.class);
            startActivity(intent);
        }

        Set keys = mainUser.getPublicRequestIDs().keySet();
        for (Object object: keys){
            if (object instanceof String){
                requestIDs.add((String) object);
            }
        }

        // Set Database Listeners (and put in arrayList)
        for (String requestID: requestIDs){
            DatabaseReference requestRef = WebServiceHandler.getRootRef().child("requests").child(requestID);
            requestRef.addValueEventListener(requestDataListener);
        }
    }

    private void updateRequestUI(Request request){
        addRequest(request);
    }

    private void updateRequest(Request request){

    }

    private void addRequest(Request request){
        displayRequestIDs.add(request.getRequestID());

        final Button button =  new Button(getApplicationContext());
        setButtonLayout(button);
        button.setText(request.getRequestorName());
        button.setBackgroundColor(Color.parseColor("#267326"));
        mainLayout.addView(button);
    }

    private void removeRequestUI(String key){

    }

    private void setButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        button.setLayoutParams(params);
        button.setGravity(Gravity.START);
        button.setGravity(Gravity.CENTER_VERTICAL);
    }


}
