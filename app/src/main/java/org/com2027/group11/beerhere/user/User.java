package org.com2027.group11.beerhere.user;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import org.com2027.group11.beerhere.utilities.database.TypeConverters.DateTypeConverter;

import java.util.Date;
/**
 * A class that keeps track of users details, this is stored on the server and cached on the client so
 * the user can use the functionality of the app offline
 */
@IgnoreExtraProperties
@Entity
public class User {
    @NonNull
    @PrimaryKey
    public String uid;
    @ColumnInfo(name = "full_name")
    public String name;
    @ColumnInfo(name = "email_address")
    public String email;
    @ColumnInfo(name = "date_of_birth")
    @TypeConverters(DateTypeConverter.class)
    public Date dateOfBirth;
    @ColumnInfo(name = "country")
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
