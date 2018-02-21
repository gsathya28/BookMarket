package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class ActSellViewRequests extends AppCompatActivity {

    // Load Request Data and basic User/Book Data with the Request.
    // Todo: Finish View Requests Page

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
        Toolbar myToolbar = findViewById(R.id.sell_view_requests_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("Requests: ");
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
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
        if ((displayRequestIDs.indexOf(request.getRequestID()) == -1)){
            requests.add(request);
            addRequest(request);
        }
        else {
            updateRequest(request);
        }

    }

    private void updateRequest(Request request){
        // Add one to the index because
        int displayIndex = requestIDs.indexOf(request.getRequestID()) + 1;
        Button button = (Button) mainLayout.getChildAt(displayIndex);

        button.setText(request.getRequestorName());
    }

    private void addRequest(final Request request){
        displayRequestIDs.add(request.getRequestID());

        final Button button =  new Button(getApplicationContext());
        setButtonLayout(button);
        String buttonText = request.getRequestorName() + " requests your book: " + request.getBookName();
        button.setText(buttonText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(), ActSellViewRequest.class);
                intent.putExtra("requestID", request.getRequestID());
                startActivity(intent);
            }
        });
        mainLayout.addView(button);
    }

    private void removeRequestUI(String key){
        if (displayRequestIDs.contains(key)){
            int displayIndex = requestIDs.indexOf(key) + 1;
            requestIDs.remove(displayIndex - 1);
            displayRequestIDs.remove(displayIndex - 1);
            mainLayout.removeViewAt(displayIndex);
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
}
