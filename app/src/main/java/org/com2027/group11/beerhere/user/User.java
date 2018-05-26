package org.com2027.group11.beerhere.user;


import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;


import org.com2027.group11.beerhere.beer.Beer;

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
    public Set<String> favourites = new HashSet<>();

    public User() {
    }

    public User(String uid, String name, String email, Date dateOfBirth, String country, Set<String> favourites) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.favourites = favourites;
    }

}
