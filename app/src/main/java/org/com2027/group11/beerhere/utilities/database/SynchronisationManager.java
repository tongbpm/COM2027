package org.com2027.group11.beerhere.utilities.database;

import android.arch.persistence.room.Database;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.com2027.group11.beerhere.utilities.FirebaseMutator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by apotter on 23/04/18.
 */
public class SynchronisationManager {

    private static final String LOG_TAG = "BEER-HERE";

    private static SynchronisationManager instance = null;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<DatabaseReference> references = new ArrayList<DatabaseReference>();

    // Path enumerations for adding data to Firebase storage
    // Android doesn't like Enum structures, so use this for better performance
    public static final String BEER = "beer";
    public static final String USER = "user";
    @StringDef({BEER, USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Path {}

    private Map<String, String> firebasePaths = new HashMap<String, String>() {
        {
            put("beer", "server/beerhere/beers");
            put("user", "server/beerhere/users");
        }
    };

    protected SynchronisationManager() {
        Iterator iter = this.firebasePaths.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            this.references.add(this.database.getReference(pair.getValue().toString()));
        }

        for (DatabaseReference databaseReference : this.references) {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static SynchronisationManager getInstance() {
        if (instance == null) {
            instance = new SynchronisationManager();
        }
        return instance;
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

    public <T> void saveNewObjectToFirebase(@Path String type, T savedObject) throws NullPointerException {
        String path = this.searchForFirebasePath(type);
        if (path == null) {
            throw new NullPointerException("Firebase database path does not exist.");
        }

        DatabaseReference ref = this.database.getReference(path);
        DatabaseReference newObjectRef = ref.push();

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

    public void getObjectsForTypeFromFirebase(@NonNull FirebaseMutator firebaseAccessorContext, @NonNull @Path String type) throws NullPointerException {
        String path = this.searchForFirebasePath(type);
        if (path == null) {
            throw new NullPointerException("Firebase database path does not exist.");
        }

        DatabaseReference ref = this.database.getReference(path);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Object> objects = new ArrayList<Object>();
                Log.e(LOG_TAG, "Count " + dataSnapshot.getChildrenCount());

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Object child = childSnapshot.getValue(Object.class);
                    objects.add(child);
                }

                firebaseAccessorContext.callbackGetObjectsFromFirebase(objects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    



}
