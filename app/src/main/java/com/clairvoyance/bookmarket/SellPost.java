package com.clairvoyance.bookmarket;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Sathya on 12/22/2017.
 *
 */

public class SellPost extends Post implements Serializable {

    public SellPost(Calendar postDate, double UID){
        super(postDate, UID);
    }

}
