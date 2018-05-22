package org.com2027.group11.beerhere.utilities;

import org.com2027.group11.beerhere.beer.Beer;

import java.util.List;

public interface FirebaseMutator {

    void callbackGetObjectsFromFirebase(List<Object> objects);
    void callbackObjectChangedFromFirebase(Object object);
    void callbackObjectRemovedFromFirebase(String id);

}
