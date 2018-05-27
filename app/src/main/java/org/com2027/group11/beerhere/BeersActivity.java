package org.com2027.group11.beerhere;

import android.support.design.widget.NavigationView;
import android.app.Activity;
import android.app.Fragment;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;

import org.com2027.group11.beerhere.utilities.views.EmptyRecyclerView;

import org.com2027.group11.beerhere.utilities.FirebaseMutator;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

public class BeersActivity extends AppCompatActivity implements FirebaseMutator {

    private static final String TAG = "BEER-HERE";
    private EmptyRecyclerView rvBeers;
    private BeerListAdapter adapter;
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();
    private Vector<Beer> beers = new Vector<Beer>();
    private DrawerLayout mDrawerLayout;
    private NavigationView headerLayout;
    private FirebaseAuth mAuth;

    private static final String LOG_TAG = "BEER-HERE";
    private FusedLocationProviderClient mFusedLocationClient;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private TextView country;
    private String mCountry;
    ArrayList<String> countriesList = new ArrayList<String>();
    ArrayAdapter<String> countriesSpinnerAddapter;
    Spinner countriesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_page);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        headerLayout = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();

        Fresco.initialize(this);

        firebaseManager.getLoggedInUser();

        if (!userPermitedLocation()){
            requestUsersLocationPermission();
        }

        //sets the toolbar as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //adds the navigation drawer "hamburger" button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        //this.firebaseManager.getBeersForCountryFromFirebase(this, SynchronisationManager.UNITED_KINGDOM);
        //this.firebaseManager.getBeersForCountryFromFirebase(this, SynchronisationManager.UNITED_KINGDOM);


        //Navigation for Navigation Drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(

                menuItem -> {
                    //set item as selected to persist highlight
                    menuItem.setChecked(true);
                    //close drawer when item is tapped
                    mDrawerLayout.closeDrawers();
                    //update the UI based on menuItem chosen
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            return true;

                        case R.id.nav_submissions:
                            startActivity(new Intent(BeersActivity.this, SubmissionActivity.class));
                            return true;
                        case R.id.nav_favourites:
                            startActivity(new Intent(BeersActivity.this, FavoritesActivity.class));

                            return true;

                        case R.id.nav_signout:
                            SynchronisationManager.getInstance().loggedInUser = null;
                            signOut();
                            return true;


                    }
                    return false;
                });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddBeerIntent = new Intent(BeersActivity.this, AddBeerActivity.class);
                startActivity(AddBeerIntent);
            }
        });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Responds when the position of the drawer changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Responds when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Responds when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion stated changed
                    }
                }
        );

        displayCountriesSpinner();

        // creates an instance of the fused location provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        country = (TextView) findViewById(R.id.country);

        if(userPermitedLocation()){
            Log.i(LOG_TAG, "User has allowed Beer Here to use device's location");
            findUsersCountryAndShowIt();
        }
        else{
            Log.i(LOG_TAG, "User has NOT allowed Beer Here to use device's location");
        }

        displayBeers();
        userthread.start();

    }

    private void displayCountriesSpinner(){
        populateCountriesList();
        countriesSpinnerAddapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countriesList);
        countriesSpinner = (Spinner) findViewById(R.id.spinner_countries);
        countriesSpinner.setAdapter(countriesSpinnerAddapter);
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                firebaseManager.deregisterCallbackWithManager(BeersActivity.this, mCountry);
                beers.clear();

                mCountry = countriesSpinner.getItemAtPosition(position).toString();
                mCountry = mCountry.replace(' ', '_');

                if(mCountry != null) {
                    Log.d(TAG, mCountry);
                    if (firebaseManager.checkIfUserOldEnough(mCountry)) {
                        Log.d(TAG, "User is old enough");
                        TextView textView = findViewById(R.id.no_beer_text);
                        textView.setText(R.string.no_beer);
                        rvBeers.setEmptyView(textView);

                        Log.d("New Country: ", mCountry);

                        firebaseManager.registerCallbackWithManager(BeersActivity.this, mCountry);
                    } else {
                        Log.d(TAG, "User is not old enough");

                        TextView textView = findViewById(R.id.no_beer_text);
                        textView.setText(R.string.underage);
                        rvBeers.setEmptyView(textView);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateCountriesList(){
        countriesList.add("Austria");countriesList.add("Belgium");countriesList.add("Bulgaria");countriesList.add("Croatia");countriesList.add("Cyprus");countriesList.add("Czech Republic");countriesList.add("Denmark");countriesList.add("Estonia");countriesList.add("Finland");countriesList.add("France");countriesList.add("Germany");countriesList.add("Greece");countriesList.add("Hungary");countriesList.add("Ireland");countriesList.add("Italy");countriesList.add("Latvia");countriesList.add("Lithuania");countriesList.add("Luxembourg");countriesList.add("Malta");countriesList.add("Netherlands");countriesList.add("Poland");countriesList.add("Portugal");countriesList.add("Romania");countriesList.add("Slovakia");countriesList.add("Slovenia");countriesList.add("Spain");countriesList.add("Sweden");countriesList.add("United Kingdom");
    }

    private void findUsersCountryAndShowIt(){
        Log.d(TAG, "Finding location");
        try{
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                updateCountryShown(location);
                            }
                        }
                    });
        }catch(SecurityException se){
            se.printStackTrace();
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BeersActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                        Intent signOut = new Intent(BeersActivity.this, SignInActivity.class);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        //finish();
                        startActivity(signOut);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(userPermitedLocation()){
            findUsersCountryAndShowIt();
        }else{
            Log.i(LOG_TAG, "User has NOT allowed Beer Here to use device's location");
        }
    }

    private void updateCountryShown(Location location){
        double lat = (double) (location.getLatitude());
        Log.d(TAG, "Lat: " + lat);
        double lng = (double) (location.getLongitude());
        Log.d(TAG, "Long:  " + lng);


        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address = null;

        try {
            address = geocoder.getFromLocation(lat, lng, 5);
            if(address.size()>0){
                mCountry = address.get(0).getCountryName();

                // If the user denies location permissions, just load beers of a default country
                    if (mCountry == null) {
                        this.firebaseManager.registerCallbackWithManager(this, SynchronisationManager.AUSTRIA);
                    } else {
                        int spinnerCountryPosition = countriesSpinnerAddapter.getPosition(mCountry);
                        countriesSpinner.setSelection(spinnerCountryPosition);
                        mCountry = mCountry.replace(' ', '_');
                        this.firebaseManager.registerCallbackWithManager(this, mCountry);
                    }


                Log.d(TAG, "BeersActivity | updateCountryShown | Country = " + mCountry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestUsersLocationPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    private Boolean userPermitedLocation(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show(); Testing if icon works
                return true;
                        }

        return super.onOptionsItemSelected(item);
    }

        //method to obtain user info and display it on their profile on the header of the navigation drawer
    private void getUserInfo() {

        //Firebase authentication and user checking, and gets User ID
        FirebaseUser user = mAuth.getCurrentUser();

        //Inflate the header of the navigation drawer
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.nav_header, headerLayout, true);

        //check whether the user name and email are null and leave the default strings if they are null
        if (user.getDisplayName() == null) {
        } else {
            //sets the name into the username textView
            TextView name = view.findViewById(R.id.userName);
            name.setText(user.getDisplayName());
        }


        if (user.getEmail() == null) {
        } else {
            //sets the email into the email textView
            TextView email = view.findViewById(R.id.email);
            email.setText(user.getEmail());
        }
    }

    //Runs the getUserInfo() method on another thread to not cause conflict or freezing on the main thread
    Thread userthread = new Thread(new Runnable() {
        @Override
        public void run() {
            getUserInfo();
        }
    });


    private void displayBeers(){
        ViewGroup view = findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.content_beers_page, view, false);
        rvBeers = findViewById(R.id.rv_beers);
        rvBeers.setEmptyView(findViewById(R.id.no_beer_text));
        adapter = new BeerListAdapter(this, this.beers);
        rvBeers.setAdapter(adapter);
        rvBeers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void callbackGetObjectsFromFirebase(List<Object> objects) {
        Log.e(LOG_TAG, String.valueOf(objects.size()));
        for (Object object : objects) {
            Log.i(LOG_TAG, "BeersActivity | received firebase update of type List<Object> of size: " + String.valueOf(objects.size()));
            if (!(this.beers.contains(object))) {
                Log.e(LOG_TAG, String.valueOf(objects.size()));
                this.beers.add((Beer) object);

                if (((Beer) object).imageID == null) {
                    Log.e(LOG_TAG, "Beer Image ID is null!");
                }
            }
        }
        this.adapter.notifyDataSetChanged();

    }

    @Override
    public void callbackObjectRemovedFromFirebase(String id) {
        // Find beer in list
        Beer foundBeer = null;
        for (Beer b : this.beers) {
            if (b.name.equals(id)) {
                foundBeer = b;
            }
        }

        if (foundBeer != null) {
            Log.e(LOG_TAG, "Removed: " + foundBeer.name);
            this.beers.remove(foundBeer);
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void callbackObjectChangedFromFirebase(Object object) {
        Log.i(LOG_TAG, "BeersActivity: object received from Firebase!");
        Beer beer = (Beer) object;
        Log.e(LOG_TAG, "Beer changed: " + beer.name);
        Beer originalBeer = null;

        // Find original beer in list
        for (Beer b : this.beers) {
            if (b.name.equals(beer.name)) {
                originalBeer = b;
            }
        }

        if (originalBeer != null) {
            this.beers.remove(originalBeer);
            this.beers.add(beer);
            if (beer.imageID != null) {
                //this.firebaseManager.getBitmapForBeerFromFirebase(beer.imageID);
            } else {
                Log.e(LOG_TAG, "Beer Image ID is null!");
            }
        } else {
            Log.e(LOG_TAG, "Changed beer not found in array adapter?");
        }

        this.adapter.notifyDataSetChanged();

        Collections.reverse(this.beers);
    }

    @Override
    public void callbackGetBitmapForBeerFromFirebase(String beerImageID, Bitmap bitmap) {
        Log.i(LOG_TAG, "BeersActivity: got bitmap for " + beerImageID + " from Firebase!");

        for (Beer b : this.beers) {
            if (b.imageID.equals(beerImageID)) {
                if (b.beerImageBmp == null) {
                    b.setBeerImage(bitmap);
                }
                Log.i(LOG_TAG, "beer bitmap: " + b.beerImageBmp.toString());
            }
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void callbackNoChildrenForFirebasePath() {
        rvBeers.setEmptyView(findViewById(R.id.no_beer_text));
    }

    @Override
    public void callbackGetBeersForReferenceList(Set<Beer> beers) {
        Log.i(LOG_TAG, "GETTING BEERS FOR REF LIST");
        for (Beer beer : beers) {
            Log.i(LOG_TAG, beer.name);
        }
    }
}
