package com.clairvoyance.bookmarket;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by Sathya on 1/24/2018.
 *
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onNewToken(String token) {
        //Get hold of the registration token
        //Log the token
        Log.d(TAG, "Refreshed token: " + token);

        // Update UserData - to store the registration token
        try {
            FirebaseHandler.setToken(token);
        }catch (IllegalAccessException iae){
            // Todo: Local Storage of Registration Key
        }
    }

    
}
