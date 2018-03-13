package org.com2027.group11.beerhere;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.com2027.group11.beerhere.user.User;
import org.com2027.group11.beerhere.utilities.DatePickerFragment;
import org.com2027.group11.beerhere.utilities.database.AppDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener {

    private static final String TAG = "SIGN_UP_ACTIVITY";

    private EditText mDateOfBirth = null;
    private Spinner mCountry = null;
    private Button mSubmitButton = null;
    private FirebaseAuth mAuth = null;
    private ArrayList<String> mCountryList = null;
    private DatabaseReference mDatabase;

    private Date mDateOfBirthValue = null;
    private String mCountryString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDateOfBirth = findViewById(R.id.date_of_birth_form);
        mDateOfBirth.setInputType(InputType.TYPE_NULL);

        mCountry = findViewById(R.id.country_spinner);
        populateCountryLists();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mCountryList);
        mCountry.setAdapter(countryAdapter);
        //Make default selection the same as the device default location
        mCountry.setSelection(countryAdapter.getPosition(getResources().getConfiguration().locale.getDisplayCountry()));

        mSubmitButton = findViewById(R.id.sign_up_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSignUpForm();
            }
        });
    }

    private void populateCountryLists() {
        //Use a set so duplicate countries do not get added
        Set countrySet = new ArraySet<>();
        Locale[] locales = Locale.getAvailableLocales();

        //Add all countries to the list
        String country;
        for (Locale locale : locales) {
            country = locale.getDisplayCountry();
            if (country.length() > 0) {
                countrySet.add(country);
            }
        }

        mCountryList = new ArrayList<>(countrySet);
        Collections.sort(mCountryList);
    }

    public void showDatePicker(View v) {
        mDateOfBirth.setHint("");
        DialogFragment datePickerFragment = DatePickerFragment.newInstance(this);
        datePickerFragment.show(this.getFragmentManager(), "datePicker");
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onDateSet(Date date) {
        //Formats the date according to the users locale
        String dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        mDateOfBirthValue = date;
        Log.d(TAG, "The date is " + dateString);
        mDateOfBirth.setText(dateString);
    }

    private void submitSignUpForm() {
        Log.d(TAG, "Form submitted");
        mCountryString = mCountry.getSelectedItem().toString();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), mDateOfBirthValue, mCountryString);

        writeNewUser(user);
        //Todo user has now completed sign up send them to activity where they can view beers
        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }


    private void writeNewUser(final User user) {
        Log.d(TAG, "Writing user data to database");
        //Write User to external database
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
        //Write user to local database
        new Thread(() -> {
            Log.d(TAG, "Writing to local DB. UID:" + user.uid);
            AppDatabase.getAppDatabase(getApplicationContext()).userDao().insertUser(user);
        }).start();
    }

}
