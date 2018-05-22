package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.com2027.group11.beerhere.R;

import java.util.Collections;
import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeersViewHolder> {

    private LayoutInflater inflater;

    private List<Beer> beers = Collections.emptyList();

    private String countryName = ""; //[CHANGE] get this as extra from intent


    public BeerListAdapter(Context context, List<Beer> beers){
        inflater = LayoutInflater.from(context);
        this.beers = beers;
    }

    class BeersViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        ImageView imBeer;
        TextView tvBeerTitle;
        TextView tvRating;
        ImageButton ibUpvote;
        ImageButton ibDownVote;
        ImageButton favButton;
        public BeersViewHolder(View itemView){
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            imBeer = itemView.findViewById(R.id.iv_beer);
            tvBeerTitle = itemView.findViewById(R.id.tv_beer_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            ibUpvote = itemView.findViewById(R.id.ib_thumbs_up);
            ibDownVote = itemView.findViewById(R.id.ib_thumbs_down);
            favButton = itemView.findViewById(R.id.fav_button);
        }
    }


    @Override
    public BeersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        BeersViewHolder holder = new BeersViewHolder(view);

        return holder;
    }

    public void removeBeerFromFavListView(int position) {
        beers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, beers.size());
    }


    @Override
    public void onBindViewHolder(final BeersViewHolder holder, final int position) {
        final Beer beer = beers.get(position);

        holder.tvRank.setText(String.valueOf(position+1));
        holder.imBeer.setImageResource(beer.imageID);
        holder.tvBeerTitle.setText(beer.beerName);
        holder.tvRating.setText(String.valueOf(beer.getBeerRating()));

        final boolean isFavorite = true; //[CHANGE] actually look up if true or not in user favs array

        if (isFavorite) {
            holder.favButton.setImageResource(R.drawable.x45);
        }
        else{
            holder.favButton.setImageResource(R.drawable.star45);
        }

        holder.favButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                boolean removeBeerFromView = false;

                if (isFavorite) {
                    Toast.makeText(v.getContext(), "You unfaved " + beer.beerName, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.star45);

                    //context is : org.com2027.group11.beerhere.FavoritesActivity@fd74857
                    //if the current activity is favourites
                    if (v.getContext().toString().substring(29,32).equals("Fav")){
                        removeBeerFromView = true;
                    }

                }
                else{
                    Toast.makeText(v.getContext(), "You faved " + beer.beerName, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.x45);
                }

                //[FIREBASE] update beer fav status
                if (isFavorite){
                    removeBeerFromUserFavs(position);
                }
                else{
                    addBeerToUserFavs(position);
                }

                //only true if unfaved beer while in favs view
                if (removeBeerFromView){
                        //removes the beer from the list
                        removeBeerFromFavListView(position);
                }


            }
        });

        holder.ibUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "You upvoted " + beer.beerName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.ibDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "You downvoted " + beer.beerName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeBeerFromUserFavs(int position){
        String beerName = beers.get(position).beerName;
        //add "country/beer" to arraylist in user favs  and in firebase favs
        //[CHANGE]
    }

    public void addBeerToUserFavs(int position){
        String beerName = beers.get(position).beerName;
        //remove "country/beer" to arraylist in user favs  and in firebase favs
        //[CHANGE]
    }



    @Override
    public int getItemCount() {
        return beers.size();
    }

}