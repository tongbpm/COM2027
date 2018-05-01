package org.com2027.group11.beerhere.utilities;

import java.util.Iterator;
import java.util.List;

public interface FirebaseMutator {

    Iterator<Object> callbackGetObjectsFromFirebase(List<Object> objects);
    Object callbackObjectUpdatedFromFirebase();

}
