package org.com2027.group11.beerhere.utilities;

import android.graphics.Bitmap;
import java.util.List;

public interface FirebaseMutator {

    void callbackGetObjectsFromFirebase(List<Object> objects);
    void callbackGetObjectsForCountryFromFirebase(List<Object> objects);
    void callbackObjectChangedFromFirebase(Object object);
    void callbackObjectRemovedFromFirebase(String id);
    void callbackGetBitmapForBeerFromFirebase(String beerName, Bitmap bitmap);

}
