package com.clairvoyance.bookmarket;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.util.ArrayList;

/**
 * Created by Sathya on 12/21/2017.
 * Amazon Web Service Handler
 */

class WebServiceHandler {

    private static double mainUID;


    static boolean authenticateMainUser(User localUser, Context context){

        // Use AWS to get main user credentials and authenticate
        // Test mainUID

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:08f4fe45-b937-45d2-b943-c6690c4fe3c7", // Identity pool ID
                Regions.US_EAST_1 // Region
        );

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
