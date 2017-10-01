package com.android.popularmovies.Adapters.RecyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmovies.BuildConfig;
import com.android.popularmovies.Models.Trailer;
import com.android.popularmovies.R;
import com.android.popularmovies.Utils.JsonUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isingh930 on 9/18/17.
 */

public class TrailerRecycleViewAdapter extends RecyclerView.Adapter<TrailerRecycleViewAdapter.TrailerAdapterViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener;
    private boolean haveAPIKey = false;
    private Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;


    public TrailerRecycleViewAdapter(){
        this.thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
    }

    public void releaseLoaders(){
        for (YouTubeThumbnailLoader loader : this.thumbnailViewToLoaderMap.values()) {
            loader.release();
        }
    }

    public void setTrailers(ArrayList<Trailer> data){
        mTrailers = data;
        notifyDataSetChanged();
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID;

        try{
            if(BuildConfig.GOOGLE_DEVELOPER_CONSOLE_API_KEY.contentEquals("")){
                layoutID = R.layout.trailer_list_item_original;
            }else{
                layoutID = R.layout.trailer_list_item;
                haveAPIKey = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            layoutID = R.layout.trailer_list_item_original;

        }


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutID, parent, false);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, final int position) {
        Trailer trailer = mTrailers.get(position);
        if(haveAPIKey){
            onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                }

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    youTubeThumbnailView.setVisibility(View.VISIBLE);
                }
            };

            holder.youTubeThumbnailView.initialize(BuildConfig.GOOGLE_DEVELOPER_CONSOLE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(mTrailers.get(position).getVideoKey());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);

                    thumbnailViewToLoaderMap.put(youTubeThumbnailView, youTubeThumbnailLoader);

                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //write something for failure
                }
            });
            holder.mVideoName.setText(trailer.getName());

        }else{
            holder.mVideoDescription.setText(trailer.getName());
            String test = JsonUtils.getFullYoutubeURL(trailer.getVideoKey());
            holder.setOnClickURL(JsonUtils.getFullYoutubeURL(trailer.getVideoKey()));
        }
    }

    @Override
    public int getItemCount() {
        if(mTrailers == null){
            return 0;
        }else{
            return mTrailers.size();
        }
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // HAVE API KEY
        private YouTubeThumbnailView youTubeThumbnailView;
        private TextView mVideoName;

        // DON'T HAVE API KEY
        private TextView mVideoDescription;
        private ImageView mPlayImageView;

        // ON CLICK LISTENER URL
        private String onClickURL;

        public void setOnClickURL(String onClickURL) {
            this.onClickURL = onClickURL;
        }

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            if(haveAPIKey){
                mVideoName = (TextView) itemView.findViewById(R.id.video_name);
                youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
                youTubeThumbnailView.setOnClickListener(this);
            }else{
                mVideoDescription = (TextView) itemView.findViewById(R.id.video_description_original_layout);
                mPlayImageView = (ImageView) itemView.findViewById(R.id.video_image_original_layout);
                mPlayImageView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            Context ctx = v.getContext();
            if(haveAPIKey){
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, BuildConfig.GOOGLE_DEVELOPER_CONSOLE_API_KEY, mTrailers.get(getLayoutPosition()).getVideoKey());
                ctx.startActivity(intent);
            }else{
                if(onClickURL != null){
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(onClickURL));
                    v.getContext().startActivity(trailerIntent);
                }
            }
        }

    }
}