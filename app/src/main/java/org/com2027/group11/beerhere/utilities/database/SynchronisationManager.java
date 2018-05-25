package org.com2027.group11.beerhere.utilities.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.com2027.group11.beerhere.R;
import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by apotter on 23/04/18.
 */
public class SynchronisationManager {

    private static final String LOG_TAG = "BEER-HERE";

    private static SynchronisationManager instance = null;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    // Associate a listener with a country
    private Map<FirebaseMutator, String> registeredCallbacks;
    private Map<FirebaseMutator, ChildEventListener> childListeners;

    private List<DatabaseReference> references = new ArrayList<DatabaseReference>();

    private Gson gson = new Gson();

    private final int IMAGE_REQUEST = 71;

    // Path enumerations for adding data to Firebase storage
    // Android doesn't like Enum structures, so use this for better performance
    public static final String BEER = "beer";
    public static final String USER = "user";
    public static final String AUSTRIA = "Austria";
    public static final String BELGIUM = "Belgium";
    public static final String BULGARIA = "Bulgaria";
    public static final String CROATIA = "Croatia";
    public static final String CYPRUS = "Cyprus";
    public static final String CZECH_REPUBLIC = "Czech_Republic";
    public static final String DENMARK = "Denmark";
    public static final String ESTONIA = "Estonia";
    public static final String FINLAND = "Finland";
    public static final String FRANCE = "France";
    public static final String GERMANY = "Germany";
    public static final String GREECE = "Greece";
    public static final String HUNGARY = "Hungary";
    public static final String IRELAND = "Ireland";
    public static final String ITALY = "Italy";
    public static final String LATVIA = "Latvia";
    public static final String LITHUANIA = "Lithuania";
    public static final String LUXEMBOURG = "Luxembourg";
    public static final String MALTA = "Malta";
    public static final String NETHERLANDS = "Netherlands";
    public static final String POLAND = "Poland";
    public static final String PORTUGAL = "Portugal";
    public static final String ROMANIA = "Romania";
    public static final String SLOVAKIA = "Slovakia";
    public static final String SLOVENIA = "Slovenia";
    public static final String SPAIN = "Spain";
    public static final String SWEDEN = "Sweden";
    public static final String UNITED_KINGDOM = "United_Kingdom";

