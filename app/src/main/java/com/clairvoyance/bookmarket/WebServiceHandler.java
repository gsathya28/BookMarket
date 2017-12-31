package com.clairvoyance.bookmarket;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Amazon Web Service Handler
 */

class WebServiceHandler {


    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static User loadedUser;

    private static boolean isMainUserAuthenticated(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        return mUser != null;
    }

    @Nullable
    static User generateMainUser(){
        if (isMainUserAuthenticated()){
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
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    // Nothing special happens
                }
            });

            // Read Data
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return loadedUser;
    }

    static void updateMainUserData(User user){

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

    static ArrayList<BuyPost> getPublicBuyPosts(){
        ArrayList<BuyPost> buyPosts = new ArrayList<>();

        // Use AWS to generate posts and put it in an array

        return buyPosts;
    }


    static void addPublicPost(Post post){
        // Use AWS to add public post
    }
}
