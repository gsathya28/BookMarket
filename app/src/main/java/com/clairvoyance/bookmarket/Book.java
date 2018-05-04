package com.clairvoyance.bookmarket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Sathya on 12/22/2017.
 * Custom Book Variable
 */

@IgnoreExtraProperties
class Book implements Serializable, Parcelable {

    public static final String MY_BOOK_SELL = "mBookSell";
    public static final String MY_BOOK_BUY = "mBookBuy";
    public static final String ALL_BOOK_BUY = "allBookBuy";
    public static final String ALL_BOOK_SELL = "allBookSell";

    public static final String SELL_BOOK = "sellBooks";
    public static final String BUY_BOOK = "buyBooks";

    private String title;
    private String price;
    private String author;
    private String notes;
    private String courseSubj;
    private String courseNumber;
    private String courseTotal;
    private String versionNumber;
    private String bookID;
    private String uid;
    private String type;
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

    @Deprecated // Must not use this constructor! All books require courseSubj and courseNumber
    public Book(){
        // Firebase Constructor (required)
    }

    // Only courseSubj and courseNumber are required
    // Todo: Set UID of Book in FirebaseHandler AddBookMethod (or elsewhere) - for offline optimization
    public Book(String courseSubj, String courseNumber, String type){
        this.courseSubj = courseSubj;
        this.courseNumber = courseNumber;
        this.courseTotal = courseSubj + courseNumber;
        bookID = UUID.randomUUID().toString();
        postDateInSecs = Calendar.getInstance().getTimeInMillis();

        if(!(type.equals(SELL_BOOK) || type.equals(BUY_BOOK))){
            throw new IllegalArgumentException("Illegal Type - Type should be set using Book.SELL_BOOK or Book.BUY_BOOK");
        }else{
            this.type = type;
        }
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
            default:
                throw new IllegalArgumentException("Invalid Parameter");
        }
    }

    public void setUid(String uid) {
        this.uid = uid;
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
    public String getCourseSubj() {
        return courseSubj;
    }
    public String getCourseNumber() {
        return courseNumber;
    }
    public String getCourseTotal() {
        return courseTotal;
    }
    public String getPrice() {
        return price;
    }
    public String getVersionNumber() {
        return versionNumber;
    }
    public String getNotes() {
        return notes;
    }
    public long getPostDateInSecs() {
        return postDateInSecs;
    }
    public String getBookID() {
        return bookID;
    }
    public String getUid() {
        return uid;
    }
    public boolean isSpam() {
        return isSpam;
    }
    public String getType() {
        return type;
    }

    @Exclude
    ArrayList<Request> getRequests(){
        return requests;
    }


    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>(){

        @Override
        public Book createFromParcel(Parcel parcel) {
            return new Book(parcel);
        }

        @Override
        public Book[] newArray(int i) {
            return new Book[0];
        }
    };

    private Book(Parcel in){
        title = in.readString();
        price = in.readString();
        author = in.readString();
        notes = in.readString();
        courseSubj = in.readString();
        courseNumber = in.readString();
        courseTotal = in.readString();
        versionNumber = in.readString();
        bookID = in.readString();
        uid = in.readString();
        postDateInSecs = in.readLong();
        isSpam = in.readByte() != 0;


        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            Boolean value = in.readByte() != 0;
            requestIDs.put(key,value);
        }

    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(price);
        parcel.writeString(author);
        parcel.writeString(notes);
        parcel.writeString(courseSubj);
        parcel.writeString(courseNumber);
        parcel.writeString(courseTotal);
        parcel.writeString(versionNumber);
        parcel.writeString(bookID);
        parcel.writeString(uid);
        parcel.writeLong(postDateInSecs);

        byte spam = (byte) (isSpam ? 1 : 0);
        parcel.writeByte(spam);

        // Write requestIDs -
        parcel.writeInt(requestIDs.size());
        for(Map.Entry<String,Boolean> entry : requestIDs.entrySet()){
            parcel.writeString(entry.getKey());
            // This doesn't matter - all values are true..
            byte x = (byte) (entry.getValue() ? 1 : 0);
            parcel.writeByte(x);
        }
    }


}