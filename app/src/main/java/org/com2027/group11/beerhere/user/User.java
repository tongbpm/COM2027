package org.com2027.group11.beerhere.user;


import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
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

    @Exclude
    public Set<String> submissions = new HashSet<>();
    @Exclude
    public Set<String> favourites = new HashSet<>();

    @Exclude
    public DatabaseReference ref;

    public User() {
    }

    public User(String uid, String name, String email, Date dateOfBirth, String country) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.favourites = favourites;
        this.submissions = submissions;
    }

    public void addSubmission(DatabaseReference dbRef){
        submissions.add((dbRef.toString().substring(FirebaseDatabase.getInstance().getReference().toString().length()-1)).replaceFirst("m", ""));
        updateFirebase();
    }

    public ArrayList<String>getSubmissions(){
        return new ArrayList<String>(submissions);
    }

    public ArrayList<String>getFavourites(){
        return new ArrayList<String>(favourites);
    }

    public void setSubmissions(ArrayList<String> submissions){
        this.submissions = new HashSet<String>(submissions);
    }

    public void setFavourites(ArrayList<String> favourites){
        this.favourites = new HashSet<String>(favourites);
    }


    /**
     * adds to user favourites if not already in favourites
     * otherwise removes from user favourites
     *
     * @param beerRef
     */
    public void updateFavourites(DatabaseReference beerRef){
        String favourite = (beerRef.toString().substring(FirebaseDatabase.getInstance().getReference().toString().length()-1)).replaceFirst("m", "");
        if(favourites.contains(favourite)){
            //already in favorites -> so being removed
            favourites.remove(favourite);

        }else{
            //not in favoritesSoBeingAdded
            favourites.add(favourite);

        }

        updateFirebase();

    }

    private void updateFirebase(){
        SynchronisationManager.getInstance().updateUserFavourites(this);
    }

}
