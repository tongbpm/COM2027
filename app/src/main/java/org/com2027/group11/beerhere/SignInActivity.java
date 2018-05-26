package org.com2027.group11.beerhere;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.com2027.group11.beerhere.beer.Beer;
import org.com2027.group11.beerhere.user.User;

import java.util.Arrays;
import java.util.List;

/**
 * Main activity is served to a user when they open the app, if a user is not logged in
 * they will be presented with log in information
 */
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "MAIN_ACTIVITY";
    //Arbitrary code returned on successful log in
    private static final int RC_SIGN_IN = 123;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Context mContext;

    private List<Beer> beers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        FirebaseHandler.getmDatabase();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        mContext = this.getApplicationContext();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        if(mAuth.getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.ic_beer)
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(true)
                            .build(),

                    RC_SIGN_IN);
        }else{
            Intent intent = new Intent(this, BeersActivity.class);
            startActivity(intent);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                mAuth = FirebaseAuth.getInstance();
                //Checks once if the user that logs in exists
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                            Log.d(TAG, "User has logged in before");
                            Intent beerIntent = new Intent(SignInActivity.this, BeersActivity.class);
                            startActivity(beerIntent);
                            finish();
                        } else {
                            //Sends user to activity with sign up form
                            Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                            startActivity(signUpIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Unable to read data snapshot");
                    }
                });
                Snackbar.make(findViewById(R.id.main_layout), "Signed In.", Snackbar.LENGTH_SHORT).show();
                try {
                } catch (NullPointerException e) {
                    Snackbar.make(findViewById(R.id.main_layout), "Error Signing In.", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                //Sign in failed
                Snackbar.make(findViewById(R.id.main_layout), "Error Signing In.", Snackbar.LENGTH_SHORT).show();
            }
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
}