package org.com2027.group11.beerhere.utilities.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.user.User;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private final int IMAGE_REQUEST = 71;

    private int userAge = 0;
    public User loggedInUser =  null;

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

    private Map<String, Integer> drinkingAges = new HashMap<>();

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
        getUserAgeFromDatabase();
        populateAges();
        this.registeredCallbacks = new HashMap<FirebaseMutator, String>();
        this.childListeners = new HashMap<FirebaseMutator, ChildEventListener>();
    }

    public void getLoggedInUser() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loggedInUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG, databaseError.getDetails());
            }
        });

    }

    public static SynchronisationManager getInstance() {
        if (instance == null) {
            instance = new SynchronisationManager();
        }
        return instance;
    }

    private void populateAges(){
        for( Map.Entry<String, String> refPath : this.firebasePaths.entrySet()){
                if(refPath.getValue().contains("countries")){
                    DatabaseReference reference = this.database.getReference(refPath.getValue()).child("age");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(LOG_TAG, reference.toString());
                            Log.d("DRINKING AGE:", (dataSnapshot.getValue(Integer.class).toString()));
                            drinkingAges.put(refPath.getKey(), dataSnapshot.getValue(Integer.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(LOG_TAG,databaseError.getDetails());
                        }
                    });
                }
        }
    }

    private void getUserAgeFromDatabase() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = this.database.getReference("users/"+uid+"/dateOfBirth");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Date dateOfBirth = dataSnapshot.getValue(Date.class);

                Calendar dob = Calendar.getInstance();
                dob.setTime(dateOfBirth);

                Calendar now = Calendar.getInstance();

                try {
                    if (dob.after(now)) {
                        throw new IllegalArgumentException("Cannot be born in the future");
                    }
                    int years = now.get(Calendar.YEAR)-dob.get(Calendar.YEAR);
                    int currMonth = now.get(Calendar.MONTH);
                    int dobMonth = dob.get(Calendar.MONTH);
                    if(dobMonth > currMonth){
                        years--;
                    }else if(dobMonth == currMonth){
                        int currDay = now.get(Calendar.DAY_OF_MONTH);
                        int dobDay = dob.get(Calendar.DAY_OF_MONTH);
                        if (dobDay > currDay) {
                            years--;
                        }
                    }
                    SynchronisationManager.this.userAge = years;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(LOG_TAG,databaseError.getDetails());

            }
        });
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
                    Log.d(LOG_TAG, "Callback got objects for country");
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

        // Adding another listener to detect if there's no children, and trigger a callback
        ValueEventListener valueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()) {
                    // No children
                    for (FirebaseMutator mutator : registeredCallbacks.keySet()) {
                        mutator.callbackNoChildrenForFirebasePath();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.child(countryPath).child("beers").addChildEventListener(listener);
        reference.child(countryPath).child("beers").addValueEventListener(valueListener);
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

    public void getBeersAtReferences(final Set<DatabaseReference> references) {

        Set<Beer> beers = new HashSet<>();

        for (DatabaseReference reference : references) {

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    beers.add(dataSnapshot.getValue(Beer.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(LOG_TAG,"SyncManager | getBeersAtReferences | Firebase RDB read cancelled!");
                }
            });
        }

        new Thread(() -> {
               while (beers.size() != references.size()) {
                   try {
                       wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

               for (FirebaseMutator mutator : registeredCallbacks.keySet()) {
                   mutator.callbackGetBeersForReferenceList(beers);
               }
            }
        ).start();
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

    public void updateUser(final User user) {
        Log.d(LOG_TAG, "SyncManager | updateUser | Writing user data to database");
        //Write User to external database
        this.database.getReference().child("users").child(user.uid).setValue(user);
    }


    public boolean checkIfUserOldEnough(String country) {
        return !(userAge < drinkingAges.get(country));
    }

    /**
     * Call this before using the manager so it can keep track of where to send updated objects,
     * as it operates asynchronously.
     * @param mutatorContext - the reflective type of the class implementing the FirebaseMutator interface to receive callbacks
     */
    public void registerCallbackWithManager(@NonNull FirebaseMutator mutatorContext, @Path String country)  {
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

    public void updateUserFavourites(final User user) {
        Log.d(LOG_TAG, "SyncManager | updateUser | Writing user data to database");
        //Write User to external database
        this.database.getReference().child("users").child(user.uid).setValue(user);
    }



}
