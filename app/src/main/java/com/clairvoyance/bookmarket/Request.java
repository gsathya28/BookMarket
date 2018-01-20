package com.clairvoyance.bookmarket;

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
    String getBookID() {return bookID;}
    String getRequestID() {
        return requestID;
    }

}
