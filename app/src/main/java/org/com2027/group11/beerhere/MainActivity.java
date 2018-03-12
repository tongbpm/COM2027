package org.com2027.group11.beerhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Arbitrary code returned on successful log in
    private static final int RC_SIGN_IN = 123;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private List<Beer> beers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.ic_beer)
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Snackbar.make(findViewById(R.id.main_layout), "Signed In.", Snackbar.LENGTH_SHORT).show();
                try {
                    ((TextView) findViewById(R.id.main_text)).setText(getString(R.string.hello, user.getDisplayName()));
                    displayBeers();
                    setAddButtonFunc();
                } catch (NullPointerException e) {
                    Snackbar.make(findViewById(R.id.main_layout), "Error Signing In.", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                //Sign in failed
                Snackbar.make(findViewById(R.id.main_layout), "Error Signing In.", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.main_layout), "Facebook log In.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.main_layout), "Logged out.s", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayBeers(){

        beers = getBeers();
        List<String> beerTitles = getBeerTitles();

        ListAdapter adapter = new BeerAdapter(this, beerTitles, beers);

        ListView lvBeers = (ListView) findViewById(R.id.lv_beers);

        lvBeers.setAdapter(adapter);

        lvBeers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String beerSelected = "Beer selected: " +
                        String.valueOf(adapterView.getItemAtPosition(position));

                Toast.makeText(MainActivity.this, beerSelected, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAddButtonFunc(){
        ImageButton addButton = (ImageButton) findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked add button", Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Beer> getBeers(){
        List<Beer> beers = new ArrayList<>();

        beers.add(new Beer("Kalnapilis", R.drawable.kalnapilis, 351));
        beers.add(new Beer("Svyturys", R.drawable.svyturys, 363));
        beers.add(new Beer("Utenos", R.drawable.utenos, 291));
        beers.add(new Beer("Calsberg", R.drawable.calsberg, 123));

        return beers;
    }

    private List<String> getBeerTitles(){
        List<String> beerTitles = new ArrayList<>();
        for(Beer beer : beers){
            beerTitles.add(beer.getTitle());
        }
        return beerTitles;
    }
}
