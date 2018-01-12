package com.clairvoyance.bookmarket;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ActSellViewPosts extends AppCompatActivity {

    User mainUser;
    ArrayList<String> bookIDs = new ArrayList<>();
    ArrayList<String> displayedBooks = new ArrayList<>();
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_view_posts);

        mainUser = WebServiceHandler.generateMainUser();
        mainLayout = findViewById(R.id.sell_my_post_layout);
        setToolbar();
        setLayout();
    }

    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.view_posts_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle("Your Posts");
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setLayout(){

        Set keys = mainUser.getBookIDs().keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
            if (object instanceof String){
                bookIDs.add((String) object);
            }
        }

        for (final String id: bookIDs){

            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(id);
            ValueEventListener getData = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Book book = dataSnapshot.getValue(Book.class);
                    // Todo: Possibly compare User-BookKey with Book-stored Key
                    if (book != null){
                        if (displayedBooks.contains(book.getBookID())){
                            int index = displayedBooks.indexOf(book.getBookID());
                            // Run Update Code (index, book)
                            updateBookInUI(index, book);
                        }
                        else {
                            // Run Add code
                            addBookToUI(book);
                            displayedBooks.add(book.getBookID());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            bookRef.addValueEventListener(getData);
        }
    }

    private void addBookToUI(final Book book){
        Button infoButton = new Button(getApplicationContext());
        setButtonLayout(infoButton);

        int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
        infoButton.setText(buttonText);
        infoButton.setBackgroundColor(Color.parseColor("#267326"));
        infoButton.setSingleLine();
        infoButton.setEllipsize(TextUtils.TruncateAt.END);
        infoButton.setPadding(valueInPx, infoButton.getPaddingTop(), valueInPx, infoButton.getPaddingBottom());
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBookDialog(book).show();
            }
        });

        mainLayout.addView(infoButton);
    }

    private void updateBookInUI(int index, final Book book){
        Button infoButton = (Button) mainLayout.getChildAt(index);
        mainLayout.removeViewAt(index);
        String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
        infoButton.setText(buttonText);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBookDialog(book).show();
            }
        });

        mainLayout.addView(infoButton, index);
    }

    private void deleteBookInUI(int index, final Book book){
        mainLayout.removeViewAt(index);
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
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setBackgroundColor(Color.parseColor("#267326"));
    }

    private AlertDialog viewBookDialog(final Book book){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellViewPosts.this);
        builder.setTitle(book.getTitle());
        // Todo: Fix Dialog Options
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
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Todo: Edit Post

            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Load Delete Post Dialog!
                deleteCheckDialog(book).show();
            }
        });

        builder.setNeutralButton("Cancel", null);

        return builder.create();
    }

    private AlertDialog deleteCheckDialog(final Book book){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellViewPosts.this);
        builder.setMessage("Are you sure do you want to delete this post?");
        builder.setTitle("Delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Todo: Delete Code - and exit out
                // In Books!
                String id = book.getBookID();
                DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(id);
                bookRef.removeValue();

                // In User Book Ids
                HashMap<String, Object> UserBookIds = mainUser.getBookIDs();
                UserBookIds.remove(id);
                WebServiceHandler.updateMainUserData(mainUser);

                // Update UI
                int index = displayedBooks.indexOf(book.getBookID());
                displayedBooks.remove(index);
                deleteBookInUI(index, book);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewBookDialog(book).show();
            }
        });

        return builder.create();
    }


}
