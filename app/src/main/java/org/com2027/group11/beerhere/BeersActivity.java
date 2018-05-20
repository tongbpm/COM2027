package org.com2027.group11.beerhere;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BeersActivity extends AppCompatActivity {

    private RecyclerView rvBeers;
    private BeerListAdapter adapter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_page);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //[REMOVE] these lines when done debugging, or heck they wont interfere
                        //with merge

                        if (menuItem.toString().equals("Countries")) {
                            //intent to countries
                        } else if (menuItem.toString().equals("Home")) {
                            //this is current activity
                        } else if (menuItem.toString().equals("Your Submissions")) {
                            //intent to submissions
                        } else if (menuItem.toString().equals("Favourites")) {
                            Intent intent = new Intent(BeersActivity.this, FavoritesActivity.class);
                            startActivity(intent);
                        } else if (menuItem.toString().equals("Sign Out")) {
                            //intent to home and erase local stuff
                        }

                        // set item as selected to persist highlight
                        Log.i("whogivesashort", "onNavigationItemSelected: " + menuItem.toString());
                        menuItem.setChecked(true);
                        // close drawer when item is tapped


                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
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
                        // Respond when the drawer motino stated changed
                    }
                }
        );

        displayBeers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        List<Beer> beers = new ArrayList<>();

        beers.add(new Beer("Kalnapilis", R.drawable.kalnapilis, 351, 0, false));
        beers.add(new Beer("Svyturys", R.drawable.svyturys, 363, 0, true));
        beers.add(new Beer("Utenos", R.drawable.utenos, 291, 0, false));
        beers.add(new Beer("Calsberg", R.drawable.calsberg, 123, 0, true));

        return beers;
    }




}
