package com.clairvoyance.bookmarket;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Sathya on 1/16/2018.
 *
 */

class Request {

    private String uid;
    private String requesterName;
    private String requesterEmail;
    private String bookID;
    private String requesteeID;
    private String requestID;
    private double requestTime;
    private String bookName;

    Request(){}

    Request(User user, Book book){
        // Todo: Check if IDS are valid ???
        this.requestID = UUID.randomUUID().toString();
        this.uid = WebServiceHandler.getUID();
        this.bookID = book.getBookID();
        this.requesteeID = book.getUid();
        this.requesterName = user.getName();
        this.requesterEmail = user.getEmail();
        this.bookName = book.getTitle();
        this.requestTime = Calendar.getInstance().getTimeInMillis();
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
    String getRequesteeID() {
        return requesteeID;
    }
    String getRequesterName() {
        return requesterName;
    }
    String getRequesterEmail() {return requesterEmail;}
    String getBookName() {
        return bookName;
    }
    double getRequestTime() {return requestTime;}
}
