package com.clairvoyance.bookmarket;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Amazon Web Service Handler
 */

class WebServiceHandler {

    static double mainUID;

    static boolean authenticateMainUser(User localUser){

        // Use AWS to get main user credentials and authenticate
        // Test mainUID

        return false;
    }

    static ArrayList<BuyPost> getPublicBuyPosts(){
        ArrayList<BuyPost> buyPosts = new ArrayList<>();

        // Use AWS to generate posts and put it in an array

        return buyPosts;
    }

    static User generateMainUser(){
        mainUID = 1;

        return new User("", mainUID, "");
    }

    static void addPublicPost(Post post){
        // Use AWS to add public post
    }

}
