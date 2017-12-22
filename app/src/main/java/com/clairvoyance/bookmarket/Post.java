package com.clairvoyance.bookmarket;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

class Post {

    private double postID;
    private Calendar postDate;
    private double UID;
    private boolean isNegotiable;
    private boolean isAvailable;
    private ArrayList<Book> books = new ArrayList<>();
    private String notes;

    Post(double postID, Calendar postDate, double UID){
        this.postID = postID;
        this.postDate = postDate;
        this.UID = UID;
    }

    public Calendar getPostDate() {
        return postDate;
    }

    public double getPostID() {
        return postID;
    }

    public double getUID() {
        return UID;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public boolean isNegotiable() {
        return isNegotiable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
