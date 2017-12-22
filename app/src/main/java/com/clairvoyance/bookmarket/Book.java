package com.clairvoyance.bookmarket;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

public class Book {

    private String name;
    private String price;
    private String author;
    private String notes;
    private String courseSubj;
    private String courseNumber;

    static final int NAME = 0;
    static final int PRICE = 1;
    static final int AUTHOR = 2;
    static final int NOTES = 3;
    static final int COURSE_SUBJECT = 4;
    static final int COURSE_NUMBER = 5;

    // Only courseSubj and courseNumber are required

    public Book(String courseSubj, String courseNumber){
        this.courseSubj = courseSubj;
        this.courseNumber = courseNumber;
    }

    public void set(int field, String value){
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
            default:
                throw new IllegalArgumentException("Invalid Parameter");
        }
    }

    public String get(int field){
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
            default:
                throw new IllegalArgumentException("Invalid Parameter");
        }
    }
}
