package org.com2027.group11.beerhere.utilities;

import android.graphics.Bitmap;

import org.com2027.group11.beerhere.user.User;

import java.util.List;

public interface FirebaseMutator {

    void callbackGetObjectsFromFirebase(List<Object> objects);
    void callbackObjectChangedFromFirebase(Object object);
    void callbackObjectRemovedFromFirebase(String id);
    void callbackGetBitmapForBeerFromFirebase(String beerName, Bitmap bitmap);
    void callbackNoChildrenForFirebasePath();
    void callbackGetUserFromFirebase(User user)

}
