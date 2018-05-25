package org.com2027.group11.beerhere.beer;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;

import org.com2027.group11.beerhere.R;
import org.com2027.group11.beerhere.utilities.StorageHandler;

import java.util.Collections;
import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeersViewHolder> {

    private static final String TAG = "BEER-HERE" ;
    private LayoutInflater inflater;

    private List<Beer> beers = Collections.emptyList();

    private Context context;

    public BeerListAdapter(Context context, List<Beer> beers){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.beers = beers;
    }

    class BeersViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank;
        SimpleDraweeView imBeer;
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
        Log.i("beer-here", "attempting to set beer adapter value bitmap");
        if(beer.beerImageBmp != null) {
            holder.imBeer.setImageBitmap(beer.beerImageBmp);
        }
        holder.tvBeerTitle.setText(beer.name);
        holder.tvRating.setText(String.valueOf(beer.getRating()));

        StorageHandler.setImageFromFirebase(beer.imageID, holder.imBeer);


        holder.ibUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public int getItemCount() {
        return beers.size();
    }
}