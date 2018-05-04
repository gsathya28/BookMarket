package com.clairvoyance.bookmarket;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Sathya on 1/24/2018.
 *
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Update UserData - to store the registration token
        try {
            FirebaseHandler.setToken(refreshedToken);
        }catch (IllegalAccessException iae){
            // Todo: Local Storage of Registration Key
        }
    }

    
}
