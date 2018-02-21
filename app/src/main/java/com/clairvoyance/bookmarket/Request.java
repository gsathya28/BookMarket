package com.clairvoyance.bookmarket;

import java.util.UUID;

/**
 * Created by Sathya on 1/16/2018.
 *
 */

class Request {

    private String uid;
    private String requestorName;
    private String bookID;
    private String requesteeID;
    private boolean accepted = false;
    private String requestID;
    private String bookName;

    Request(){}

    Request(User user, Book book){
        // Todo: Check if IDS are valid ???
        this.requestID = UUID.randomUUID().toString();
        this.uid = WebServiceHandler.getUID();
        this.bookID = book.getBookID();
        this.requesteeID = book.getUid();
        this.requestorName = user.getName();
        this.bookName = book.getTitle();
    }

    public boolean isAccepted() {
        return accepted;
    }
    String getUid() {
        return uid;
    }
    String getBookID() {
        return bookID;
    }
    String getRequestID() {
        return requestID;
    }
    public String getRequesteeID() {
        return requesteeID;
    }
    public String getRequestorName() {
        return requestorName;
    }

    public String getBookName() {
        return bookName;
    }
}
