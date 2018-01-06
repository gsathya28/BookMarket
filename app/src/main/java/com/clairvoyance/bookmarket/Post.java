package com.clairvoyance.bookmarket;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

class Post implements Serializable{

    private String postID;
    private Calendar postDate;
    private String uid;
    private boolean isNegotiable = false;
    private ArrayList<Book> books = new ArrayList<>();
    private String notes;
    private String postType;

    public Post(){
        // For Firebase usage
    }

    Post(Calendar postDate, String uid){
        this.postDate = postDate;
        this.uid = uid;
        postID = UUID.randomUUID().toString();

        if (this instanceof SellPost){
            postType = "sell";
        }
        else if(this instanceof BuyPost){
            postType = "buy";
        }
    }

    Calendar getPostDate() {
        return postDate;
    }
    String getPostID() {
        return postID;
    }
    String getUid() {
        return uid;
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

    @Exclude
    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("postID", postID);
        result.put("uid", uid);
        result.put("postDate", postDate);
        result.put("negotiable", isNegotiable);
        result.put("books", books);
        result.put("notes", notes);
        result.put("postType", postType);

        return result;
    }
}
