package com.clairvoyance.bookmarket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

class Post implements Serializable{

    private double postID = -1;
    private Calendar postDate;
    private double UID;
    private boolean isNegotiable;
    private ArrayList<Book> books = new ArrayList<>();
    private String notes;
    private String postType;

    Post(Calendar postDate, double UID){
        this.postDate = postDate;
        this.UID = UID;
    }

    Calendar getPostDate() {
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

    public void addBookList(ArrayList<Book> books){
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public boolean isNegotiable() {
        return isNegotiable;
    }
}
