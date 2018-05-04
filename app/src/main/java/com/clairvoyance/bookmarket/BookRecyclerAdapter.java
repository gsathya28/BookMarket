package com.clairvoyance.bookmarket;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.clairvoyance.bookmarket.BookListFragment.OnListFragmentInteractionListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final String mType;
    private final User mainUser;
    private View dialogLayout;
    private final Query query;

    private ChildEventListener childTracker = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Book book = dataSnapshot.getValue(Book.class);
            if(book != null){
                if(!(book.getUid().equals(mainUser.getUid()))){
                    mValues.add(0, book);
                    notifyItemInserted(0);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Book changedBook = dataSnapshot.getValue(Book.class);
            if(changedBook != null) {
                for (Book book : mValues) {
                    if (changedBook.getBookID().equals(book.getBookID())) {
                        book = changedBook;
                        int index = mValues.indexOf(book);
                        notifyItemChanged(index);
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Book removedBook = dataSnapshot.getValue(Book.class);
            if(removedBook != null){
                for (Book book : mValues){
                    if (removedBook.getBookID().equals(book.getBookID())) {
                        int index = mValues.indexOf(book);
                        mValues.remove(index);
                        notifyItemRemoved(index);
                    }
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private final HashMap<String, String> bookRequests;

    BookRecyclerAdapter(List<Book> items, String type, User user) throws IllegalAccessException {
        mValues = items;
        mType = type; // Todo: Check for type validity (precautionary)
        mainUser = user;
        bookRequests = mainUser.getMyRequestIDs();

        query = FirebaseHandler.getBookListQuery(mType);
        if(mType.equals(Book.MY_BOOK_SELL) || mType.equals(Book.MY_BOOK_BUY)) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mValues.clear();
                    for(DataSnapshot d: dataSnapshot.getChildren()){
                        Book book = d.getValue(Book.class);
                        if (book != null){
                            mValues.add(book);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.ALL_BOOK_BUY)) {
            mValues.clear();
            notifyDataSetChanged();
            query.addChildEventListener(childTracker);
        }
        Log.d("QueryDetacher", "Query Attached!" + " " + mType);
    }

    @Override
    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = parent;
        if(mType.equals(Book.MY_BOOK_SELL) || mType.equals(Book.MY_BOOK_BUY)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fragment_book, parent, false);
        }
        else if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.ALL_BOOK_BUY)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_fragment_book, parent, false);
        }
        return new ViewHolder(view);
    }

    private void setInfoButtonLayout(Button infoButton){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int bottomValueInPx = (int) infoButton.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int valueInPx = (int) infoButton.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        if(mType.equals(Book.MY_BOOK_SELL) || mType.equals(Book.MY_BOOK_BUY)){
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomValueInPx*2);
        }

        infoButton.setGravity(Gravity.START);
        infoButton.setGravity(Gravity.CENTER_VERTICAL);
        infoButton.setTextColor(Color.WHITE);
        if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.MY_BOOK_SELL)) {
            infoButton.setBackgroundColor(Color.parseColor("#2aa22a"));
        }else if(mType.equals(Book.ALL_BOOK_BUY) || mType.equals(Book.MY_BOOK_BUY)){
            infoButton.setBackgroundColor(Color.parseColor("#3385ff"));
        }
        infoButton.setSingleLine();
        infoButton.setEllipsize(TextUtils.TruncateAt.END);
        infoButton.setPadding(valueInPx, infoButton.getPaddingTop(), valueInPx, infoButton.getPaddingBottom());
        infoButton.setLayoutParams(params);
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

    private void setBookListLayout(LinearLayout bookLayout){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int topValueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        int bottomValueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_vertical_margin);
        bottomValueInPx = bottomValueInPx / 2;
        int valueInPx = (int) bookLayout.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);

        params.setMargins(params.leftMargin, topValueInPx, valueInPx/2, bottomValueInPx);

        bookLayout.setGravity(Gravity.CENTER_VERTICAL);
        bookLayout.setOrientation(LinearLayout.HORIZONTAL);
        bookLayout.setLayoutParams(params);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        String buttonText = holder.mItem.getCourseSubj() + " " + holder.mItem.getCourseNumber() + " - " + holder.mItem.getTitle();
        if(holder.mView instanceof Button){ // Personal Book
            Button button = (Button) holder.mView;
            button.setText(buttonText);
            setInfoButtonLayout(button);
            final Book book = holder.mItem;
            final Context context = holder.mView.getContext();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewMyBookDialog(book, context).show();
                }
            });
        }
        else if(holder.mView instanceof LinearLayout){ // Public Book

            LinearLayout bookLayout = (LinearLayout) holder.mView;
            bookLayout.removeAllViews();
            setBookListLayout(bookLayout);

            Context context = bookLayout.getContext();

            final ToggleButton reqButton = new ToggleButton(context);
            bookLayout.addView(reqButton);
            setReqButtonLayout(reqButton);

            Button infoButton = new Button(context);
            infoButton.setText(buttonText);
            bookLayout.addView(infoButton);
            setInfoButtonLayout(infoButton);

            // Check if the book is already requested by the User
            final Book book = holder.mItem;
            if(bookRequests.containsKey(book.getBookID())){
                reqButton.setChecked(true);
            }

            reqButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    checkedConditional(reqButton, isChecked, book);
                }
            });

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPublicBookDialog(book, reqButton).show();
                }
            });
        }
    }

    // Private Book Functions
    private AlertDialog viewMyBookDialog(final Book book, final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

        stringBuilder.append("Notes: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getNotes());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final AlertDialog editBookDialog = editBookDialog(context);

                if(book.getType().equals(Book.SELL_BOOK)){
                    ((RadioButton) dialogLayout.findViewById(R.id.radioSell)).setChecked(true);
                }else if(book.getType().equals(Book.BUY_BOOK)){
                    ((RadioButton) dialogLayout.findViewById(R.id.radioBuy)).setChecked(true);
                }

                ((EditText) dialogLayout.findViewById(R.id.sell_course_type_text)).setText(book.get(Book.COURSE_SUBJECT));
                ((EditText) dialogLayout.findViewById(R.id.sell_course_number_text)).setText(book.get(Book.COURSE_NUMBER));
                ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).setText(book.get(Book.TITLE));
                ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).setText(book.get(Book.AUTHOR));
                ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).setText(book.get(Book.PRICE));
                ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).setText(book.get(Book.VERSION_NUMBER));
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
                                    Toast.makeText(context, "Both Course Subject and Number required", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                book.set(Book.COURSE_SUBJECT, courseType);
                                book.set(Book.COURSE_NUMBER, courseNumber);

                                String bookTitle = ((EditText) dialogLayout.findViewById(R.id.sell_book_title_text)).getText().toString();
                                String author = ((EditText) dialogLayout.findViewById(R.id.sell_author_text)).getText().toString();
                                String price = ((EditText) dialogLayout.findViewById(R.id.sell_price_text)).getText().toString();
                                String vnum = ((EditText) dialogLayout.findViewById(R.id.sell_vnum_text)).getText().toString();
                                String notes = ((EditText) dialogLayout.findViewById(R.id.sell_book_notes_text)).getText().toString();

                                book.set(Book.TITLE, bookTitle);
                                book.set(Book.AUTHOR, author);
                                book.set(Book.PRICE, price);
                                book.set(Book.VERSION_NUMBER, vnum);
                                book.set(Book.NOTES, notes);

                                // Save Data -
                                try {
                                    FirebaseHandler.updatePublicBook(book);
                                }catch (IllegalAccessException iae){
                                    illegalAccess();
                                }

                                int index = mValues.indexOf(book);
                                notifyItemChanged(index);
                                editBookDialog.dismiss();
                                viewMyBookDialog(book, context).show();
                            }
                        });

                        Button deleteButton = editBookDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editBookDialog.dismiss();
                                viewMyBookDialog(book, context).show();
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
                deleteCheckDialog(book, context).show();
            }
        });

        builder.setNeutralButton("Cancel", null);

        return builder.create();
    }

    private AlertDialog deleteCheckDialog(final Book book, final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure do you want to delete this post?");
        builder.setTitle("Delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // In Books!
                try {
                    FirebaseHandler.removePublicBook(book);
                }catch (IllegalAccessException iae){
                    illegalAccess();
                }
                // In User Book Ids
                String id = book.getBookID();
                HashMap<String, Object> UserBookIds = mainUser.getBookIDs();
                UserBookIds.remove(id);

                // Delete requests attached to the book
                HashMap<String, Boolean> requestIDs = book.getRequestIDs();
                Set keySet = requestIDs.keySet();
                try {
                    for (Object object : keySet) {
                        if (object instanceof String) {
                            FirebaseHandler.removeRequest((String) object);
                        }
                    }
                    FirebaseHandler.updateMainUserData(mainUser);

                } catch (IllegalAccessException ie){
                    illegalAccess();
                }


                int index = mValues.indexOf(book);
                mValues.remove(index);
                notifyItemRemoved(index);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewMyBookDialog(book, context).show();
            }
        });

        return builder.create();
    }

    private AlertDialog editBookDialog(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        dialogLayout = inflater.inflate(R.layout.add_book_dialog_layout, null);

        RadioGroup radioGroup = dialogLayout.findViewById(R.id.sell_buy_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioSell){
                    dialogLayout.setBackgroundColor(Color.parseColor("#c2efc2"));
                }else if(i == R.id.radioBuy){
                    dialogLayout.setBackgroundColor(Color.parseColor("#cce0ff"));
                }
            }
        });

        builder.setView(dialogLayout);
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    // Public Book Functions
    private AlertDialog viewPublicBookDialog(final Book book, final ToggleButton reqButton){

        AlertDialog.Builder builder = new AlertDialog.Builder(reqButton.getContext());
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

        stringBuilder.append("Notes: ");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(book.getNotes());
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(System.getProperty("line.separator"));

        String infoText = stringBuilder.toString();
        TextView infoTextView = new TextView(reqButton.getContext());
        infoTextView.setText(infoText);

        LinearLayout linearLayout = new LinearLayout(reqButton.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(infoTextView);

        if(!book.isSpam()){
            final Button button = new Button(reqButton.getContext());
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
                        FirebaseHandler.addSpam(book);
                        FirebaseHandler.updatePublicBook(book);
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
                    viewPublicBookDialog(book, reqButton).show();
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
            FirebaseHandler.addRequest(request);

            bookRequested.addRequestID(request);
            FirebaseHandler.updatePublicBook(bookRequested);

            mainUser.addMyRequest(request);
            FirebaseHandler.updateMainUserData(mainUser);
        }catch (IllegalAccessException i){
            illegalAccess();
        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(button.getContext());
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

    private void deleteRequest(Book book, String requestID){
        // NO need to delete to bookRequests, since bookRequests is a pointer (mainUser.getMyRequestIDs) AND it will update the Firebase Database
        FirebaseHandler.getRootRef().child("requests").child(requestID).removeValue();

        try {
            book.removeRequestID(requestID);
            FirebaseHandler.updatePublicBook(book);

            mainUser.getMyRequestIDs().remove(book.getBookID());
            FirebaseHandler.updateMainUserData(mainUser);
        }catch (IllegalAccessException i){
            illegalAccess();
        }
    }

    private void illegalAccess(){

    }

    public void removeListener(){
        if(mType.equals(Book.ALL_BOOK_SELL) || mType.equals(Book.ALL_BOOK_BUY)) {
            if (query != null) {
                query.removeEventListener(childTracker);
                Log.d("QueryDetacher", "Query Detached!" + " " + mType);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        Book mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
