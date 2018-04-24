package org.com2027.group11.beerhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CountriesActivity extends AppCompatActivity {

    ArrayList<Country> countryList = new ArrayList<Country>();
    ArrayList<String[]> list = new ArrayList<>();
    ArrayAdapter<Country> adapter;
    Country_Adapter myCountryAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String countryName = list.get(position)[0];
                String countryContinent = list.get(position)[1];
                Toast.makeText(CountriesActivity.this, "Selected country: " + countryName + " (" + countryContinent + ")", Toast.LENGTH_SHORT).show();

            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //change listView to context.this maybes or vice versa
        adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.country_name, countryList);
        myCountryAdapter = new Country_Adapter(this, countryList);
        readFileData();
    }

    public void readFileData() {

        try {
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format("countries.txt")));
            Scanner scan = new Scanner(textFileStream);
            readFileHelper(scan);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readFileHelper(Scanner scan) {

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] pieces = line.split("[*]");
            list.add(pieces);
        }

        scan.close();


        countryList = new ArrayList<>();
        for (int b = 0; b < list.size(); b++) {

                String name = list.get(b)[0];
                String continent = list.get(b)[1];
                Country c = new Country(name, continent);
                countryList.add(c);


        }

        adapter  = new ArrayAdapter<>(this, R.layout.row_layout, R.id.country_name, countryList);
        myCountryAdapter = new Country_Adapter(this, countryList);
        listView.setAdapter(myCountryAdapter);


    }

}
