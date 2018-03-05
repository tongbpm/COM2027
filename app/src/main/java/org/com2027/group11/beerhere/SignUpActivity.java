package org.com2027.group11.beerhere;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.com2027.group11.beerhere.utilities.DatePickerFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity implements DatePickerFragment.DatePickerFragmentListener {

    private static final String TAG = "SIGN_UP_ACTIVITY";

    EditText mDateOfBirth = null;
    Spinner mCountry = null;

    ArrayList<String> mCountryList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDateOfBirth = findViewById(R.id.date_of_birth_form);
        mDateOfBirth.setInputType(InputType.TYPE_NULL);

        mCountry = findViewById(R.id.country_spinner);
        populateCountryLists();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mCountryList);
        mCountry.setAdapter(countryAdapter);
        //Make default selection the same as the device default location
        mCountry.setSelection(countryAdapter.getPosition(getResources().getConfiguration().locale.getDisplayCountry()));
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

        Log.d(TAG, "The date is " + dateString);
        mDateOfBirth.setText(dateString);
    }
}
