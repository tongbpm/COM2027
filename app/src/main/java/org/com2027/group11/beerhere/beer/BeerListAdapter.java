package org.com2027.group11.beerhere.beer;

import android.content.Context;
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
            //set star resource here instead?
        }
    }


    @Override
    public BeersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        BeersViewHolder holder = new BeersViewHolder(view);

        return holder;
    }

    public void removeBeerFromFav(int position) {
        beers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, beers.size());
    }

    @Override
    public void onBindViewHolder(BeersViewHolder holder, int position) {
        Beer beer = beers.get(position);

        holder.tvRank.setText(String.valueOf(position+1));
        holder.imBeer.setImageResource(beer.imageID);
        holder.tvBeerTitle.setText(beer.beerName);
        holder.tvRating.setText(String.valueOf(beer.getBeerRating()));
        if (beer.favourite) {
            holder.favButton.setImageResource(R.drawable.x45);
        }
        else{
            holder.favButton.setImageResource(R.drawable.star45);
        }

        holder.favButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                boolean removeBeer = false;

                if (beer.favourite) {
                    Toast.makeText(v.getContext(), "You unfaved " + beer.beerName, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.star45);

                    //context is : org.com2027.group11.beerhere.FavoritesActivity@fd74857
                    if (v.getContext().toString().substring(29,32).equals("Fav")){
                        removeBeer = true;
                    }


                }
                else{
                    Toast.makeText(v.getContext(), "You faved " + beer.beerName, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.x45);
                }

                //[FIREBASE] update beer fav status

                    if (removeBeer){
                        removeBeerFromFav(position);
                    }
                    else {
                        beers.get(position).favourite = !beer.favourite;
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



    @Override
    public int getItemCount() {
        return beers.size();
    }

}