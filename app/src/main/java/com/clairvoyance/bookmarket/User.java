package com.clairvoyance.bookmarket;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 *
 */

class User implements Serializable {
    private String name;
    private double UID;
    private String email;
    private ArrayList<Post> myPosts;
    private boolean isAuthenticated;

    User(String name, double UID, String email){
        this.name = name;
        this.UID = UID;
        this.email = email;
        myPosts = new ArrayList<>();
    }

    void addPost(Post post){
        myPosts.add(post);
    }

    boolean isAuthenticated() {
        return isAuthenticated;
    }

    void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    double getUID() {
        return UID;
    }

    ArrayList<SellPost> getMySellPosts() {
        ArrayList<SellPost> sellPosts = new ArrayList<>();
        for (Post post : myPosts){
            if (post instanceof SellPost){
                sellPosts.add((SellPost) post);
            }
        }

        return sellPosts;
    }
}
