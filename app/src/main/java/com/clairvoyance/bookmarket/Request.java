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
    private String bookCourse;
    private String bookType;

    Request(){}

    Request(User user, Book book) {
        this.requestID = UUID.randomUUID().toString();
        this.uid = user.getUid();
        this.bookID = book.getBookID();
        this.requesteeID = book.getUid();
        this.requesterName = user.getName();
        this.requesterEmail = user.getEmail();
        this.bookCourse = book.getCourseTotal();
        this.requestTime = Calendar.getInstance().getTimeInMillis();
        this.bookType = book.getType();
    }

    public String getUid() {
        return uid;
    }
    public String getBookID() {
        return bookID;
    }
    public String getRequestID() {
        return requestID;
    }
    public String getRequesteeID() {
        return requesteeID;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public String getRequesterEmail() {return requesterEmail;}
    public String getBookCourse() {
        return bookCourse;
    }
    public double getRequestTime() {return requestTime;}
    public String getBookType() {
        return bookType;
    }
}
