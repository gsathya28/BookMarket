package com.clairvoyance.bookmarket;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 */

public class WebServiceHandler {

    double mainUID;

    public WebServiceHandler(double UID){
        mainUID = UID;
    }

    public User generateMainUser(){
        // Use AWS to get main user credentials

        return new User("", mainUID, "");
    }

    public static ArrayList<Post> getBuyPosts(){
        ArrayList<Post> buyPosts = new ArrayList<>();

        // Use AWS to generate posts and put it in an array

        return buyPosts;
    }

}
