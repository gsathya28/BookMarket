package com.clairvoyance.bookmarket;

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
    private String name;
    private String email;
    private HashMap<String, Object> bookIDs = new HashMap<>();

    // Key is bookID!!! - Value is requestID!!!!
    private HashMap<String, String> myRequestIDs = new HashMap<>();
    private boolean isEmailVerified = false;
    private String registrationToken;

    public User(){ }

    User(String email){
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

    String getName() {
        return name;
    }
    String getEmail() {
        return email;
    }
    boolean isEmailVerified() {
        return isEmailVerified;
    }
    HashMap<String, Object> getBookIDs() {
        return bookIDs;
    }
    HashMap<String, String> getMyRequestIDs() {
        return myRequestIDs;
    }
    String getRegistrationToken() {
        return registrationToken;
    }
}
