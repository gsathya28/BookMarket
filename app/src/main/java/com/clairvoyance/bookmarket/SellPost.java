package com.clairvoyance.bookmarket;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

class SellPost extends Post implements Serializable {

    SellPost(Calendar postDate, String UID){
        super(postDate, UID);
    }

}
