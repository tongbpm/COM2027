package org.com2027.group11.beerhere;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler{

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getmDatabase() {
        if (mDatabase==null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }

}
