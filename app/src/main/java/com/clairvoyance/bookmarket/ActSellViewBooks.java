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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ActSellViewBooks extends AppCompatActivity {

    User mainUser;
    ValueEventListener getBookData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Book book = dataSnapshot.getValue(Book.class);
            // Todo: Possibly compare User-BookKey with Book-stored Key
            if (book != null){
                loadRequestData(book);
                if (displayedBookIDs.contains(book.getBookID())){
                    int index = displayedBookIDs.indexOf(book.getBookID());
                    // Run Update Code (index, book)
                    updateBookInList(index, book);
                }
                else {
                    // Run Add code
                    addBookToList(book);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    ArrayList<String> bookIDs = new ArrayList<>();
    ArrayList<String> displayedBookIDs = new ArrayList<>();
    ArrayList<Book> mBooks = new ArrayList<>();

    LinearLayout mainLayout;
    View dialogLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_view_posts);
        mainLayout = findViewById(R.id.sell_my_post_layout);

        setToolbar();
        setMainUser();
        loadData();
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

    private void setMainUser(){
        try {
            mainUser = WebServiceHandler.generateMainUser();
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void loadData(){

        Set keys = mainUser.getBookIDs().keySet();

        for (Object object: keys){
            if (object instanceof String){
                bookIDs.add((String) object);
            }
        }

        for (final String id: bookIDs){
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(id);
            bookRef.addValueEventListener(getBookData);
        }
    }

    /**
        @param book - final - Book that is added to the main Book List - to update layout

        Adds book to displayedBooksList in order of when it was
        made (not when it was edited... Notifications will solve any edit problems)
     */
    private void addBookToList(final Book book){

        boolean added = false;
        // Push the most recent book up first
        for (int i = 0; i < mBooks.size(); i++){
            if(book.getPostDateInSecs() > mBooks.get(i).getPostDateInSecs()){
                mBooks.add(i, book);
                added = true;
                break;
            }
        }

        // End case if it is the least recent book published - and wasn't added
        if(!added){
            mBooks.add(book);
        }

        displayedBookIDs = new ArrayList<>();
        for (Book listBook: mBooks){
            displayedBookIDs.add(listBook.getBookID());
        }


        setMainLayout();
    }

    private void updateBookInList(int index, final Book book){

        LinearLayout infoLayout = (LinearLayout) mainLayout.getChildAt(index);
        mainLayout.removeViewAt(index);

        Button infoButton = (Button) infoLayout.getChildAt(0);

        String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
        infoButton.setText(buttonText);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBookDialog(book).show();
            }
        });

        mainLayout.addView(infoLayout, index);

    }

    private void deleteBookInUI(int index){
        mBooks.remove(index);
        mainLayout.removeViewAt(index);
    }

    // This is called whenever a book is added or updated (so when a request is made, the book data is updated so this should work - fingers crossed)
    private void loadRequestData(Book book){
        final ArrayList<Request> requests = new ArrayList<>();
        ArrayList<String> requestIDs = new ArrayList<>();
        Set requestIDSet = book.getRequestIDs().keySet();

        for (Object object: requestIDSet){
            if (object instanceof String){
                requestIDs.add((String) object);
            }
        }

        for (String id: requestIDs){

            DatabaseReference requestRef = WebServiceHandler.getRootRef().child("requests").child(id);
            ValueEventListener requestListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Request request = dataSnapshot.getValue(Request.class);
                    requests.add(request);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            requestRef.addListenerForSingleValueEvent(requestListener);
        }
        book.addRequestList(requests);
    }

    private void setButtonLayout(Button button){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        int valueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(leftValueInPx, topValueInPx, leftValueInPx / 2, bottomValueInPx);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setBackgroundColor(Color.parseColor("#267326"));

        button.setSingleLine();
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setPadding(valueInPx, button.getPaddingTop(), valueInPx, button.getPaddingBottom());
    }

    private void setReqButtonLayout(Button button){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                3.0f
        );

        int topValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int leftValueInPx = (int) getApplicationContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(leftValueInPx, topValueInPx, leftValueInPx / 2, bottomValueInPx);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);

        button.setSingleLine();
        button.setEllipsize(TextUtils.TruncateAt.END);
    }

    private void setMainLayout(){
        mainLayout.removeAllViews();
        for (final Book book: mBooks){

            LinearLayout singleBookLayout = new LinearLayout(getApplicationContext());
            singleBookLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Info Button
            Button infoButton = new Button(getApplicationContext());
            setButtonLayout(infoButton);
            String buttonText = book.getCourseSubj() + " " + book.getCourseNumber() + " - " + book.getTitle();
            infoButton.setText(buttonText);
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewBookDialog(book).show();
                }
            });

            // Requests Button
            Button reqButton = new Button(getApplicationContext());
            setReqButtonLayout(reqButton);
            reqButton.setText("Req.");
            reqButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookRequestsDialog(book).show();
                }
            });

            singleBookLayout.addView(infoButton);
            singleBookLayout.addView(reqButton);

            mainLayout.addView(singleBookLayout);
        }
    }

    private AlertDialog viewBookDialog(final Book book){

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellViewBooks.this);
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

        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final AlertDialog editBookDialog = editBookDialog();

                ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).setText(book.get(Book.COURSE_SUBJECT));
                ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).setText(book.get(Book.COURSE_NUMBER));
                ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).setText(book.get(Book.TITLE));
                ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).setText(book.get(Book.AUTHOR));
                ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).setText(book.get(Book.PRICE));
                ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).setText(book.get(Book.VERSION_NUMBER));
                ((EditText) dialogLayout.findViewById(R.id.sell_instructor_text)).setText(book.get(Book.INSTRUCTOR));
                ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).setText(book.get(Book.NOTES));

                editBookDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialo3gInterface) {

                        Button editButton = editBookDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String courseType = ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).getText().toString();
                                String courseNumber = ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).getText().toString();

                                if (courseType.equals("") && courseNumber.equals("")){
                                    Toast.makeText(getApplicationContext(), "Both Course Subject and Number required", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                book.set(Book.COURSE_SUBJECT, courseType);
                                book.set(Book.COURSE_NUMBER, courseNumber);

                                String bookTitle = ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).getText().toString();
                                String author = ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).getText().toString();
                                String price = ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).getText().toString();
                                String vnum = ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).getText().toString();
                                String instructor = ((EditText) dialogLayout.findViewById(R.id.sell_instructor_text)).getText().toString();
                                String notes = ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).getText().toString();

                                book.set(Book.TITLE, bookTitle);
                                book.set(Book.AUTHOR, author);
                                book.set(Book.PRICE, price);
                                book.set(Book.VERSION_NUMBER, vnum);
                                book.set(Book.NOTES, notes);
                                book.set(Book.INSTRUCTOR, instructor);

                                // Save Data -
                                DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(book.getBookID());
                                bookRef.setValue(book);

                                editBookDialog.dismiss();
                                viewBookDialog(book).show();
                            }
                        });

                        Button deleteButton = editBookDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editBookDialog.dismiss();
                                viewBookDialog(book).show();
                            }
                        });
                    }
                });

                editBookDialog.show();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellViewBooks.this);
        builder.setMessage("Are you sure do you want to delete this post?");
        builder.setTitle("Delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // In Books!
                String id = book.getBookID();
                DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(id);
                bookRef.removeValue();

                // In User Book Ids
                HashMap<String, Object> UserBookIds = mainUser.getBookIDs();
                UserBookIds.remove(id);

                // Delete requests attached to the book
                HashMap<String, Boolean> requestIDs = book.getRequestIDs();
                Set keySet = requestIDs.keySet();
                try {

                    for (Object object : keySet) {
                        if (object instanceof String) {
                            WebServiceHandler.removeRequest((String) object);
                        }
                    }
                    WebServiceHandler.updateMainUserData(mainUser);

                } catch (IllegalAccessException ie){
                    illegalAccess();
                }

                // Update UI
                int index = displayedBookIDs.indexOf(book.getBookID());
                displayedBookIDs.remove(index);
                deleteBookInUI(index);

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

    private AlertDialog editBookDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        dialogLayout = inflater.inflate(R.layout.add_book_dialog_layout, null);

        builder.setView(dialogLayout);
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    private AlertDialog bookRequestsDialog(Book book){
        AlertDialog.Builder builder = new AlertDialog.Builder(ActSellViewBooks.this);
        builder.setTitle("Requests on your book");

        LinearLayout mainRequestLayout = new LinearLayout(ActSellViewBooks.this);
        mainRequestLayout.setOrientation(LinearLayout.VERTICAL);

        // Todo: Sort requests in descending order (most recent first)

        ArrayList<Request> requests = book.getRequests();
        for (Request request: requests){

            LinearLayout requestLayout = new LinearLayout(ActSellViewBooks.this);
            requestLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView requestInfo = new TextView(ActSellViewBooks.this);
            String requestString = request.getRequesterName() + "\n" + request.getRequesterEmail();
            requestInfo.setText(requestString);

            requestLayout.addView(requestInfo);

            Button acceptButton = new Button(ActSellViewBooks.this);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActSellViewBooks.this, ActViewMessages.class);
                    startActivity(intent);
                }
            });
        }

        builder.setView(mainRequestLayout);
        builder.setPositiveButton("OK", null);

        return builder.create();
    }

    private void illegalAccess(){
        Intent intent = new Intent(this, ActLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        for (final String id: bookIDs){
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books").child(id);
            bookRef.removeEventListener(getBookData);
        }
        super.onDestroy();
    }
}