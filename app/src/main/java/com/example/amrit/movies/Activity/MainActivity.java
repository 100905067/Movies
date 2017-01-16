package com.example.amrit.movies.Activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.amrit.movies.Adapter.MovieAdapter;
import com.example.amrit.movies.AsyncTask.SearchMovieAsyncTask;
import com.example.amrit.movies.Interface.AsyncResponse;
import com.example.amrit.movies.Interface.OnItemClick;
import com.example.amrit.movies.Manager.NetworkManager;
import com.example.amrit.movies.Pojos.Movie;
import com.example.amrit.movies.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClick {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe;

    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.movieView);
        swipe = (SwipeRefreshLayout) findViewById(R.id.activity_main);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(movieList, this);

        recyclerView.setAdapter(movieAdapter);

        movieAdapter.onItemClick = this;

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateMovies();
                if (swipe.isRefreshing())
                    swipe.setRefreshing(false);
            }
        });
    }

    public void populateMovies() {
        if(!NetworkManager.getInstance(getApplicationContext()).isOnline()) {
            Toast.makeText(getApplicationContext(), "You are offline!!", Toast.LENGTH_SHORT).show();
            return;
        }
        SearchMovieAsyncTask searchMovies = new SearchMovieAsyncTask();
        searchMovies.mResponse = new AsyncResponse() {
            @Override
            public void processFinish(List<Movie> mList) {
                movieList.clear();
                movieAdapter.notifyDataSetChanged();
                movieList.addAll(mList);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void processFinish(String key){}
        };
        searchMovies.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateMovies();
    }

    @Override
    public void onItemClickListener(Movie movie) {
        Intent intent = null;
        if(movie.getVote_average()>5) {
            intent = new Intent(this, VideoPlay.class);
            intent.putExtra("video_path", movie.getVideo_path());
        }
        else {
            intent = new Intent(this, DescriptionActivity.class);
            intent.putExtra("detail", movie);
        }

        startActivity(intent);
    }
}
