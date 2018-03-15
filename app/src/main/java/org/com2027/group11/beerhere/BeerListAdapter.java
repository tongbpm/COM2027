package org.com2027.group11.beerhere;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeersViewHolder> {

    private LayoutInflater inflater;

    private List<Beer> beers = Collections.emptyList();

    public BeerListAdapter(Context context, List<Beer> beers){
        inflater = LayoutInflater.from(context);
        this.beers = beers;
    }

    class BeersViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        ImageView imBeer;
        TextView tvBeerTitle;
        TextView tvRating;
        public BeersViewHolder(View itemView){
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            imBeer = itemView.findViewById(R.id.iv_beer);
            tvBeerTitle = itemView.findViewById(R.id.tv_beer_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }
    }

    @Override
    public BeersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        BeersViewHolder holder = new BeersViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(BeersViewHolder holder, int position) {
        Beer beer = beers.get(position);

        holder.tvRank.setText(String.valueOf(position+1));
        holder.imBeer.setImageResource(beer.getImageId());
        holder.tvBeerTitle.setText(beer.getTitle());
        holder.tvRating.setText(String.valueOf(beer.getRating()));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }
}