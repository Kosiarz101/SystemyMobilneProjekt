package com.example.task9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.task9.Database.BookRepository;
import com.example.task9.Database.ReviewRepository;
import com.example.task9.Models.DatabaseModels.Review;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;

import at.huber.youtubeExtractor.Format;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class PlayerActivity extends AppCompatActivity implements SensorEventListener {

    private BookDetailsActivity book;
    private Review review;

    PlayerView playerView;
    SimpleExoPlayer player;
    boolean playWhenReady = true;
    int currentWindow = 0;
    long playbackPosition = 0;

    private SensorManager sensorManager;
    private Sensor proximitySensor;

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT >= 24)
            initPlayer();
    }

    @Override
    protected void onPause() {
        if(Util.SDK_INT < 24)
            releasePlayer();
        sensorManager.unregisterListener(PlayerActivity.this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.SDK_INT < 24 || player == null)
        {
            initPlayer();
            hideSystemUI();
        }
        sensorManager.registerListener(PlayerActivity.this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void hideSystemUI() {
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LOW_PROFILE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onStop() {
        releasePlayer();
        super.onStop();
    }

    private void releasePlayer() {
        if(player != null)
        {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        playerView = findViewById(R.id.player_view);
        review = getIntent().getParcelableExtra("review");
        initPlayer();
    }

    private void initPlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        playYoutubeVideo(review.getYtVideoUrl());
    }

    private void playYoutubeVideo(String url) {
        new YouTubeExtractor(this) {

            @Override
            protected void onExtractionComplete(@Nullable SparseArray<YtFile> ytFiles, @Nullable VideoMeta videoMeta) {
                if(ytFiles != null)
                {
                    YtFile ytFile = null;
                    if(ytFiles.get(22) != null)
                    {
                        int videoTag = 22;
                        MediaSource videoSource = new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                                .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).getUrl()));
                        player.setMediaSource(new MergingMediaSource(true, videoSource), true);
                    }
                    else if((ytFile = GetVideoAndAudioFormat(ytFiles)) != null)
                    {
                        MediaSource videoSource = new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                                .createMediaSource(MediaItem.fromUri(ytFiles.get(ytFile.getFormat().getItag()).getUrl()));
                        player.setMediaSource(new MergingMediaSource(true, videoSource), true);
                    }
                    else
                    {
                        int videoTag = 137;
                        int audioTag = 140;
                        MediaSource audioSource = new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                                .createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).getUrl()));
                        MediaSource videoSource = new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                                .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).getUrl()));
                        player.setMediaSource(new MergingMediaSource(
                                        true,
                                        videoSource,
                                        audioSource),
                                true
                        );
                    }

                    player.prepare();
                    player.setPlayWhenReady(playWhenReady);
                    player.seekTo(currentWindow, playbackPosition);

                    review.setYtOriginalTitle(videoMeta.getTitle());
                    review.setChannelName(videoMeta.getAuthor());
                }
                else
                {
                    review.setYtOriginalTitle("None");
                    review.setChannelName("None");
                }
                new ReviewRepository(getApplication()).update(review);
            }
        }.extract(url);
    }
    private YtFile GetVideoAndAudioFormat(@Nullable SparseArray<YtFile> ytFiles)
    {
        for(int i=0; i<ytFiles.size(); i++)
        {
            YtFile ytFile = ytFiles.get(ytFiles.keyAt(i));
            Format format = ytFile.getFormat();
            if(format.getAudioBitrate() > 0)
                return ytFile;
        }
        return null;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.savedReviewOptionOnlyMore:
                Intent intent = new Intent(this, SavedReviewsActivity.class);
                startActivity(intent);
                break;
            case R.id.searchBookOptionOnlyMore:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.only_more_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        if(x > 4)
        {
            startPlayer();
        }
        else
        {
            pausePlayer();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void pausePlayer(){
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }
    private void startPlayer(){
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }
}