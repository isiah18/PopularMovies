package com.android.popularmovies.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.popularmovies.Models.Interfaces.RecyclerViewOnClickHandler;
import com.android.popularmovies.Models.Movie;

import com.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Isingh930 on 8/20/17.
 */

public class MovieRecycleViewAdapter extends RecyclerView.Adapter<MovieRecycleViewAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> mMovies;
    private final RecyclerViewOnClickHandler mMovieAdapterOnClickHandler;
    private final Context mContext;

    public MovieRecycleViewAdapter(RecyclerViewOnClickHandler handler, Context mainContext){
        mMovieAdapterOnClickHandler = handler;
        mContext = mainContext;
    }

    public ArrayList<Movie> getMovieData(){
        return mMovies;
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMovies = movieData;
        notifyDataSetChanged();
    }

    public void addToMovieDataSet(ArrayList<Movie> movieData){
        mMovies.addAll(movieData);
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutID, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String imageURL = movie.getImageURLPath();
        Picasso.with(mContext).load(imageURL).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null){
            return 0;
        }
        else{
            return mMovies.size();
        }
    }



    // VIEWHOLDER INNER CLASS - MOVIE ADAPTER VIEW HOLDER --> IMPLEMENTS ON CLICK LISTENER
    //===================================================================================================
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.movie_image_view) ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mMovieAdapterOnClickHandler.onClick(movie);
        }
    }
}
