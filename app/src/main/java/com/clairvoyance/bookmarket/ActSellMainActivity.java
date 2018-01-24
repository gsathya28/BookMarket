package com.clairvoyance.bookmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

public class ActSellMainActivity extends AppCompatActivity {

    // Firebase Data Transfer Variables
    User mainUser;
    Query bookListRef = WebServiceHandler.mBooks;
    ValueEventListener bookDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Add Book Objects in to arrayList
            books = new ArrayList<>();
            for (DataSnapshot d: dataSnapshot.getChildren()){
                books.add(d.getValue(Book.class));
            }
            // Layout is loaded only after all the data is loaded from the database
            updateUI();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
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

    // Local Machine Data
    ArrayList<Book> books;
    ArrayList<String> requestIDs = new ArrayList<>();
    ArrayList<Request> requests = new ArrayList<>();
    ArrayList<DatabaseReference> requestRefs = new ArrayList<>();

    // GUI Variables
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_main);

        mainLayout = findViewById(R.id.buyer_post_layout);
        setToolbar();
        setOptionButtons();
        setMainUser();
        loadData();

        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
    }

    // Toolbar Methods - setToolbar, onCreateOptionsMenu, onOptionsItemSelected
    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("Books People Want: ");
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sell_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search_event:
                // This will lead to an activity (GUI) that will Search/Scan
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMainUser(){
        mainUser = WebServiceHandler.generateMainUser();
        if (mainUser == null){
            Intent intent = new Intent(this, ActLoginActivity.class);
            startActivity(intent);
        }
    }

    // Set Listeners for Option Buttons generated at the Bottom of the layout
    private void setOptionButtons(){
        Button addBookButton = findViewById(R.id.sell_add_post);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPostIntent = new Intent(getApplicationContext(), ActSellAddBook.class);
                startActivity(addPostIntent);
            }
        });

        Button viewBooksButton = findViewById(R.id.sell_view_posts);
        viewBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPostsIntent = new Intent(getApplicationContext(), ActSellViewPosts.class);
                startActivity(viewPostsIntent);
            }
        });

        Button viewRequestsButton = findViewById(R.id.sell_view_requests);
        viewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewRequestsIntent = new Intent(getApplicationContext(), ActSellViewRequests.class);
                startActivity(viewRequestsIntent);
            }
        });
    }

    // Main Data Load from Database
    private void loadData(){
        // Get Request Data - note Listeners are triggered for sure once, but only after all the code has run in OnCreate
        // Get Keys for Request IDs - to see what the user has requested already
        Set keys = mainUser.getMyRequestIDs().keySet();

        for (Object object: keys){
            if (object instanceof String){
                requestIDs.add((String) object);
            }
        }

        // Set Database Listeners for Request Objects - so Request Data is ready when updateGUI() is called.
        for (String requestID: requestIDs){
            DatabaseReference requestRef = WebServiceHandler.getRootRef().child("requests").child(requestID);
            requestRef.addValueEventListener(requestDataListener);
            requestRefs.add(requestRef);
        }

        // Set Database Listener for Public Book List
        bookListRef.addValueEventListener(bookDataListener);
    }

    private void setInfoButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        button.setLayoutParams(params);
        button.setGravity(Gravity.START);
        button.setGravity(Gravity.CENTER_VERTICAL);

    }

    private void setBookLayout(LinearLayout bookLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(params.leftMargin, topValueInPx, valueInPx/2, bottomValueInPx);

        bookLayout.setGravity(Gravity.CENTER_VERTICAL);
        bookLayout.setOrientation(LinearLayout.HORIZONTAL);
        bookLayout.setLayoutParams(params);
    }

    private void setReqButtonLayout(ToggleButton reqButton){
        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        reqButton.setLayoutParams(checkParams);
        reqButton.setText(R.string.request);
        reqButton.setTextOff("Request");
        reqButton.setTextOn("Unrequest");
    }

    // Actual Data Incorporation - all of it runs after onCreate!
    private void updateUI(){
        // Remove all View since there is a new Book ArrayList in place
        mainLayout.removeAllViews();

        // Funnel through the data placing new Horizontal LinearLayout (to hold info and request buttons) for each book
        for (final Book book: books){
            LinearLayout bookLayout = new LinearLayout(getApplicationContext());
            setBookLayout(bookLayout);

            int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

            // Set layout for Buttons in BookLayout
            final ToggleButton reqButton = new ToggleButton(getApplicationContext());
            bookLayout.addView(reqButton);
            Button infoButton = new Button(getApplicationContext());
            bookLayout.addView(infoButton);
            setInfoButtonLayout(infoButton);
            setReqButtonLayout(reqButton);


            // Check if there's a pending request on the book by the user.
            for (Request request: requests){
                if (book.getBookID().equals(request.getBookID())){
                    reqButton.setChecked(true);
                    book.setGUIRequestID(request.getRequestID());
                    break;
                }
            }

            // This is really important - adds and deletes request data when checked/unchecked
            reqButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    checkedConditional(reqButton, isChecked, book);
                }
            });

            // Set Button Text
            String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
            infoButton.setText(buttonText);
            infoButton.setBackgroundColor(Color.parseColor("#267326"));
            infoButton.setSingleLine();
            infoButton.setEllipsize(TextUtils.TruncateAt.END);
            infoButton.setPadding(valueInPx, infoButton.getPaddingTop(), valueInPx, infoButton.getPaddingBottom());
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Set Info Dialog (different from Delete? Dialog)
                    viewBookDialog(book, reqButton).show();
                }
            });

            mainLayout.addView(bookLayout);
        }
    }

    private AlertDialog viewBookDialog(final Book book, final ToggleButton reqButton){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellMainActivity.this);
        builder.setTitle(book.getTitle());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Book Title");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getTitle());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Course: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getCourseSubj());
        stringBuilder.append(" ");
        stringBuilder.append(book.getCourseNumber());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Price: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("$");
        stringBuilder.append(book.getPrice());

        builder.setMessage(stringBuilder.toString());

        // If a request is not made for this book - button will prompt to make request
        // and the listener will set the toggleButton to isChecked which will run code to add request to database
        if (!reqButton.isChecked()) {
            builder.setPositiveButton("Add to Request List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reqButton.setChecked(true);
                }
            });
        }
        // Else, the button will prompt to un-request, and the listener will load a dialog to make sure the un-request is intended.
        else {
            builder.setPositiveButton("Un-request", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String GUIRequestID = book.getGUIRequestID();
                    if (GUIRequestID == null || GUIRequestID.equals("")){
                        throw new IllegalStateException("Invalid GUI Request ID Values!!!!");
                    }

                    AlertDialog dialog = removeRequestDialog(book, book.getGUIRequestID(), reqButton);
                    dialog.show();
                }
            });
        }
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    private AlertDialog removeRequestDialog(final Book book, final String requestID, final ToggleButton button){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellMainActivity.this);
        builder.setTitle("Remove Request?");
        builder.setMessage("Are you sure you want to remove your request for this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRequest(requestID);
                button.setOnCheckedChangeListener(null);
                button.setChecked(false);
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        checkedConditional(button, button.isChecked(), book);
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (book != null){
                    // To prevent the triggering of code in the OnCheckedChangeListener
                    button.setOnCheckedChangeListener(null);
                    button.setChecked(true);
                    button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            checkedConditional(button, button.isChecked(), book);
                        }
                    });
                }
            }
        });
        return builder.create();
    }

    private void addRequest(Book bookRequested){
        Request request = new Request(mainUser, bookRequested);
        mainUser.addMyRequest(request);
        WebServiceHandler.updateMainUserData(mainUser);
        bookRequested.setGUIRequestID(request.getRequestID());
        WebServiceHandler.addRequest(request);
    }

    private void deleteRequest(String requestID){
        WebServiceHandler.getRootRef().child("requests").child(requestID).removeValue();
        mainUser.getMyRequestIDs().remove(requestID);
        WebServiceHandler.updateMainUserData(mainUser);
    }

    private void checkedConditional(ToggleButton reqButton, boolean isChecked, Book book){
        if (isChecked){
            addRequest(book);
        }
        else {

            // Check if requestID exists Todo: Need to check if requestID is valid
            String GUIRequestID = book.getGUIRequestID();
            if (GUIRequestID == null || GUIRequestID.equals("")){
                throw new IllegalStateException("Invalid GUI Request ID Values!!!!");
            }

            // Add Delete? Dialog - to prevent accidental request removal -
            AlertDialog dialog = removeRequestDialog(book, book.getGUIRequestID(), reqButton);
            dialog.show();
        }
    }

    // Disable Back Button
    @Override
    public void onBackPressed(){

    }

    // Remove ValueEventListeners when Activity is destroyed - to prevent memory leaks.
    @Override
    public void onDestroy(){
        super.onDestroy();
        bookListRef.removeEventListener(bookDataListener);
        // For loop to remove every ValueEventListener for requests
        for (DatabaseReference requestRef: requestRefs){
            requestRef.removeEventListener(requestDataListener);
        }
    }
}