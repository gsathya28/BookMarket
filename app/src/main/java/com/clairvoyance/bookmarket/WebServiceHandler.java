package com.clairvoyance.bookmarket;

import android.content.Context;
import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Amazon Web Service Handler
 */

class WebServiceHandler {

    private static double mainUID;
    final static int RC_SIGN_IN = 2899;
    final static String WEB_CLIENT_ID = "483082602147-bmhfbbj3k1proa5r2ll3hr694d9s5mrr.apps.googleusercontent.com";


    static boolean authenticateMainUser(User localUser, Context context){

        // Use AWS to get main user credentials and authenticate
        // Test mainUID

        // Initialize the Amazon Cognito credentials provider
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
