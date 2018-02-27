package org.com2027.group11.beerhere.user;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by river on 27/02/18.
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public Date dateOfBirth;
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
