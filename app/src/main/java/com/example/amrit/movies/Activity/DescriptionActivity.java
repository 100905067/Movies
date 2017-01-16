package com.example.amrit.movies.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.amrit.movies.Pojos.Movie;
import com.example.amrit.movies.R;
import com.squareup.picasso.Picasso;

public class DescriptionActivity extends AppCompatActivity {

    private static final String LOG_TAG = DescriptionActivity.class.getSimpleName() ;
    private ImageView image;
    private TextView title,overview,ratingTxt, popularityTxt;
    private RatingBar rating;
    private Button watch;

    private String base_url = "https://image.tmdb.org/t/p/w185";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        image = (ImageView) findViewById(R.id.descImg);
        title = (TextView) findViewById(R.id.descTitle);
        overview = (TextView) findViewById(R.id.descOverview);
        rating = (RatingBar) findViewById(R.id.rating);
        ratingTxt = (TextView)findViewById(R.id.ratinTxt) ;
        popularityTxt = (TextView) findViewById(R.id.descPopularity);
        watch = (Button) findViewById(R.id.watch);

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("detail");

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        rating.setRating(movie.getVote_average());
        ratingTxt.setText("Rating : "+movie.getVote_average());
        popularityTxt.setText("Popularity :"+movie.getPopularity());
        Picasso.with(getApplicationContext()).load(base_url+movie.getPoster_path()).into(image);

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchVideo(movie);
            }
        });
    }

    public void watchVideo(Movie movie) {
        Intent intent = new Intent(this, VideoPlay.class);
        intent.putExtra("video_path", movie.getVideo_path());
        startActivity(intent);
    }
}
