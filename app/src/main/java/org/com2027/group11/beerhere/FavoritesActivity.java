package org.com2027.group11.beerhere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.widget.TextView;


import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;

import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import org.com2027.group11.beerhere.utilities.views.EmptyRecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavoritesActivity extends AppCompatActivity implements FirebaseMutator {

    private EmptyRecyclerView rvFavBeers;
    private BeerListAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private List<Beer> beers = new ArrayList<>();
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fav_toolbar);
        rvFavBeers = (EmptyRecyclerView) findViewById(R.id.rvFav_beers);

        TextView tv = (TextView) findViewById(R.id.no_favorites_text);
        tv.setText(R.string.no_beer);
        rvFavBeers.setEmptyView(tv);

        setSupportActionBar(toolbar);
        //adds the navigation drawer "hamburger" button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



        this.firebaseManager.getBeersAtReferences(this.firebaseManager.loggedInUser.favourites);

        //Navigation for Navigation Drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(

                menuItem -> {
                    //set item as selected to persist highlight
                    menuItem.setChecked(true);
                    //close drawer when item is tapped
                    //mDrawerLayout.closeDrawers();
                    //update the UI based on menuItem chosen
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            startActivity(new Intent(FavoritesActivity.this, BeersActivity.class));
                            return true;

                        case R.id.nav_submissions:

                            startActivity(new Intent(FavoritesActivity.this, SubmissionActivity.class));
                            return true;
                        case R.id.nav_favourites:

                            return true;

                        case R.id.nav_signout:
                            SynchronisationManager.getInstance().loggedInUser = null;
                            signOut();
                            return true;


                    }
                    return false;
                });



        //Get the list of fav beers with intent




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



    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(FavoritesActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                        Intent signOut = new Intent(FavoritesActivity.this, SignInActivity.class);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        signOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        //finish();
                        startActivity(signOut);
                    }
                });
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
