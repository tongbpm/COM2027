package org.com2027.group11.beerhere;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.beer.BeerListAdapter;
import org.com2027.group11.beerhere.user.User;
import org.com2027.group11.beerhere.user.UserDao;
import org.com2027.group11.beerhere.utilities.database.AppDatabase;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BeersActivity extends AppCompatActivity {

    private RecyclerView rvBeers;
    private BeerListAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private static final String TAG = "MAIN_ACTIVITY";
    //private Context mContext;
    //private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView username;


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
                menuItem -> {
                    //set item as selected to persist highlight
                    menuItem.setChecked(true);
                    //close drawer when item is tapped
                    mDrawerLayout.closeDrawers();
                    return false;
                }
        );



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

        displayBeers();
        userthread.start();


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

    public User getUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Log.e("Beer uid", uid);
        UserDao userDao = AppDatabase.getAppDatabase(this).userDao();
        User user = userDao.findByID(uid);
        Log.e("user", user.name);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.nav_header, null );
        String username = user.name.toString();
        Log.e("username", username);
        final TextView textView = (TextView) view.findViewById(R.id.userName);
        textView.setText(username);
        return user;
    }
    Thread userthread = new Thread(new Runnable() {
        @Override
        public void run() {
            getUser();
        }
    });


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

        beers.add(new Beer("Kalnapilis", R.drawable.kalnapilis, 351, 0));
        beers.add(new Beer("Svyturys", R.drawable.svyturys, 363, 0));
        beers.add(new Beer("Utenos", R.drawable.utenos, 291, 0));
        beers.add(new Beer("Calsberg", R.drawable.calsberg, 123, 0));

        return beers;
    }

}
