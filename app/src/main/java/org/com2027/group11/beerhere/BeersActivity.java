package org.com2027.group11.beerhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.util.ArrayList;
import java.util.List;

public class BeersActivity extends AppCompatActivity implements FirebaseMutator {

    private RecyclerView rvBeers;
    private BeerListAdapter adapter;
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();
    private List<Beer> beers = new ArrayList<Beer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.firebaseManager.registerCallbackWithManager(this);

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
        adapter = new BeerListAdapter(this, getBeers());
        rvBeers.setAdapter(adapter);
        rvBeers.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Beer> getBeers() {
        return this.beers;
    }

    @Override
    public void callbackGetObjectsFromFirebase(List<Object> objects) {
        for (Object object : objects) {
            this.beers.add((Beer) object);
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
        Beer beer = (Beer) object;

        for (Beer b : this.beers) {
            if (b.beerName.equals(beer.beerName)) {
                this.beers.remove(b);
                this.beers.add(beer);
            }
        }

        this.adapter.notifyDataSetChanged();
    }

}
