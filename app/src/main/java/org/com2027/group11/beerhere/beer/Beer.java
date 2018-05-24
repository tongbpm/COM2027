package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.com2027.group11.beerhere.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexpotter1 on 27/02/2018.
 */

@IgnoreExtraProperties
public class Beer {

    @Exclude
    public Bitmap beerImageBmp;

    public String name;
    public int upvotes;
    public int downvotes;
    private int rating;
    public String imageID;
    public long timeCreated;
    public int hotness;
    @Exclude
    public Set<String> upvoters = new HashSet<>();
    @Exclude
    public Set<String> downvoters = new HashSet<>();

    public static final String LOG_TAG = "BEER-HERE";

    public Beer() {}

    public Beer(String name, @NonNull String imageID, @NonNull int upvotes,
                @NonNull int downvotes, @NonNull String timeCreated, @NonNull int hotness, @NonNull int rating, @NonNull Set<String> upvoters, @NonNull Set<String> downvoters ) {
        if (!(name.isEmpty())) {
            this.name = name;
            this.imageID = imageID;
            this.upvotes = upvotes;
            this.downvotes = downvotes;

            this.upvoters = upvoters;
            this.downvoters = downvoters;

            this.timeCreated = Long.parseLong(timeCreated);

            this.hotness = hotness;

            this.rating = rating;

        } else {
            Log.e(LOG_TAG, "Beer: Failed to create, empty name");
        }
    }

    public Beer(String name, String imageId, String uid) {
        if (!(name.isEmpty())) {
            this.name = name;
            this.upvotes = 1;
            this.downvotes = 0;

            this.imageID = imageId;

            this.upvoters.add(uid);

            this.hotness = 24;
            this.rating = this.hotness*(this.upvotes - this.downvotes);
            this.timeCreated = System.currentTimeMillis() / 1000L;
        } else {
            Log.e(LOG_TAG, "Beer: Failed to create, empty name");
        }
    }


    public Beer(String name, @NonNull String imageID, @NonNull int upvotes,
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

    public void upvote(String uid, Context context){
        if(upvoters.contains(uid)){
            //user already upvoted
            Toast.makeText(context, R.string.upvote_fail, Toast.LENGTH_SHORT).show();
        }else{
            upvoters.add(uid);
            upvotes++;
            if(downvoters.contains(uid)){
                downvoters.remove(uid);
                downvotes--;
            }
        }
    }

    public  void downvote(String uid, Context context){
        if(downvoters.contains(uid)){
            //User already downvoted
            Toast.makeText(context, R.string.downvote_fail, Toast.LENGTH_SHORT).show();
        }else{
            downvoters.add(uid);
            downvotes++;
            if(upvoters.contains(uid)){
                upvoters.remove(uid);
                upvotes--;
            }
        }
    }

    @Exclude
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

    public ArrayList<String> getUpvoters(){
        return new ArrayList<>(upvoters);
    }

    public ArrayList<String>getDownvoters(){
        return new ArrayList<>(downvoters);
    }

    public void setUpvoters(ArrayList<String> upvoters) {
        this.upvoters = new HashSet<>(upvoters);
    }

    public void setDownvoters(Set<String> downvoters) {
        this.downvoters = new HashSet<>(downvoters);
    }
}
