package com.clairvoyance.bookmarket; // has been delivered.. bitch!

import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Firebase Authentication and Database Handler
 */

class WebServiceHandler {


    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static User loadedUser;

    // Value Event Listeners

    static Query mBooks = FirebaseDatabase.getInstance().getReference().child("books").orderByChild("postDateInSecs").limitToFirst(100);

    private static boolean isMainUserAuthenticated(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        return mUser != null;
    }

    @Nullable
    static User generateMainUser(){
        if (isMainUserAuthenticated()){
            // KeyLine
            User user = new User(mUser.getUid(), mUser.getEmail());

            user.setEmailVerified(mUser.isEmailVerified());
            user = loadMainUserData(user);
            return user;
        }
        else {
            return null; // Re-authentication intent set up in onCreate
        }
    }

    private static User loadMainUserData(final User user){

        if (loadedUser == null) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());

            // Read Data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Create new user - with values
                        createNewUserData(user);
                        loadedUser = user;
                    }
                    else {
                        loadedUser = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return loadedUser;
    }

    private static void createNewUserData(User user){
        if (isMainUserAuthenticated()){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
            userRef.child(mUser.getUid()).setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void updateMainUserData(User user){
        if (isMainUserAuthenticated()){
            DatabaseReference userRefList = FirebaseDatabase.getInstance().getReference().child("users");
            DatabaseReference userRef = userRefList.child(mUser.getUid());
            userRef.setValue(user);
        }
        else {
            throw new IllegalStateException("User not authorized");
        }
    }

    static void addPublicBook(Book book){
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("books");
            postRef.child(book.getBookID()).setValue(book);
        }
    }

    static void addRequest(Request request){
        if (isMainUserAuthenticated()) { // Add function to only allow certain people to post
            DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("requests");
            requestRef.child(request.getRequestID()).setValue(request);

        }
    }

}
