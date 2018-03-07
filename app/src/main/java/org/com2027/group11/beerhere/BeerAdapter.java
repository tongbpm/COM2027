package org.com2027.group11.beerhere;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class BeerAdapter extends ArrayAdapter<String> {
    private List<Beer> beers;

    public BeerAdapter(@NonNull Context context, List<String> beerTitles, List<Beer> beers) {
        super(context, R.layout.row_layout,beerTitles);

        this.beers  = beers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.row_layout, parent, false);

        String beerTitle = getItem(position);

        TextView rankTV = (TextView) view.findViewById(R.id.rankTV);
        rankTV.setText(String.valueOf(position+1));

        TextView beerTitleTV = (TextView) view.findViewById(R.id.beerTitleTV);
        beerTitleTV.setText(beerTitle);

        TextView ratingTV = (TextView) view.findViewById(R.id.ratingTV);
        ratingTV.setText(String.valueOf(beers.get(position).getRating()));

        ImageView imageView = (ImageView) view.findViewById(R.id.beerImageView);
        imageView.setImageResource(beers.get(position).getImageId());

        return view;
    }
}