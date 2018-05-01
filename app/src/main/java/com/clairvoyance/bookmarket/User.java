package com.clairvoyance.bookmarket;

import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Sathya on 12/21/2017.
 *
 */
// Todo: Implement Parcelable (for argument passing in bundles in fragments)
@IgnoreExtraProperties
class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private HashMap<String, Object> bookIDs = new HashMap<>();

    // Key is bookID!!! - Value is requestID!!!! (Todo: Refactor Name)
    private HashMap<String, String> myRequestIDs = new HashMap<>();
    private boolean isEmailVerified = false;
    private String registrationToken;

    @Deprecated
    public User(){ }

    User(String uid, String email){
        this.uid = uid;
        this.email = email;
    }

    void setName(String name) {
        this.name = name;
    }
    void setEmailVerified(boolean emailVerified) { isEmailVerified = emailVerified; }
    void addBook(Book book){
        bookIDs.put(book.getBookID(), true);
    }
    void addMyRequest(Request request){
        myRequestIDs.put(request.getBookID(), request.getRequestID());
    }
    void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public boolean isEmailVerified() {
        return isEmailVerified;
    }
    public HashMap<String, Object> getBookIDs() {
        return bookIDs;
    }
    public HashMap<String, String> getMyRequestIDs() {
        return myRequestIDs;
    }
    public String setRegistrationToken() {
        return registrationToken;
    }

    public String getUid() {
        return uid;
    }


}
