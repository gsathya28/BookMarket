package com.clairvoyance.bookmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActSellAddBook extends AppCompatActivity {

    User mainUser;
    ArrayList<Book> postBooks = new ArrayList<>();
    View dialogLayout;
    TextView noBookText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sell_add_post);
        noBookText = findViewById(R.id.sell_default_no_book);

        setToolbar();
        setMainUser();
        setButtons();
    }

    private void setToolbar(){
        Toolbar myToolbar = findViewById(R.id.add_post_toolbar);
        myToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        myToolbar.setTitle(R.string.Add_Post);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setMainUser() {
        String uid = WebServiceHandler.getUID();
        if (uid == null) {
            illegalAccess();
            return;
        }

        DatabaseReference userRef = WebServiceHandler.getRootRef().child("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    illegalAccess();
                } else {
                    // This works even after the initial data read since loadedUser's pointer is returned at the end of the method.
                    Log.d("MainActivityCycle", "mainUserSet");
                    mainUser = dataSnapshot.getValue(User.class);
                    if (mainUser != null) {
                        // Set up the GUI now that the mainUser is set (we'll need its data)
                        setButtons();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setButtons(){

        // Add Book Button
        final Button addBook = findViewById(R.id.sell_add_book_button);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // New Dialog allowing you to enter Book Details
                final AlertDialog newBookDialog = newBookDialog();

                // OnShowListener - that will set OnClickListeners for Buttons in Dialog
                newBookDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = newBookDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                        // Listener for Dialog Positive Button to create Book Object and stored temporarily in Activity (postBooks)
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String courseType = ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).getText().toString();
                                String courseNumber = ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).getText().toString();

                                Book newBook;

                                if (courseType.equals("") && courseNumber.equals("")){
                                    Toast.makeText(getApplicationContext(), "Both Course Subject and Number required", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                try{
                                    newBook = new Book(courseType, courseNumber);
                                }catch (IllegalAccessException i){
                                    illegalAccess();
                                    return; // Don't know why I need this...
                                }

                                String bookTitle = ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).getText().toString();
                                String author = ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).getText().toString();
                                String price = ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).getText().toString();
                                String vnum = ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).getText().toString();
                                String notes = ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).getText().toString();

                                newBook.set(Book.TITLE, bookTitle);
                                newBook.set(Book.AUTHOR, author);
                                newBook.set(Book.PRICE, price);
                                newBook.set(Book.VERSION_NUMBER, vnum);
                                newBook.set(Book.NOTES, notes);

                                postBooks.add(newBook);
                                addToList(newBook);
                                newBookDialog.dismiss();
                            }
                        });
                    }
                });
                newBookDialog.show();
            }
        });

        // Post Button
        Button postButton = findViewById(R.id.sell_post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add books to database that are in temporary store (postBooks)
                for(Book book: postBooks){
                    mainUser.addBook(book);
                    try{
                        WebServiceHandler.addPublicBook(book);
                    }catch (IllegalAccessException i){
                        illegalAccess();
                    }
                }

                try{
                    WebServiceHandler.updateMainUserData(mainUser);
                }catch (IllegalAccessException i){
                    illegalAccess();
                }

                // Go back to the Sell-MainActivity
                Intent savePost = new Intent(getApplicationContext(), ActMainActivity.class);
                startActivity(savePost);
            }
        });
    }

    private void setButtonLayout(Button bookButton){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);


        params.setMargins(leftValueInPx, topValueInPx, leftValueInPx, bottomValueInPx);
        bookButton.setLayoutParams(params);

        bookButton.setBackgroundColor(Color.parseColor("#267326"));
    }

    private void addToList(final Book book){
        final LinearLayout listLayout = findViewById(R.id.sell_add_book_list);

        // Remove "No Books" View
        if (listLayout.getChildAt(0).equals(findViewById(R.id.sell_default_no_book))){
            listLayout.removeViewAt(0);
        }

        // Load a button to GUI
        final Button bookButton = new Button(this);
        listLayout.addView(bookButton, 0);
        setButtonLayout(bookButton);

        // Set text using the Book object
        String courseSubj = book.get(Book.COURSE_SUBJECT);
        String courseNum = book.get(Book.COURSE_NUMBER);
        String bookName = book.get(Book.TITLE);
        String buttonText = courseSubj + " " + courseNum + " - " + bookName;
        bookButton.setText(buttonText);
        bookButton.setTextColor(Color.parseColor("#FFFFFF"));

        // Set Listener to load Dialog to update/change book data.
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog editBookDialog = editBookDialog();

                // Load current data into dialog
                ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).setText(book.get(Book.COURSE_SUBJECT));
                ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).setText(book.get(Book.COURSE_NUMBER));
                ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).setText(book.get(Book.TITLE));
                ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).setText(book.get(Book.AUTHOR));
                ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).setText(book.get(Book.PRICE));
                ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).setText(book.get(Book.VERSION_NUMBER));
                ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).setText(book.get(Book.NOTES));

                // Handle Buttons (w/ Listeners) in dialog
                editBookDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialo3gInterface) {
                        Button editButton = editBookDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                        // Save Book Data back into postBooks - (Temporary Activity Store)
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String courseType = ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).getText().toString();
                                String courseNumber = ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).getText().toString();

                                Book newBook;

                                if (courseType.equals("") && courseNumber.equals("")){
                                    Toast.makeText(getApplicationContext(), "Both Course Subject and Number required", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                try {
                                    newBook = new Book(courseType, courseNumber);
                                }catch (IllegalAccessException i){
                                    illegalAccess();
                                    return;
                                }

                                String bookTitle = ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).getText().toString();
                                String author = ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).getText().toString();
                                String price = ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).getText().toString();
                                String vnum = ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).getText().toString();
                                String notes = ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).getText().toString();

                                newBook.set(Book.TITLE, bookTitle);
                                newBook.set(Book.AUTHOR, author);
                                newBook.set(Book.PRICE, price);
                                newBook.set(Book.VERSION_NUMBER, vnum);
                                newBook.set(Book.NOTES, notes);

                                postBooks.remove(book);
                                postBooks.add(newBook);
                                addToList(newBook);
                                deleteBookFromList(bookButton);
                                editBookDialog.dismiss();
                            }
                        });

                        // Delete Book from Temporary Store
                        Button deleteButton = editBookDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postBooks.remove(book);
                                deleteBookFromList(bookButton);
                                editBookDialog.dismiss();
                            }
                        });
                    }
                });
                editBookDialog.show();
            }
        });
    }

    private void deleteBookFromList(Button bookButton){
        final LinearLayout listLayout = findViewById(R.id.sell_add_book_list);

        // Check if button is part of layout before deleting
        if (listLayout.indexOfChild(bookButton) != -1)
            listLayout.removeView(bookButton);

        // Add the "No Books" text if there are no books left.
        if (postBooks.size() == 0){
            listLayout.addView(noBookText, 0);
        }
    }

    private AlertDialog newBookDialog(){
        // Set Dialog Layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.add_book_dialog_layout, null);
        builder.setView(dialogLayout);
        builder.setTitle("New Book");

        // Add Buttons (onClickListeners added when onShowListener is added to AlertDialog after it is returned)
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    private AlertDialog editBookDialog(){
        // Set Dialog Layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.add_book_dialog_layout, null);
        builder.setView(dialogLayout);

        // Add Buttons (onClickListeners added when onShowListener is added to AlertDialog after it is returned)
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("DELETE", null);
        return builder.create();
    }

    private void illegalAccess(){
        Intent intent = new Intent(this, ActLoginActivity.class);
        startActivity(intent);
    }

}