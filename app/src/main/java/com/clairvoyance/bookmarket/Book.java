package com.clairvoyance.bookmarket;

import java.io.Serializable;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

public class Book implements Serializable {

    private String name;
    private String price;
    private String author;
    private String notes;
    private String courseSubj;
    private String courseNumber;
    private String versionNumber;
    private String instructor;
    private boolean isAvailable;

    static final int NAME = 0;
    static final int PRICE = 1;
    static final int AUTHOR = 2;
    static final int NOTES = 3;
    static final int COURSE_SUBJECT = 4;
    static final int COURSE_NUMBER = 5;
    static final int VERSION_NUMBER = 6;
    static final int INSTRUCTOR = 7;
    static final int AVAILABLE = 8;

    // Only courseSubj and courseNumber are required

    public Book(String courseSubj, String courseNumber){
        this.courseSubj = courseSubj;
        this.courseNumber = courseNumber;
    }

    void set(int field, String value){
        if (value == null){
            return;
        }

        switch (field){
            case NAME:
                this.name = value;
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
            case NAME:
                return this.name;
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

    public boolean isAvailable() {
        return isAvailable;
    }

}
