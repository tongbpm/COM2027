package org.com2027.group11.beerhere.utilities;

import java.util.Iterator;
import java.util.List;

public interface FirebaseMutator {

    void callbackGetObjectsFromFirebase(List<Object> objects);
    void callbackObjectChangedFromFirebase(Object object);
    void callbackObjectRemovedFromFirebase(String id);

}
