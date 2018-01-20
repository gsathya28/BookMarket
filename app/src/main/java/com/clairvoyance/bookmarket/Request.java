package com.clairvoyance.bookmarket;


import com.google.firebase.database.Exclude;

import java.util.UUID;

/**
 * Created by Sathya on 1/16/2018.
 *
 */

class Request {

    private String uid;
    private String bookID;
    private boolean accepted = false;
    private String requestID;

    Request(){}

    Request(String uid, String postID){
        // Todo: Check if IDS are valid ???
        this.requestID = UUID.randomUUID().toString();
        this.uid = uid;
        this.bookID = postID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    String getUid() {
        return uid;
    }

    public String getBookID() {
        return bookID;
    }

    // Actual Book Data will be loaded and displayed in the listener in the listener.

    String getRequestID() {
        return requestID;
    }
}
