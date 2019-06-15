package com.mobiweb.ibrahim.agenda.activities.director.videos;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;


public class ActivityVideo extends ActivityBase {

    private String videoURL;
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer player;
    private boolean isAutoRotation = false;
    private long startFrom = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoURL = getIntent().getExtras().getString(AppConstants.VIDEO_URL);
        isAutoRotation = Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(player != null){
            startFrom = player.getCurrentPosition();
            player.stop();
            player.release();
            exoPlayerView.setPlayer(null);
            player = null;
            exoPlayerView = null;
        }
        videoURL = null;
    }





    private void init(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.BLACK);
        setContentView(R.layout.activity_video);
       initExoPlayer();
    }

    private void initExoPlayer(){
        exoPlayerView = (SimpleExoPlayerView)findViewById(R.id.exoplayerView);
        exoPlayerView.setUseController(true);
        exoPlayerView.showController();
        exoPlayerView.requestFocus();

        if(player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            // Prepare the player with the source.
            player.prepare(new ExtractorMediaSource(Uri.parse(videoURL), new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null), true, false);
            player.seekTo(startFrom);
            player.setPlayWhenReady(true);
        }
        exoPlayerView.setPlayer(player);
    }


}
