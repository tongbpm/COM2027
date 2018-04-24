package org.com2027.group11.beerhere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adrien on 05/03/2018.
 */

public class Country_Adapter extends ArrayAdapter<Country> {

    private ArrayList<Country> countries = new ArrayList<Country>();



    public Country_Adapter(Context context, ArrayList<Country> countries) {
        super(context, R.layout.row_layout, countries);
        this.countries = countries;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        final TextView textView = (TextView)rowView.findViewById(R.id.country_name);
        //textView.setText(countries.get(position).getName());
        textView.setText(countries.get(position).getName()); //replace ghis line by previous

        return rowView;
    }


    public String getListViewText(int position){
        return countries.get(position).getName();
    }
}
