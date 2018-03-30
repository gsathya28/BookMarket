package com.clairvoyance.bookmarket;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Sathya on 12/22/2017.
 * Custom Book Variable
 */

@IgnoreExtraProperties
class Book implements Serializable {

    private String title;
    private String price;
    private String author;
    private String notes;
    private String courseSubj;
    private String courseNumber;
    private String courseTotal;
    private String versionNumber;
    private String instructor;
    private String bookID;
    private String uid;
    private long postDateInSecs;
    private boolean isSpam = false;
    private HashMap<String, Boolean> requestIDs = new HashMap<>();
    private ArrayList<Request> requests = new ArrayList<>();

    static final int TITLE = 0;
    static final int PRICE = 1;
    static final int AUTHOR = 2;
    static final int NOTES = 3;
    static final int COURSE_SUBJECT = 4;
    static final int COURSE_NUMBER = 5;
    static final int VERSION_NUMBER = 6;
    static final int INSTRUCTOR = 7;

    public Book(){
        // Firebase Constructor (required)
    }

    // Only courseSubj and courseNumber are required
    public Book(String courseSubj, String courseNumber) throws IllegalAccessException{
        this.courseSubj = courseSubj;
        this.courseNumber = courseNumber;
        this.courseTotal = courseSubj + courseNumber;
        bookID = UUID.randomUUID().toString();
        postDateInSecs = Calendar.getInstance().getTimeInMillis();
        this.uid = WebServiceHandler.getUID();
    }

    void set(int field, String value){
        if (value == null){
            return;
        }

        switch (field){
            case COURSE_NUMBER:
                this.courseNumber = value;
                return;
            case COURSE_SUBJECT:
                this.courseSubj = value;
            case TITLE:
                this.title = value;
                return;
            case PRICE:
                this.price = value;
                return;
            case AUTHOR:
                this.author = value;
                return;
            case NOTES:
                this.notes = value;
                return;
            case VERSION_NUMBER:
                this.versionNumber = value;
                return;
            case INSTRUCTOR:
                this.instructor = value;
                return;
            default:
                throw new IllegalArgumentException("Invalid Parameter");
        }
    }

    void setSpam(boolean spam) {
        isSpam = spam;
    }

    void addRequestID(Request request){
        requestIDs.put(request.getRequestID(), true);
    }

    void removeRequestID(String requestID){
        requestIDs.remove(requestID);
    }

    void addRequestList(ArrayList<Request> requests){
        this.requests = requests;
    }

    HashMap<String, Boolean> getRequestIDs(){
        return requestIDs;
    }

    String get(int field){
        switch (field){
            case TITLE:
                return this.title;
            case PRICE:
                return this.price;
            case AUTHOR:
                return this.author;
            case NOTES:
                return this.notes;
            case COURSE_SUBJECT:
                return this.courseSubj;
            case COURSE_NUMBER:
                return this.courseNumber;
            case VERSION_NUMBER:
                return this.versionNumber;
            case INSTRUCTOR:
                return this.instructor;
            default:
                throw new IllegalArgumentException("Invalid Parameter");
        }
    }

    // Firebase-Required Getters
    String getTitle() {
        return title;
    }
    String getAuthor() {
        return author;
    }
    String getCourseSubj() {
        return courseSubj;
    }
    String getCourseNumber() {
        return courseNumber;
    }
    String getCourseTotal() {
        return courseTotal;
    }
    String getPrice() {
        return price;
    }
    String getVersionNumber() {
        return versionNumber;
    }
    String getInstructor() {
        return instructor;
    }
    public String getNotes() {
        return notes;
    }
    long getPostDateInSecs() {
        return postDateInSecs;
    }
    String getBookID() {
        return bookID;
    }
    String getUid() {
        return uid;
    }
    boolean isSpam() {
        return isSpam;
    }

    @Exclude
    ArrayList<Request> getRequests(){
        return requests;
    }

}