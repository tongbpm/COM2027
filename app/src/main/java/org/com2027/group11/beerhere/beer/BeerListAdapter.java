package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public BeersViewHolder(View itemView){
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            imBeer = itemView.findViewById(R.id.iv_beer);
            tvBeerTitle = itemView.findViewById(R.id.tv_beer_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            ibUpvote = itemView.findViewById(R.id.ib_thumbs_up);
            ibDownVote = itemView.findViewById(R.id.ib_thumbs_down);
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
        if(beer.beerImageBmp != null) {
            holder.imBeer.setImageResource(beer.imageID);
        }
        holder.tvBeerTitle.setText(beer.beerName);
        holder.tvRating.setText(String.valueOf(beer.getBeerRating()));

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