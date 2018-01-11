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
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActSellMainActivity extends AppCompatActivity {

    User mainUser;
    ArrayList<Book> books;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_main);

        mainUser = WebServiceHandler.generateMainUser();
        if (mainUser == null){
            Intent intent = new Intent(this, ActLoginActivity.class);
            startActivity(intent);
        }
        mainLayout = findViewById(R.id.buyer_post_layout);
        setToolbar();
        setOptionButtons();
        setLayout();
    }

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

    private void setOptionButtons(){
        Button addPostButton = findViewById(R.id.sell_add_post);
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPostIntent = new Intent(getApplicationContext(), ActSellAddBook.class);
                startActivity(addPostIntent);
            }
        });

        Button viewPostsButton = findViewById(R.id.sell_view_posts);
        viewPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPostsIntent = new Intent(getApplicationContext(), ActSellViewPosts.class);
                startActivity(viewPostsIntent);
            }
        });
    }

    private void setLayout(){

        Query bookListRef = WebServiceHandler.mBooks;
        ValueEventListener bookDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books = new ArrayList<>();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    books.add(d.getValue(Book.class));
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
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

    private void setReqButtonLayout(Button reqButton){
        LinearLayout.LayoutParams checkParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        reqButton.setLayoutParams(checkParams);
        reqButton.setText("Request");
    }


    private void updateUI(){
        mainLayout.removeAllViews();
        for (final Book book: books){
            LinearLayout bookLayout = new LinearLayout(getApplicationContext());
            setBookLayout(bookLayout);

            int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

            Button reqButton = new Button(getApplicationContext());
            bookLayout.addView(reqButton);
            Button infoButton = new Button(getApplicationContext());
            bookLayout.addView(infoButton);

            setInfoButtonLayout(infoButton);
            setReqButtonLayout(reqButton);

            String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
            infoButton.setText(buttonText);
            infoButton.setBackgroundColor(Color.parseColor("#267326"));
            infoButton.setSingleLine();
            infoButton.setEllipsize(TextUtils.TruncateAt.END);
            infoButton.setPadding(valueInPx, infoButton.getPaddingTop(), valueInPx, infoButton.getPaddingBottom());
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Set Dialog!
                    viewBookDialog(book).show();
                }
            });

            mainLayout.addView(bookLayout);
        }
    }

    private AlertDialog viewBookDialog(Book book){

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
        builder.setPositiveButton("Add to Request List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Todo: check the checkbox!

            }
        });

        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    @Override
    public void onBackPressed(){

    }

}
