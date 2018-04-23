package org.com2027.group11.beerhere.utilities.database;

/**
 * Created by apotter on 23/04/18.
 */
public class SynchronisationManager {

    private static SynchronisationManager instance = null;
    protected SynchronisationManager() {}

    public static SynchronisationManager getInstance() {
        if (instance == null) {
            instance = new SynchronisationManager();
        }
        return instance;
    }

}
