package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.com2027.group11.beerhere.R;

import java.util.List;

public class BeerAdapter extends ArrayAdapter<String> {
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

        TextView tvRank = (TextView) view.findViewById(R.id.tv_rank);
        tvRank.setText(String.valueOf(position+1));

        TextView tvBeerTitle = (TextView) view.findViewById(R.id.tv_beer_title);
        tvBeerTitle.setText(beerTitle);

        TextView tvRating = (TextView) view.findViewById(R.id.tv_rating);
        tvRating.setText(String.valueOf(beers.get(position).beerUpvotes));

        ImageView ivBeer = (ImageView) view.findViewById(R.id.iv_beer);
        ivBeer.setImageBitmap(beers.get(position).beerImageBmp);

        ImageView ivThumbsUp = (ImageView) view.findViewById(R.id.ib_thumbs_up);
        ivThumbsUp.setImageResource(R.drawable.thumbs_up);

        ImageView ivThumbsDown= (ImageView) view.findViewById(R.id.ib_thumbs_down);
        ivThumbsDown.setImageResource(R.drawable.thumbs_down);

        return view;
    }
}