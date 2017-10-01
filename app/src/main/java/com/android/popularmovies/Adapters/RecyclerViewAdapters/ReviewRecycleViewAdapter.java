package com.android.popularmovies.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.popularmovies.Models.Review;
import com.android.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Isingh930 on 9/18/17.
 */

public class ReviewRecycleViewAdapter extends RecyclerView.Adapter<ReviewRecycleViewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<Review> mReviews;

    public void setReviews(ArrayList<Review> data){
        mReviews = data;
        notifyDataSetChanged();
    }

    public void addToReviews(ArrayList<Review> data){
        if(data != null){
            mReviews.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.review_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutID, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mAuthorTV.setText(review.getAuthor());
        holder.mContentTV.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviews == null){
            return 0;
        }else{
            return mReviews.size();
        }
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.review_author)  TextView mAuthorTV;
        @BindView(R.id.review_content)  com.borjabravo.readmoretextview.ReadMoreTextView mContentTV;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
