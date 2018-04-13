package com.clairvoyance.bookmarket;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by Sathya on 12/21/2017.
 * Firebase Authentication and Database Handler
 */

class WebServiceHandler {

    private static final String SELL_BOOK_REF = "sellBooks";
    private static final String BUY_BOOK_REF = "buyBooks";
    private static final String REQUEST_REF = "requests";
    private static final String USER_REF = "users";
    private static final String UID_REF = "uid";
    private static final String DATE_REF = "postDateInSecs";

    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";
    private static FirebaseUser mUser;
    private static User loadedUser;

    private static DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    static Query allSellBooks = rootRef.child(SELL_BOOK_REF).orderByChild(DATE_REF).limitToFirst(100);
    static Query mySellBooks = rootRef.child(SELL_BOOK_REF).orderByChild(UID_REF).equalTo(getUID());
    static Query allBuyBooks = rootRef.child(BUY_BOOK_REF).orderByChild(DATE_REF).limitToFirst(100);
    static Query myBuyBooks = rootRef.child(BUY_BOOK_REF).orderByChild(UID_REF).equalTo(getUID());

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
        }
    }

    private static User loadMainUserData(final User user){
        // Load main data (and set listener) from database - if not already loaded
        if (loadedUser == null) {
            DatabaseReference userRef = rootRef.child(USER_REF).child(mUser.getUid());

            // Read Data
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        Log.d("MainActivityCycle", "mainUserSet");
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

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(USER_REF);
        userRef.child(mUser.getUid()).setValue(user);

    }

    static void updateMainUserData(User user) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            user.setRegistrationToken(FirebaseInstanceId.getInstance().getToken());
            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());
            userRef.setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
            // Todo: Implement Try-catch to send activities back to the Login Activity
        }
    }

    static String getUID(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if(mUser != null){
            return mUser.getUid();
        }
        return null;
    }

    static void updateToken() throws IllegalAccessException{
        if(isMainUserAuthenticated()){
            User user = generateMainUser();
            updateMainUserData(user);
        }
    }

    // This also updates books!
    static void addPublicBook(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference postRef = WebServiceHandler.rootRef.child(SELL_BOOK_REF).child(book.getBookID());
            postRef.setValue(book);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void addRequest(Request request) throws IllegalAccessException{
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference myRequestRef = rootRef.child(REQUEST_REF).child(request.getRequestID());
            myRequestRef.setValue(request);

        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void removeRequest(String requestID) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            rootRef.child(REQUEST_REF).child(requestID).removeValue();
        }
    }

    static void addSpam(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated()){
            rootRef.child("spam").child(book.getBookID()).setValue(getUID());
        }
    }

}
