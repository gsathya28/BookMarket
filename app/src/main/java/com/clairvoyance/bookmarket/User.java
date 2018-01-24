package com.clairvoyance.bookmarket;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Sathya on 12/21/2017.
 *
 */

@IgnoreExtraProperties
class User implements Serializable {
    private String name;
    private String uid;
    private String email;
    private HashMap<String, Object> bookIDs = new HashMap<>();
    private HashMap<String, Object> myRequestIDs = new HashMap<>();
    private HashMap<String, Object> publicRequestIDs = new HashMap<>();
    private boolean isEmailVerified = false;

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
    void addBook(String bookID) {bookIDs.put(bookID, true);}
    void addMyRequest(Request request){
        myRequestIDs.put(request.getRequestID(), true);
    }
    void addMyRequest(String requestID){
        myRequestIDs.put(requestID, true);
    }
    void addPublicRequest(Request request){
        publicRequestIDs.put(request.getRequestID(), true);
    }
    void addPublicRequest(String requestID){
        publicRequestIDs.put(requestID, true);
    }

    @Exclude
    String getUid() {
        return uid;
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
    HashMap<String, Object> getMyRequestIDs() {
        return myRequestIDs;
    }
    HashMap<String, Object> getPublicRequestIDs(){
        return publicRequestIDs;
    }

    // Todo: Still need to implement/override equals method if I plan on have local store of user data

}
