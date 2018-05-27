package org.com2027.group11.beerhere;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity implements FirebaseMutator {

    private RecyclerView rvFavBeers;
    private BeerListAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private List<Beer> beers = new ArrayList<>();
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.fav_drawer_layout);
        rvFavBeers = (RecyclerView) findViewById(R.id.rvFav_beers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        this.firebaseManager.getBeersAtReferences(this.firebaseManager.loggedInUser.favourites);


        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        NavigationView navigationView = findViewById(R.id.fav_nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                    //set item as selected to persist highlight
                    menuItem.setChecked(true);
                    //close drawer when item is tapped
                    mDrawerLayout.closeDrawers();
                    return false;
                }}
        );



        //Get the list of fav beers with intent

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        displayfavBeers();

    }

    public void displayfavBeers(){
        ViewGroup view = findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.content_beers_page, view, false);
        rvFavBeers = findViewById(R.id.rvFav_beers);
        adapter = new BeerListAdapter(this, this.beers);
        rvFavBeers.setAdapter(adapter);
        rvFavBeers.setLayoutManager(new LinearLayoutManager(this));
    }


    /*
      Mandatory override methods for FirebaseMutator iface
     */

    @Override
    public void callbackGetBeersForReferenceList(Set<Beer> beers) {
        if (beers != null) {
            this.beers = new ArrayList<>(beers);
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void callbackGetObjectsFromFirebase(List<Object> objects) {}
    public void callbackObjectChangedFromFirebase(Object object) {}
    public void callbackObjectRemovedFromFirebase(String id) {}
    public void callbackGetBitmapForBeerFromFirebase(String beerName, Bitmap bitmap) {}
    public void callbackNoChildrenForFirebasePath() {}

}
