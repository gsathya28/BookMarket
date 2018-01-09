package com.clairvoyance.bookmarket;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_main);

        mainUser = LocalDataHandler.parseMainUserData(getApplicationContext());
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
                Intent addPostIntent = new Intent(getApplicationContext(), ActSellAddPost.class);
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
        final LinearLayout mainLayout = findViewById(R.id.buyer_post_layout);

        Query postListRef = WebServiceHandler.mPublicPosts;
        ValueEventListener publicPostListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        Post post = d.getValue(Post.class);
                        post.setPostDate(post.getPostDateInSecs());

                        Button button = new Button(getApplicationContext());
                        setButtonLayout(button);

                        StringBuilder builder = new StringBuilder();
                        builder.append("Posted: ");
                        builder.append(DateFormat.getTimeInstance(DateFormat.SHORT).format(post.getPostDate().getTime()));
                        builder.append(" - ");
                        builder.append(DateFormat.getDateInstance().format(post.getPostDate().getTime()));
                        builder.append(System.getProperty("line.separator"));
                        String buttonText = button.getText().toString();
                        buttonText = buttonText + builder.toString();
                        button.setText(buttonText);

                        setButtonText(button, post);

                        mainLayout.addView(button);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        postListRef.addListenerForSingleValueEvent(publicPostListener);
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

    private void setButtonText(final Button button, final Post post){

        ArrayList<String> bookIds = post.getBookIDs();

        StringBuilder builder = new StringBuilder();
        builder.append("Books:");
        builder.append(System.getProperty("line.separator"));
        String text = button.getText() + builder.toString();
        button.setText(text);

        button.setTextColor(Color.parseColor("#FFFFFF"));

        // Book ID Code
        DatabaseReference bookList = FirebaseDatabase.getInstance().getReference().child("books");
        for (String id: bookIds){
            DatabaseReference bookRef = bookList.child(id);
            ValueEventListener getBookById = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Book book = dataSnapshot.getValue(Book.class);
                    post.addBook(book);
                    StringBuilder stringBuilder = new StringBuilder();
                    String courseSubj = book.get(Book.COURSE_SUBJECT);
                    String courseNum = book.get(Book.COURSE_NUMBER);
                    String bookName = book.get(Book.TITLE);

                    stringBuilder.append(courseSubj);
                    stringBuilder.append(" ");
                    stringBuilder.append(courseNum);
                    stringBuilder.append(" - ");
                    stringBuilder.append(bookName);
                    stringBuilder.append(System.getProperty("line.separator"));
                    String buttonText = button.getText().toString();
                    buttonText = buttonText + stringBuilder.toString();
                    button.setText(buttonText);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            bookRef.addListenerForSingleValueEvent(getBookById);
        }
    }

    @Override
    public void onBackPressed(){

    }
}
