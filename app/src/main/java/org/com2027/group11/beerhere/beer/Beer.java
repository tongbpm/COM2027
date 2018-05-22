package org.com2027.group11.beerhere.beer;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
    private int beerRating;
    public int imageID;
    public long timeCreated;

    public static final String LOG_TAG = "BEER-HERE";

    public Beer() {}

    public Beer(String name, @NonNull int imageID, @NonNull int upvotes, @NonNull int downvotes) {
        if (!(name.isEmpty())) {
            this.beerName = name;
            this.imageID = imageID;
            this.beerUpvotes = upvotes;
            this.beerDownvotes = downvotes;
            this.timeCreated = System.currentTimeMillis() / 1000L;
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

    public int getBeerRating() {
        // Basic implementation, change
        return this.beerUpvotes - this.beerDownvotes;
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }



}
