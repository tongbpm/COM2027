package org.com2027.group11.beerhere.user;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by river on 27/02/18.
 */
@IgnoreExtraProperties
/**
 * A class that keeps track of users details
 */
public class User {

    /**
     * The User's name
     */
    public String name;
    /**
     * The User's email address
     */
    public String email;
    /**
     * The User's date of birth
     */
    public Date dateOfBirth;
    /**
     * The country the user lives in
     */
    public String country;

    public User() {
    }

    public User(String name, String email, Date dateOfBirth, String country) {
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
    }
}
