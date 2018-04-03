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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ActSellSearchResults extends AppCompatActivity {

    User mainUser;
    ArrayList<Book> resultBooks = new ArrayList<>();
    // Single Value Event - so no need for edit checks
    ValueEventListener searchBooks = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Book book = dataSnapshot.getValue(Book.class);
            if(book != null){
                resultBooks.add(book);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    HashMap<String, String> bookRequests = new HashMap<>();

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_search_results);

        mainLayout = findViewById(R.id.sell_view_book_list);

        setToolbar();
        setMainUser();
        loadQuery();
        setLayout();
    }

    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.search_results_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("Search Results");
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

    private void loadQuery(){
        Log.d("LifeCycle", "loadQuery");
        Book searchBook = (Book) (getIntent().getSerializableExtra("queryBook"));

        if(searchBook != null) {
            Log.d("LifeCycle", "setQuery");
            Log.d("LifeCycle", "books " + searchBook.getCourseTotal());
            Query searchQuery = WebServiceHandler.getRootRef().child("books").equalTo("courseTotal", searchBook.getCourseTotal());
            searchQuery.addListenerForSingleValueEvent(searchBooks);
        }

        bookRequests = mainUser.getMyRequestIDs();

    }

    private void setLayout(){

        // Remove all View since there is a new Book ArrayList in place
        mainLayout.removeAllViews();
        Log.d("Book Count", Integer.toString(resultBooks.size()));
        // Funnel through the data placing new Horizontal LinearLayout (to hold info and request buttons) for each book
        try {
            for (final Book book : resultBooks) {

                if (book.getUid().equals(WebServiceHandler.getUID())) {
                    continue;
                }

                if (book.isSpam()) {
                    continue;
                }

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

                if (bookRequests.containsKey(book.getBookID())) {
                    reqButton.setChecked(true);
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
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void setInfoButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        button.setLayoutParams(params);
        button.setGravity(Gravity.START);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setTextColor(Color.WHITE);
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

    private void checkedConditional(ToggleButton reqButton, boolean isChecked, Book book){
        if (isChecked){
            addRequest(book);
        }
        else {

            // Check if requestID exists Todo: Need to check if requestID is valid
            String requestID = bookRequests.get(book.getBookID());
            if (requestID == null || requestID.equals("")){
                throw new IllegalStateException("Invalid GUI Request ID Values!!!!");
            }

            // Add Delete? Dialog - to prevent accidental request removal -
            AlertDialog dialog = removeRequestDialog(book, requestID, reqButton);
            dialog.show();
        }
    }

    private void addRequest(Book bookRequested){
        // NO need to add to bookRequests, since bookRequests is a pointer AND it will update the Firebase Database
        try {
            Request request = new Request(mainUser, bookRequested);
            WebServiceHandler.addRequest(request);

            bookRequested.addRequestID(request);
            WebServiceHandler.addPublicBook(bookRequested);

            mainUser.addMyRequest(request);
            WebServiceHandler.updateMainUserData(mainUser);
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void deleteRequest(Book book, String requestID){
        // NO need to delete to bookRequests, since bookRequests is a pointer (mainUser.getMyRequestIDs) AND it will update the Firebase Database
        WebServiceHandler.getRootRef().child("requests").child(requestID).removeValue();

        try {
            book.removeRequestID(requestID);
            WebServiceHandler.addPublicBook(book);

            mainUser.getMyRequestIDs().remove(book.getBookID());
            WebServiceHandler.updateMainUserData(mainUser);
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private AlertDialog viewBookDialog(final Book book, final ToggleButton reqButton){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellSearchResults.this);
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
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Author: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getAuthor());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Version Number: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getVersionNumber());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Instructor: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getInstructor());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("Notes: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getNotes());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        String infoText = stringBuilder.toString();
        TextView infoTextView = new TextView(ActSellSearchResults.this);
        infoTextView.setText(infoText);

        LinearLayout linearLayout = new LinearLayout(ActSellSearchResults.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(infoTextView);

        if(!book.isSpam()){
            final Button button = new Button(ActSellSearchResults.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            button.setText("Flag");
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    book.setSpam(true);
                    try {
                        WebServiceHandler.addSpam(book);
                        WebServiceHandler.addPublicBook(book);
                    }catch (IllegalAccessException i){
                        illegalAccess();
                    }
                    button.setVisibility(View.GONE);
                }
            });
            linearLayout.addView(button);
        }

        builder.setView(linearLayout);

        // If a request is not made for this book - button will prompt to make request
        // and the listener will set the toggleButton to isChecked which will run code to add request to database
        if (!reqButton.isChecked()) {
            builder.setPositiveButton("Add to Request List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reqButton.setChecked(true);
                    viewBookDialog(book, reqButton).show();
                }
            });
        }
        // Else, the button will prompt to un-request, and the listener will load a dialog to make sure the un-request is intended.
        else {
            builder.setPositiveButton("Un-request", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String requestID = bookRequests.get(book.getBookID());
                    if (requestID == null || requestID.equals("")){
                        throw new IllegalStateException("Invalid GUI Request ID Values!!!!");
                    }

                    AlertDialog dialog = removeRequestDialog(book, requestID, reqButton);
                    dialog.show();
                }
            });
        }
        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    private AlertDialog removeRequestDialog(final Book book, final String requestID, final ToggleButton button){

        button.setOnCheckedChangeListener(null);
        button.setChecked(true);
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkedConditional(button, button.isChecked(), book);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellSearchResults.this);
        builder.setTitle("Remove Request?");
        builder.setMessage("Are you sure you want to remove your request for this book?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRequest(book, requestID);
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

    private void illegalAccess(){
        Intent intent = new Intent(this, ActLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        WebServiceHandler.cutUserDataListener();
        super.onDestroy();
    }
}
