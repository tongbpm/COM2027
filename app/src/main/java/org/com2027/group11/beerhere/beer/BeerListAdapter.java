package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.os.Bundle;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.com2027.group11.beerhere.R;
import org.com2027.group11.beerhere.user.User;
import org.com2027.group11.beerhere.utilities.StorageHandler;
import org.com2027.group11.beerhere.utilities.database.SynchronisationManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeersViewHolder> {

    private static final String TAG = "BEER-HERE" ;
    private LayoutInflater inflater;
    private FirebaseAuth mAuth; //[REMOVE?]


    private List<Beer> beers = Collections.emptyList();

    private String countryName = ""; //[CHANGE] get this as extra from intent

    private Context context;

    public BeerListAdapter(Context context, List<Beer> beers){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.beers = beers;
        mAuth = FirebaseAuth.getInstance(); //[REMOVE?]
    }

    class BeersViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        SimpleDraweeView imBeer;
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
    public void onBindViewHolder(final BeersViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        final Beer beer = beers.get(pos);
        final boolean isFavourite;
        boolean tempBool = false;

        holder.tvRank.setText(String.valueOf(pos+1));

        if(beer.beerImageBmp != null) {
            holder.imBeer.setImageBitmap(beer.beerImageBmp);
        }

        holder.tvBeerTitle.setText(beer.name);
        holder.tvRating.setText(String.valueOf(beer.upvotes - beer.downvotes));

        StorageHandler.setImageFromFirebase(beer.imageID, holder.imBeer);

       User user = SynchronisationManager.getInstance().loggedInUser;

       String beerCountry = beer.ref.toString().split("/")[4];
       String beerName = beer.ref.toString().split("/")[6];

        Log.i(TAG, "onBindViewHolder: " + "beerCountry" + beerCountry);
        Log.i(TAG, "onBindViewHolder: beerName " + beerName );

        for (String s: user.favourites){

            String favCountry = beer.ref.toString().split("/")[4];
            String favName = beer.ref.toString().split("/")[6];

            Log.i(TAG, "onBindViewHolder: " + "favCountry" + favCountry);
            Log.i(TAG, "onBindViewHolder: favName " + favName );


            if (s.split("/")[2].equals(beerCountry)
                    && s.split("/")[4].equals(beerName)){
                tempBool = true;
            }
        }

       if (tempBool){
           isFavourite = true;
       }
        else {
            isFavourite = false;
        }


        if (isFavourite) {
            holder.favButton.setImageResource(R.drawable.x45);
        }
        else{
            holder.favButton.setImageResource(R.drawable.star45);
        }

        holder.favButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                boolean removeBeerFromView = false;


                if (isFavourite) {
                    Toast.makeText(v.getContext(), "You unfaved " + beer.name, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.star45);

                    //context is : org.com2027.group11.beerhere.FavoritesActivity@fd74857
                    //if the current activity is favourites
                    Log.i(TAG, "onClick: " + this.getClass().getName());
                    if (v.getContext().toString().substring(29,32).equals("Fav")){
                        removeBeerFromView = true;
                    }

                }
                else{
                    Toast.makeText(v.getContext(), "You faved " + beer.name, Toast.LENGTH_SHORT).show();
                    holder.favButton.setImageResource(R.drawable.x45);
                }

                //[FIREBASE] update beer fav status
                //add to favs if is remove if isnt

               user.updateFavourites(beer.ref);

                //only true if unfaved beer while in favs view
                if (removeBeerFromView){
                        //removes the beer from the list
                        removeBeerFromFavListView(pos);
                }


            }
        });

        holder.ibUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "You upvoted " + beer.name, Toast.LENGTH_SHORT).show();
                beer.upvote(FirebaseAuth.getInstance().getCurrentUser().getUid(), BeerListAdapter.this.context);
                Log.d(TAG, "Upvotes: " + beer.upvotes);
                Log.d(TAG, "Downvotes: " + beer.downvotes);
                Log.d(TAG, beer.ref.toString());
            }
        });

        holder.ibDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beer.downvote(FirebaseAuth.getInstance().getCurrentUser().getUid(), BeerListAdapter.this.context);
                Log.d(TAG, "Upvotes: " + beer.upvotes);
                Log.d(TAG, "Downvotes: " + beer.downvotes);
            }
        });
    }

    public void removeBeerFromUserFavs(int position){
        String beerName = beers.get(position).name;
        //add "country/beer" to arraylist in user favs  and in firebase favs
        //[CHANGE]
    }

    public void addBeerToUserFavs(int position){
        String beerName = beers.get(position).name;
        //remove "country/beer" to arraylist in user favs  and in firebase favs
        //[CHANGE]
    }



    @Override
    public int getItemCount() {
        return beers.size();
    }

}