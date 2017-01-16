package com.example.amrit.movies.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrit.movies.AsyncTask.SearchMovieAsyncTask;
import com.example.amrit.movies.AsyncTask.SearchVideoAsyncTask;
import com.example.amrit.movies.Interface.AsyncResponse;
import com.example.amrit.movies.Interface.OnItemClick;
import com.example.amrit.movies.Manager.NetworkManager;
import com.example.amrit.movies.Pojos.Movie;
import com.example.amrit.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by amrita on 1/14/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CustomViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movieList;
    private Context context;
    private String base_url = "https://image.tmdb.org/t/p/w185";

    public OnItemClick onItemClick;

    public MovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cell, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        //check config
        int orientation = context.getResources().getConfiguration().orientation;

        String image_path = base_url;
        if(movie.getVote_average()< 5) {
            if(orientation == Configuration.ORIENTATION_LANDSCAPE)
                image_path = image_path.concat(movie.getBackdrop_path());
            else
                image_path = image_path.concat(movie.getPoster_path());
            holder.imgLay.setForeground(null);
            holder.movieTitle.setText(movie.getTitle());
            holder.movieOverview.setText(movie.getOverview());
            holder.movieTitle.setVisibility(View.VISIBLE);
            holder.movieOverview.setVisibility(View.VISIBLE);
            holder.readMore.setVisibility(View.VISIBLE);
        }
        else {
            image_path = image_path.concat(movie.getBackdrop_path());
            holder.imgLay.setForeground(context.getResources().getDrawable(R.drawable.play));
            holder.movieTitle.setVisibility(View.GONE);
            holder.movieOverview.setVisibility(View.GONE);
            holder.readMore.setVisibility(View.GONE);
        }
        Picasso.with(context).load(image_path)
                .transform(new RoundedCornersTransformation(8,8))
                .placeholder(context.getResources().getDrawable(R.drawable.defaultimg))
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView movieImage;
        TextView movieTitle, movieOverview, readMore;
        FrameLayout imgLay;

        public CustomViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            imgLay = (FrameLayout) itemView.findViewById(R.id.imgLay);
            movieImage = (ImageView) itemView.findViewById(R.id.movieImg);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieOverview = (TextView) itemView.findViewById(R.id.movieOverview);
            readMore = (TextView) itemView.findViewById(R.id.readMore);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    final Movie movie = movieList.get(pos);
                    String params = Integer.toString(movie.getId());
                    if(movie.getVideo_path()==null) {
                        if(NetworkManager.getInstance(context).isOnline() == false) {
                            Toast.makeText(context, "You are offline!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SearchVideoAsyncTask videoAsyncTask = new SearchVideoAsyncTask();
                        videoAsyncTask.mResponse = new AsyncResponse() {
                            @Override
                            public void processFinish(List<Movie> mList) {
                            }

                            @Override
                            public void processFinish(String key) {
                                movie.setVideo_path(key);
                                onItemClick.onItemClickListener(movie);
                            }
                        };
                        videoAsyncTask.execute(params);
                    }
                    else
                        onItemClick.onItemClickListener(movie);
                }
            });
        }
    }
}
