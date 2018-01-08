package com.clairvoyance.bookmarket;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

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
    private long postDateInSecs;
    private String uid;
    private boolean isNegotiable = false;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<String> bookIDs = new ArrayList<>();
    private String notes;
    private String postType;

    public Post(){
        // For Firebase usage
    }

    Post(String uid){
        postDateInSecs = Calendar.getInstance().getTimeInMillis();
        setPostDate(postDateInSecs);
        this.uid = uid;
        postID = UUID.randomUUID().toString();

        if (this instanceof SellPost){
            postType = "sell";
        }
        else if(this instanceof BuyPost){
            postType = "buy";
        }
    }


    String getPostID() {
        return postID;
    }
    String getUid() {
        return uid;
    }
    public long getPostDateInSecs() {
        return postDateInSecs;
    }
    void setNotes(String notes) {this.notes = notes;}
    String getNotes() {return notes;}
    void setNegotiable(boolean negotiable) {isNegotiable = negotiable;}
    boolean isNegotiable() {
        return isNegotiable;
    }
    public void setPostDate(long postDateInSecs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(postDateInSecs);
        this.postDate = calendar;
    }
    void addBook(Book book){
        books.add(book);
        bookIDs.add(book.getBookID());
    }
    void addBookList(ArrayList<Book> books){
        this.books = books;
        for (Book book: books){
            bookIDs.add(book.getBookID());
        }
    }
    public ArrayList<String> getBookIDs() {
        return bookIDs;
    }

    @Exclude
    Calendar getPostDate() {
        return postDate;
    }

    @Exclude
    ArrayList<Book> getBooks() {
        if (books.isEmpty() && !bookIDs.isEmpty()){
            DatabaseReference bookListRef = FirebaseDatabase.getInstance().getReference().child("books");
            for (String id: bookIDs){

                DatabaseReference bookRef = bookListRef.child(id);
            }
        }

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
