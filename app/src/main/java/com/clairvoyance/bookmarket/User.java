package com.clairvoyance.bookmarket;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 *
 */

public class User {
    private String name;
    private double UID;
    private String email;
    private ArrayList<Post> myPosts;

    User(String name, double UID, String email){
        this.name = name;
        this.UID = UID;
        this.email = email;
    }

    public void addPost(Post post){
        myPosts.add(post);
    }



}
