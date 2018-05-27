package org.com2027.group11.beerhere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.utilities.FirebaseMutator;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;
import org.com2027.group11.beerhere.utilities.views.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubmissionActivity extends AppCompatActivity implements FirebaseMutator{


    private EmptyRecyclerView rvSubmittedBeers;
    private BeerListAdapter adapter;
    private List<Beer> beers = new ArrayList<>();
    private SynchronisationManager firebaseManager = SynchronisationManager.getInstance();
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fav_toolbar);

        //setSupportActionBar(toolbar);
        //adds the navigation drawer "hamburger" button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


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
                            startActivity(new Intent(SubmissionActivity.this, BeersActivity.class));
                            return true;

                        case R.id.nav_submissions:


                            return true;
                        case R.id.nav_favourites:
                            startActivity(new Intent(SubmissionActivity.this, FavoritesActivity.class));

                            return true;

                        case R.id.nav_signout:
                            SynchronisationManager.getInstance().loggedInUser = null;
                            signOut();
                            return true;


                    }
                    return false;
                });


        displaySubmission();

    }

    private void displaySubmission() {
        ViewGroup view = findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.content_beers_page, view, false);
        rvSubmittedBeers = findViewById(R.id.rvSubmitted_Beers);
        adapter = new BeerListAdapter(this, this.beers);
        rvSubmittedBeers.setAdapter(adapter);
        rvSubmittedBeers.setLayoutManager(new LinearLayoutManager(this));
        rvSubmittedBeers.setEmptyView(findViewById(R.id.no_submissions));
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SubmissionActivity.this, "You have been signed out", Toast.LENGTH_SHORT).show();
                        Intent signOut = new Intent(SubmissionActivity.this, SignInActivity.class);
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

    @Override
    public void callbackGetObjectsFromFirebase(List<Object> objects) {

    }

    @Override
    public void callbackObjectChangedFromFirebase(Object object) {

    }

    @Override
    public void callbackObjectRemovedFromFirebase(String id) {

    }

    @Override
    public void callbackGetBitmapForBeerFromFirebase(String beerName, Bitmap bitmap) {

    }

    @Override
    public void callbackNoChildrenForFirebasePath() {

    }

    @Override
    public void callbackGetBeersForReferenceList(Set<Beer> beers) {
        if (beers != null) {
            this.beers = new ArrayList<>(beers);
        }
        this.adapter.notifyDataSetChanged();
    }
}
