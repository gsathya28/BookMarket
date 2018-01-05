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
    private String UID;
    private boolean isNegotiable = false;
    private ArrayList<Book> books = new ArrayList<>();
    private String notes;
    private String postType;

    public Post(){
        // For Firebase usage
    }

    Post(Calendar postDate, String UID){
        this.postDate = postDate;
        this.UID = UID;
    }

    Calendar getPostDate() {
        return postDate;
    }
    double getPostID() {
        return postID;
    }
    String getUID() {
        return UID;
    }

    void setNotes(String notes) {this.notes = notes;}
    String getNotes() {return notes;}
    void setNegotiable(boolean negotiable) {isNegotiable = negotiable;}
    boolean isNegotiable() {
        return isNegotiable;
    }

    void addBook(Book book){
        books.add(book);
    }
    void addBookList(ArrayList<Book> books){
        this.books = books;
    }
    ArrayList<Book> getBooks() {
        return books;
    }
}
