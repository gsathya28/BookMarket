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

class FirebaseHandler {

    private static final String REQUEST_REF = "requests";
    private static final String USER_REF = "users";
    private static final String UID_REF = "uid";
    private static final String DATE_REF = "postDateInSecs";

    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";

    private static DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    static DatabaseReference getRootRef() {
        return rootRef;
    }

    private static FirebaseUser getFirebaseUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private static boolean isMainUserAuthenticated(FirebaseUser user) {
        return user != null;
    }

    static User createNewUserData() {
        FirebaseUser mUser = getFirebaseUser();
        User user = new User(mUser.getUid(), mUser.getEmail());
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(USER_REF);
        userRef.child(mUser.getUid()).setValue(user);
        return user;
    }

    static void updateMainUserData(User user) throws IllegalAccessException{
        FirebaseUser mUser = getFirebaseUser();
        if (isMainUserAuthenticated(mUser)){
            user.setRegistrationToken(FirebaseInstanceId.getInstance().getToken());
            DatabaseReference userRef = rootRef.child("users").child(mUser.getUid());
            userRef.setValue(user);
        }
        else {
            throw new IllegalAccessException("User not authorized");
        }
    }

    static String getUID() throws IllegalAccessException{
        FirebaseUser mUser = getFirebaseUser();
        if(isMainUserAuthenticated(mUser)){
            return mUser.getUid();
        }
        else{
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void setToken(final String token) throws IllegalAccessException{

        String uid = getUID();
        DatabaseReference userRef = getRootRef().child(USER_REF).child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mainUser = dataSnapshot.getValue(User.class);
                if(mainUser != null){
                    mainUser.setRegistrationToken(token);
                    dataSnapshot.getRef().setValue(mainUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static void updatePublicBook(Book book) throws IllegalAccessException{
        if(isMainUserAuthenticated(getFirebaseUser())){
            DatabaseReference postRef = FirebaseHandler.rootRef.child(book.getType()).child(book.getBookID());
            postRef.setValue(book);
        }
        else {
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void addPublicBook(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated(getFirebaseUser())) {
            book.setUid(getFirebaseUser().getUid());
            DatabaseReference postRef = FirebaseHandler.rootRef.child(book.getType()).child(book.getBookID());
            postRef.setValue(book);
        }
        else {
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void removePublicBook(Book book) throws IllegalAccessException{
        if(isMainUserAuthenticated(getFirebaseUser())){
            String id = book.getBookID();
            DatabaseReference bookRef = rootRef.child(book.getType()).child(id);
            bookRef.removeValue();
        }else{
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void addRequest(Request request) throws IllegalAccessException{
        if (isMainUserAuthenticated(getFirebaseUser())) { // Add function to only allow certain people to post
            DatabaseReference myRequestRef = rootRef.child(REQUEST_REF).child(request.getRequestID());
            myRequestRef.setValue(request);

        }else{
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void removeRequest(String requestID) throws IllegalAccessException{
        if (isMainUserAuthenticated(getFirebaseUser())){
            rootRef.child(REQUEST_REF).child(requestID).removeValue();
        }else{
            throw new IllegalAccessException("User not authorized");
        }
    }

    static void addSpam(Book book) throws IllegalAccessException{
        if (isMainUserAuthenticated(getFirebaseUser())){
            rootRef.child("spam").child(book.getBookID()).setValue(getUID());
        }else{
            throw new IllegalAccessException("User not authorized");
        }
    }

    static Query getBookListQuery(String type) throws IllegalAccessException{
        FirebaseUser mUser = getFirebaseUser();
        if(isMainUserAuthenticated(mUser)){
            switch (type){
                case Book.ALL_BOOK_SELL:
                    return rootRef.child(Book.SELL_BOOK).orderByChild(DATE_REF).limitToFirst(100);
                case Book.ALL_BOOK_BUY:
                    return rootRef.child(Book.BUY_BOOK).orderByChild(DATE_REF).limitToFirst(100);
                case Book.MY_BOOK_SELL:
                    return rootRef.child(Book.SELL_BOOK).orderByChild(UID_REF).equalTo(mUser.getUid());
                case Book.MY_BOOK_BUY:
                    return rootRef.child(Book.BUY_BOOK).orderByChild(UID_REF).equalTo(mUser.getUid());
                default:
                    throw new IllegalArgumentException("Wrong Query Type Parameter - Use Book Class Query Strings");
            }
        }else{
            throw new IllegalAccessException("User not authorized");
        }
    }

}