    @StringDef({BEER, USER, AUSTRIA, BELGIUM, BULGARIA, CROATIA, CYPRUS, CZECH_REPUBLIC, DENMARK, ESTONIA, FINLAND, FRANCE,
                GERMANY, GREECE, HUNGARY, IRELAND, ITALY, LATVIA, LITHUANIA, LUXEMBOURG, MALTA, NETHERLANDS, POLAND, PORTUGAL,
                ROMANIA, SLOVAKIA, SLOVENIA, SPAIN, SWEDEN, UNITED_KINGDOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Path {}

    private Map<String, String> firebasePaths = new HashMap<String, String>() {
        {
            put("beer", "beers");
            put("user", "users");
            put("Austria", "countries/Austria");
            put("Belgium", "countries/Belgium");
            put("Bulgaria", "countries/Bulgaria");
            put("Croatia", "countries/Croatia");
            put("Cyprus", "countries/Cyprus");
            put("Czech_Republic", "countries/Czech_Republic");
            put("Denmark", "countries/Denmark");
            put("Estonia", "countries/Estonia");
            put("Finland", "countries/Finland");
            put("France", "countries/France");
            put("Germany", "countries/Germany");
            put("Greece", "countries/Greece");
            put("Hungary", "countries/Hungary");
            put("Ireland", "countries/Ireland");
            put("Italy", "countries/Italy");
            put("Latvia", "countries/Latvia");
            put("Lithuania", "countries/Lithuania");
            put("Luxembourg", "countries/Luxembourg");
            put("Malta", "countries/Malta");
            put("Netherlands", "countries/Netherlands");
            put("Poland", "countries/Poland");
            put("Portugal", "countries/Portugal");
            put("Romania", "countries/Romania");
            put("Slovakia", "countries/Slovakia");
            put("Slovenia", "countries/Slovenia");
            put("Spain", "countries/Spain");
            put("Sweden", "countries/Sweden");
            put("United_Kingdom", "countries/United_Kingdom");
        }
    };

    protected SynchronisationManager() {
        Iterator iter = this.firebasePaths.entrySet().iterator();
        this.registeredCallbacks = new HashMap<FirebaseMutator, String>();
        this.childListeners = new HashMap<FirebaseMutator, ChildEventListener>();
    }

    public static SynchronisationManager getInstance() {
        if (instance == null) {
            instance = new SynchronisationManager();
        }
        return instance;
    }

    private void beginListeningForCountryBeersFirebase(FirebaseMutator mutatorContext, @Path String country) {
        DatabaseReference reference = this.database.getReference();

        String countryPath = this.firebasePaths.get(country);
        Log.i(LOG_TAG, "SyncManager | beginListening | creating ChildEventListener");
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                List<Object> returnedObjects = new ArrayList<Object>();

                Log.i(LOG_TAG, "SyncManager | CEListener | New child added to Firebase with key " + dataSnapshot.getKey());
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Beer beer = createBeerFromFirebaseMap(map, dataSnapshot.getKey());
                returnedObjects.add(beer);

                for (FirebaseMutator mut : registeredCallbacks.keySet()) {
                    mut.callbackGetObjectsFromFirebase(returnedObjects);
                }
                beer.ref = reference.child(countryPath).child("beers").child(beer.name);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (FirebaseMutator mut : registeredCallbacks.keySet()) {
                    Log.e(LOG_TAG, dataSnapshot.getKey());
                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                    Beer beer = createBeerFromFirebaseMap(map, dataSnapshot.getKey());

                    Log.i(LOG_TAG, "SyncManager | CEListener | Existing child modified on Firebase with key " + dataSnapshot.getKey());

                    beer.ref = reference.child(countryPath).child("beers").child(beer.name);

                    mut.callbackObjectChangedFromFirebase(beer);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (FirebaseMutator mut : registeredCallbacks.keySet()) {
                    mut.callbackObjectRemovedFromFirebase(dataSnapshot.getKey());
                    Log.i(LOG_TAG, "SyncManager | CEListener | Child removed from Firebase with key " + dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.wtf(LOG_TAG, "SyncManager | CEListener | Firebase RDB read cancelled with error " + databaseError.getDetails());
            }
        };
        reference.child(countryPath).child("beers").addChildEventListener(listener);
        this.childListeners.put(mutatorContext, listener);
    }

    private void stopListeningForCountryBeersFirebase(FirebaseMutator mutatorContext, @Path String country) {
        DatabaseReference reference = this.database.getReference();

        String countryPath = this.firebasePaths.get(country);
        ChildEventListener listener = this.childListeners.get(mutatorContext);
        Log.i(LOG_TAG, "SyncManager | stopListening | removing Listener");
        reference.child(countryPath).child("beers").removeEventListener(listener);
    }

    @Nullable
    private String searchForFirebasePath(@NonNull String criterion) {
        // Search by key
        Iterator iter = this.firebasePaths.entrySet().iterator();

        String foundPath = null;

        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            if (pair.getKey().toString().toUpperCase().equals(criterion.toUpperCase())) {
                foundPath = pair.getValue().toString();
            }
        }

        // Return null if the path is not found...this needs to be handled later on
        if (foundPath != null) {
            return foundPath;
        }
        return null;
    }

    public void saveBeerToFirebase(@Path String country, String beerName, Beer savedObject) throws NullPointerException {
        String path = this.searchForFirebasePath(country);
        if (path == null) {
            throw new NullPointerException("Firebase country path does not exist.");
        }

        DatabaseReference ref = this.database.getReference().child("countries").child(country).child("beers");
        DatabaseReference newObjectRef = ref.child(beerName);

        newObjectRef.setValue(savedObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // Could change to snackbars later on...
                if (databaseError != null) {
                    Log.e(LOG_TAG, "Data could not be saved to Firebase: " + databaseError.getMessage());
                } else {
                    Log.e(LOG_TAG, "Data saved successfully.");
                }
            }
        });
    }

