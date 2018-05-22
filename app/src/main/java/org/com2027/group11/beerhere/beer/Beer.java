package org.com2027.group11.beerhere.beer;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import org.com2027.group11.beerhere.R;

/**
 * Created by alexpotter1 on 27/02/2018.
 */

@IgnoreExtraProperties
public class Beer {

    public Bitmap beerImageBmp;
    public String name;
    public int upvotes;
    public int downvotes;
    private int rating;
    public int imageID;
    public long timeCreated;
    public int hotness;

    public static final String LOG_TAG = "BEER-HERE";

    public Beer() {}

    public Beer(String name, @NonNull int imageID, @NonNull int upvotes,
                @NonNull int downvotes, @NonNull String timeCreated, @NonNull int hotness, @NonNull int rating ) {
        if (!(name.isEmpty())) {
            this.name = name;
            this.imageID = imageID;
            this.upvotes = upvotes;
            this.downvotes = downvotes;

            this.timeCreated = Long.parseLong(timeCreated);

            this.hotness = hotness;

            this.rating = rating;

        } else {
            Log.e(LOG_TAG, "Beer: Failed to create, empty name");
        }
    }


    public Beer(String name) {
        if (!(name.isEmpty())) {
            this.name = name;
            this.upvotes = 1;
            this.downvotes = 0;

            this.hotness = 24;
            this.rating = this.hotness*(this.upvotes - this.downvotes);
            this.timeCreated = System.currentTimeMillis() / 1000L;
        } else {
            Log.e(LOG_TAG, "Beer: Failed to create, empty name");
        }
    }


    public Beer(String name, @NonNull int imageID, @NonNull int upvotes,
                @NonNull int downvotes) {
        if (!(name.isEmpty())) {
            this.name = name;
            this.imageID = imageID;
            this.upvotes = upvotes;
            this.downvotes = downvotes;

            this.timeCreated = System.currentTimeMillis() / 1000L;

            this.hotness = 0;

            this.rating = this.upvotes - this.downvotes;
            //Log.e(LOG_TAG, String.valueOf(R.drawable.calsberg));

        } else {
            //Log.e(LOG_TAG, "Beer: Failed to create, empty name");
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

    public int getRating() {
        // Basic implementation, change
        return this.rating;
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }
}
