package org.com2027.group11.beerhere;

import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        this.firebaseManager.getBeersAtReferences(this.firebaseManager.loggedInUser.submissions);


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