    public void deleteObjectByIdFromFirebase(@NonNull @Path String type, String id) throws NullPointerException {
        String path = this.searchForFirebasePath(type);
        if (path == null) {
            throw new NullPointerException("SyncManager | deleteObjectById | Firebase database path does not exist.");
        }

        DatabaseReference ref = this.database.getReference().getRoot().child(path);
        ref.setValue(null);
    }

    private Beer createBeerFromFirebaseMap(@NonNull HashMap<String, Object> inMap, @NonNull String mapName) {
        int upvotes = 0;
        try {
            upvotes = ( (Long) inMap.get("upvotes")).intValue();
        } catch (NullPointerException e) {
            upvotes = 0;
        }

        String time_created;
        try {
            time_created = Long.toString((Long) inMap.get("time_created"));
        } catch (NullPointerException e) {
            time_created = "0";
        }

        int downvotes;
        try {
            downvotes = ( (Long) inMap.get("downvotes")).intValue();
        } catch (NullPointerException e) {
            downvotes = 0;
        }

        int hotness;
        try {
            hotness = ((Long) inMap.get("hotness")).intValue();
        } catch (NullPointerException e) {
            hotness = 0;
        }

        String image_id;
        try {
            image_id =  (String) inMap.get("imageID");
        } catch (NullPointerException e) {
            image_id = "";
        }

        int rating;
        try{
            rating = ((Long)inMap.get("rating")).intValue();
        }catch (NullPointerException e){
            rating = 0;
        }

        Set<String> upvoters;
        try{
            upvoters = new HashSet<String>((ArrayList<String>)inMap.get("upvoters"));
        }catch (NullPointerException e){
            upvoters = new HashSet<>();
        }

        Set<String> downvoters;
        try{
            downvoters = new HashSet<String>((ArrayList<String>)inMap.get("downvoters"));
        }catch(NullPointerException e){
            downvoters = new HashSet<>();
        }

        Beer beer = new Beer(mapName, image_id, upvotes, downvotes, time_created, hotness, rating, upvoters, downvoters);
        return beer;
    }

    public void updateBeer(DatabaseReference ref, Beer beer){
        ref.setValue(beer);
    }

    /**
     * Call this before using the manager so it can keep track of where to send updated objects,
     * as it operates asynchronously.
     * @param mutatorContext - the reflective type of the class implementing the FirebaseMutator interface to receive callbacks
     */
    public void registerCallbackWithManager(@NonNull FirebaseMutator mutatorContext, @Path String country) {
        if (this.registeredCallbacks.containsKey(mutatorContext)) {
            Log.w(LOG_TAG, "SyncManager | registerCallback | Mutator context already exists in callback");
        } else {
            this.registeredCallbacks.put(mutatorContext, country);
            this.beginListeningForCountryBeersFirebase(mutatorContext, country);
            Log.i(LOG_TAG, "SyncManager | registerCallback | Mutator context registered successfully.");
        }
    }

    /**
     * Call this if a class implementing the callback FirebaseMutator interface is no longer active.
     * @param mutatorContext - the reflective type of the class implementing the FirebaseMutator interface to receive callbacks
     */
    public void deregisterCallbackWithManager(@NonNull FirebaseMutator mutatorContext, @Path String country) {
        if (this.registeredCallbacks.containsKey(mutatorContext)) {
            this.registeredCallbacks.remove(mutatorContext);
            this.stopListeningForCountryBeersFirebase(mutatorContext, country);
            Log.i(LOG_TAG, "SyncManager | registerCallback | Mutator context successfully deregistered.");
        }
    }

}
