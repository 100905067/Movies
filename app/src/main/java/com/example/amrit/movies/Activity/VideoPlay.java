package com.example.amrit.movies.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amrit.movies.BuildConfig;
import com.example.amrit.movies.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class VideoPlay extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String developerKey = BuildConfig.YOU_TUBE_API_KEY;
    private String videoUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        Intent videoPlay = getIntent();
        videoUrl = videoPlay.getStringExtra("video_path");

        YouTubePlayerView youTubeView = (YouTubePlayerView)
                findViewById(R.id.videoView);
        youTubeView.initialize(developerKey, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(videoUrl);
            youTubePlayer.setFullscreen(true);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }
}
