package com.clairvoyance.bookmarket;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sathya on 12/24/2017.
 * DataHandler
 */

class DataHandler {

    // May add myBooks / publicBooks ArrayLists later if necessary

    static User parseMainUserData(Context context){

        User mainUser = null;
        try {
            FileInputStream fIn = context.openFileInput("localUser");
            byte[] byteHolder = new byte[fIn.available()];
            int x = fIn.read(byteHolder);
            if (x != -1)
                mainUser = (User) Serializer.deserialize(byteHolder);
        } catch (IOException|ClassNotFoundException i)
        {
            Log.d("Read Error: ", i.getMessage());
        }

        return mainUser;
    }

    static void saveMainUserData(User mainUser, Context context){

        try {
            FileOutputStream fOut = context.openFileOutput("localUser", Context.MODE_PRIVATE);
            fOut.write(Serializer.serialize(mainUser));
            fOut.close();
        } catch (IOException i) {
            Log.d("Save Error: ", i.getMessage());
        }
    }
}
