package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
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
        myToolbar.setTitle(R.string.sell_main_layout_title);
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

    private void setButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
        button.setLayoutParams(params);
        button.setGravity(Gravity.START);
        button.setBackgroundColor(Color.parseColor("#267326"));

    }

    private void updateUI(){
        for (Book book: books){
            Button button = new Button(getApplicationContext());
            setButtonLayout(button);

            button.setText(book.getTitle());
            mainLayout.addView(button);
        }
    }

    @Override
    public void onBackPressed(){

    }
}
