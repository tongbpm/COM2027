package org.com2027.group11.beerhere.utilities.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by apotter on 23/04/18.
 */
public class SynchronisationManager {

    private static SynchronisationManager instance = null;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private HashMap<String, String> firebasePaths = new HashMap<String, String>() {
        {
            put("beer", "server/beers");
            put("user", "server/users");
        }
    };

    protected SynchronisationManager() {}

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

    public boolean saveBeerToFirebase() {
        return true;
    }



}
