package org.com2027.group11.beerhere.beer;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by alexpotter1 on 27/02/2018.
 */

@IgnoreExtraProperties
public class Beer {

    public Bitmap beerImageBmp;
    public String beerName;
    public int beerUpvotes;
    public int beerDownvotes;

    public static final String LOG_TAG = "BEER-HERE";

    public Beer() {}

    public Beer(String name) {
        if (!(name.isEmpty())) {
            this.beerName = name;
            this.beerUpvotes = 0;
            this.beerDownvotes = 0;
        } else {
            Log.e(LOG_TAG, "Beer: Failed to create, empty name");
        }
    }

    public boolean setBeerImage(Bitmap bmp) {
        if (bmp != null) {
            this.beerImageBmp = bmp;
            return true;
        } else {
            Log.e(LOG_TAG, "Beer: Failed to set image, null bitmap");
            return false;
        }
    }



}
