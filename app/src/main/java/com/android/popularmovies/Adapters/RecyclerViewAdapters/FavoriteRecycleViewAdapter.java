package com.android.popularmovies.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmovies.Data.MovieContract;
import com.android.popularmovies.Models.Interfaces.RecyclerViewOnClickHandler;
import com.android.popularmovies.Models.Movie;
import com.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Isingh930 on 9/21/17.
 */

public class FavoriteRecycleViewAdapter extends RecyclerView.Adapter<FavoriteRecycleViewAdapter.FavoriteViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private RecyclerViewOnClickHandler mOnClickHandler;

    public FavoriteRecycleViewAdapter(Context mContext, RecyclerViewOnClickHandler handler) {
        this.mContext = mContext;
        this.mOnClickHandler = handler;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext= parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_list_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry._ID);
        int imageURLIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_IMAGE_URL);
        int titleIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE);

        // get to the right location in the cursor
        mCursor.moveToPosition(position);

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String imageUrl = mCursor.getString(imageURLIndex);
        String titleText = mCursor.getString(titleIndex);

        //Set values
        holder.itemView.setTag(id);
        Picasso.with(mContext).load(imageUrl).into(holder.mImageView);

        holder.mTextView.setText(titleText);

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }else{
            return mCursor.getCount();
        }
    }

    public Cursor swapCursor(Cursor cursor){
        if(mCursor == cursor){
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = cursor;

        if(cursor !=null){
            notifyDataSetChanged();
        }
        return temp;
    }

    // VIEW HOLDER
    //==============================================================================================

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.favorite_list_item_image_view)  ImageView mImageView;
        @BindView(R.id.favorite_list_item_text_view)  TextView mTextView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mCursor != null){
                mCursor.moveToPosition(getAdapterPosition());
                Movie movie = setMovieData();
                mOnClickHandler.onClick(movie);
            }
        }


        private Movie setMovieData(){
            int idIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_MOVIEID);
            int imageURLIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_IMAGE_URL);
            int backgroundImageURLIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BACKGROUND_URL);
            int titleIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE);
            int releaseDateIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE);
            int overviewIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW);
            int ratingIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RATING);
            int voteCountIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT);
            int movieRuntimeIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RUNTIME);
            int budgetIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BUDGET);
            int genreIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_GENRES);
            int revenueIndex = mCursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_REVENUE);

            Movie movie = new Movie();
            movie.setID(mCursor.getInt(idIndex));
            movie.setImageURLPath(mCursor.getString(imageURLIndex));
            movie.setBackgroundImageURLPath(mCursor.getString(backgroundImageURLIndex));
            movie.setTitle(mCursor.getString(titleIndex));
            movie.setReleaseDate(mCursor.getString(releaseDateIndex));
            movie.setOverview(mCursor.getString(overviewIndex));
            movie.setRating(mCursor.getString(ratingIndex));
            movie.setVoteCount(mCursor.getInt(voteCountIndex));
            movie.setMovieRuntime(String.valueOf(mCursor.getInt(movieRuntimeIndex)));
            movie.setBudget(mCursor.getInt(budgetIndex));
            movie.setRevenue(mCursor.getInt(revenueIndex));

            ArrayList<String> genres = convertJSONStringToArrayList(mCursor.getString(genreIndex));
            if(genres != null){
                movie.setGenres(genres);
            }

            return movie;
        }


        private ArrayList<String> convertJSONStringToArrayList(String jsonString){
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jArray = jsonObject.optJSONArray("genreArray");

                ArrayList<String> listdata = new ArrayList<String>();
                if (jArray != null) {
                    for (int i=0;i<jArray.length();i++){
                        listdata.add(jArray.getString(i));
                    }
                }
                return listdata;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }

        }
    }
}

