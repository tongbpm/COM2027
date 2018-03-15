package org.com2027.group11.beerhere;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import org.com2027.group11.beerhere.R;

import java.util.ArrayList;
import java.util.List;

public class BeersPage extends AppCompatActivity {

    private RecyclerView rvBeers;
    private BeerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beers_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ViewGroup view = findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.content_beers_page, view, false);
        rvBeers = findViewById(R.id.rv_beers);
        adapter = new BeerListAdapter(this, getBeers());
        rvBeers.setAdapter(adapter);
        rvBeers.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Beer> getBeers() {
        List<Beer> beers = new ArrayList<>();

        beers.add(new Beer("Kalnapilis", R.drawable.kalnapilis, 351));
        beers.add(new Beer("Svyturys", R.drawable.svyturys, 363));
        beers.add(new Beer("Utenos", R.drawable.utenos, 291));
        beers.add(new Beer("Calsberg", R.drawable.calsberg, 123));

        return beers;
    }

}
