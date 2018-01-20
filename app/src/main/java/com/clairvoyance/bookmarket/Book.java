package com.clairvoyance.bookmarket;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Sathya on 12/22/2017.
 *
 */
@IgnoreExtraProperties
class Book implements Serializable {

    private String title;
    private String price;
    private String author;
    private String notes;
    private String courseSubj;
    private String courseNumber;
    private String versionNumber;
    private String instructor;
    private boolean isAvailable;
    private String bookID;
    private String uid;
    private long negPostDateInSecs;

    // GUI Variables
    private String GUIRequestID;

    static final int TITLE = 0;
    static final int PRICE = 1;
    static final int AUTHOR = 2;
    static final int NOTES = 3;
    static final int COURSE_SUBJECT = 4;
    static final int COURSE_NUMBER = 5;
    static final int VERSION_NUMBER = 6;
    static final int INSTRUCTOR = 7;
    static final int AVAILABLE = 8;


    public Book(){

    }

    // Only courseSubj and courseNumber are required
    public Book(String courseSubj, String courseNumber){
        this.courseSubj = courseSubj;
        this.courseNumber = courseNumber;
        bookID = UUID.randomUUID().toString();
        negPostDateInSecs = Calendar.getInstance().getTimeInMillis() * -1;
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
    void set(int field, boolean value){

        switch (field){
            case AVAILABLE:
                this.isAvailable = value;
        }

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
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    String getCourseSubj() {
        return courseSubj;
    }
    String getCourseNumber() {
        return courseNumber;
    }
    String getPrice() {
        return price;
    }
    public String getVersionNumber() {
        return versionNumber;
    }
    public String getInstructor() {
        return instructor;
    }
    public String getNotes() {
        return notes;
    }
    boolean isAvailable() {
        return isAvailable;
    }
    long getPostDateInSecs() {return negPostDateInSecs * -1;}
    String getBookID() {
        return bookID;
    }

    // GUI - Control Variables
    void setGUIRequestID(String GUIRequestID) {
        this.GUIRequestID = GUIRequestID;
    }
    @Exclude
    String getGUIRequestID() {
        return GUIRequestID;
    }
}
