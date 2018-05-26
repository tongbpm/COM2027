package org.com2027.group11.beerhere.user;


import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;


import java.util.Date;
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

    public User() {
    }

    public User(String uid, String name, String email, Date dateOfBirth, String country) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
    }

}
