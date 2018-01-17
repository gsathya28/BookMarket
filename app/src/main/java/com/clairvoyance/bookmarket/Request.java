package com.clairvoyance.bookmarket;

import android.renderscript.Sampler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sathya on 1/16/2018.
 */

class Request {

    private String uid;
    private String bookID;
    private boolean accepted = false;

    Request(String uid, String postID){
        this.uid = uid;
        this.bookID = postID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    // Actual Book Data will be loaded and displayed in the listener in the listener.

}
