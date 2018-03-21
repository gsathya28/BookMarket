package com.clairvoyance.bookmarket;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sathya on 12/21/2017.
 * Firebase Authentication and Database Handler
 */

class WebServiceHandler {


    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";
    private static FirebaseUser mUser;
    private static User loadedUser;

    private static DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    static Query mBooks = rootRef.child("books").orderByChild("postDateInSecs").limitToFirst(100);

    static DatabaseReference getRootRef() {
        return rootRef;
    }

    private static boolean isMainUserAuthenticated() throws IllegalAccessException{
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null){
            throw new IllegalAccessException("User not authorized");
        }
        else{
            return true;
        }
    }

    @Nullable
    static User generateMainUser() throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            User user = new User(mUser.getEmail());
            user.setEmailVerified(mUser.isEmailVerified());
            user.setName(mUser.getDisplayName());
            // Key-line
            user = loadMainUserData(user);
            return user;
        }
        else {
            throw new IllegalStateException("Main User not Authenticated");
            // Todo: Throw Exception to send back to Login Activity
        }
    }

    private static User loadMainUserData(final User user){
        // Load main data (and set listener) from database - if not already loaded
        if (loadedUser == null) {

            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());

            // Read Data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Create new user in database
                        createNewUserData(user);
                        // Loaded User is whatever it is now since there is no previous data to load
                        loadedUser = user;
                    }
                    else {
                        // This works even after the initial data read since loadedUser's pointer is returned at the end of the method.
                        loadedUser = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return loadedUser; // This will be null at first since the ValueEventListener only puts the data in this pointer after all the code (onCreate) has run
    }

    private static void createNewUserData(User user) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.child(mUser.getUid()).setValue(user);

    }

    // Add/Update Data down below!
    // Todo: Some sort of trigger needs to run when a book is added - so correct notifications are given. (Either here, database backend (lambda), or when data is read in (background))
    // For above task-to-do: We can try to use some sort of state counter/dataset with each state showing what DB edits were made in a certain time frame

    static void updateMainUserData(User user) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());
            userRef.setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
            // Todo: Implement Try-catch to send activities back to the Login Activity
        }
    }

    static String getUID() throws IllegalAccessException{
        if(isMainUserAuthenticated()){
            return mUser.getUid();
        }
        else{
            return "";
        }
    }

    // This also updates books!
    static void addPublicBook(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference postRef = WebServiceHandler.rootRef.child("books").child(book.getBookID());
            postRef.setValue(book);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void addRequest(Request request) throws IllegalAccessException{
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference myRequestRef = rootRef.child("requests").child(request.getRequestID());
            myRequestRef.setValue(request);

        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void removeRequest(String requestID) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            rootRef.child("requests").child(requestID).removeValue();
        }
    }

    static void addSpam(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            rootRef.child("spam").child(book.getBookID()).setValue(getUID());
        }
    }

}
