package org.com2027.group11.beerhere.utilities;

import android.graphics.Bitmap;

import org.com2027.group11.beerhere.beer.Beer;

import java.util.List;
import java.util.Set;

public interface FirebaseMutator {

    void callbackGetObjectsFromFirebase(List<Object> objects);
    void callbackObjectChangedFromFirebase(Object object);
    void callbackObjectRemovedFromFirebase(String id);
    void callbackGetBitmapForBeerFromFirebase(String beerName, Bitmap bitmap);
    void callbackNoChildrenForFirebasePath();
    void callbackGetBeersForReferenceList(Set<Beer> beers);
}
