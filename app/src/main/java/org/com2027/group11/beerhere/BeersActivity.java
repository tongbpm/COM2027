package org.com2027.group11.beerhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class BeersActivity extends AppCompatActivity implements FirebaseMutator {

    private RecyclerView rvBeers;
    private BeerListAdapter adapter;
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();
    private Vector<Beer> beers = new Vector<Beer>();

    private static final String LOG_TAG = "BEER-HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.firebaseManager.registerCallbackWithManager(this);
        //this.firebaseManager.getObjectsForTypeFromFirebase(null, SynchronisationManager.BELGIUM);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddBeerIntent = new Intent(BeersActivity.this, AddBeerActivity.class);
                startActivity(AddBeerIntent);
            }
        });

        displayBeers();
    }

    private void displayBeers(){
        ViewGroup view = findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.content_beers_page, view, false);
        rvBeers = findViewById(R.id.rv_beers);
        adapter = new BeerListAdapter(this, this.beers);
        rvBeers.setAdapter(adapter);
        rvBeers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void callbackGetObjectsFromFirebase(List<Object> objects) {
        Log.e(LOG_TAG, String.valueOf(objects.size()));
        for (Object object : objects) {
            Log.i(LOG_TAG, "Beer obtained! " + ((Beer) object).beerName);
            if (!(this.beers.contains(object))) {
                Log.e(LOG_TAG, String.valueOf(objects.size()));
                this.beers.add((Beer) object);
            }
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void callbackObjectRemovedFromFirebase(String id) {
        for (Beer beer : this.beers) {
            if (beer.beerName.equals(id)) {
                this.beers.remove(beer);
            }
        }

        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void callbackObjectChangedFromFirebase(Object object) {
        Log.i(LOG_TAG, "BeersActivity: object received from Firebase!");
        Beer beer = (Beer) object;
        Beer originalBeer = null;

        // Find original beer in list
        for (Beer b : this.beers) {
            if (b.beerName.equals(beer.beerName)) {
                originalBeer = b;
            }
        }

        if (originalBeer != null) {
            this.beers.remove(originalBeer);
            this.beers.add(beer);
        } else {
            Log.e(LOG_TAG, "Changed beer not found in array adapter?");
        }

        this.adapter.notifyDataSetChanged();
    }

}
