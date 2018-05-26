package org.com2027.group11.beerhere.user;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


import org.com2027.group11.beerhere.R;
import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that keeps track of users details, this is stored on the server and cached on the client so
 * the user can use the functionality of the app offline
 */
@IgnoreExtraProperties
public class User {
    @NonNull
    public String uid;
    public String name;
    public String email;
    public Date dateOfBirth;
    public String country;

    public Set<DatabaseReference> submissions = new HashSet<>();
    public Set<DatabaseReference> favourites = new HashSet<>();

    @Exclude
    public DatabaseReference ref;

    public User() {
    }

    public User(String uid, String name, String email, Date dateOfBirth, String country, Set<DatabaseReference> favourites) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.favourites = favourites;
    }

    public void addSubmission(DatabaseReference dbRef){
        submissions.add(dbRef);
        updateFirebase();
    }

    /**
     * adds to user favourites if not already in favourites
     * otherwise removes from user favourites
     *
     * @param beerRef
     */
    public void updateFavourites(DatabaseReference beerRef){
        if(favourites.contains(beerRef)){
            //already in favorites -> so being removed
            favourites.remove(beerRef);
        }else{
            //not in favoritesSoBeingAdded
            favourites.add(beerRef);
        }

        updateFirebase();

    }

    private void updateFirebase(){
        SynchronisationManager.getInstance().updateUserFavourites(this);
    }

}
