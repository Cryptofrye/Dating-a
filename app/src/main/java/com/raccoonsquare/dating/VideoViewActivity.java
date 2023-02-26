package com.raccoonsquare.dating;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.raccoonsquare.dating.common.ActivityBase;


public class VideoViewActivity extends ActivityBase {

    Toolbar toolbar;

    private PlayerView mVideoView;
    private ExoPlayer mPlayer;

    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_view);

        getSupportActionBar().hide();

        Intent i = getIntent();

        videoUrl = i.getStringExtra("videoUrl");


        //

        mVideoView = findViewById(R.id.video_view);

        mPlayer = new ExoPlayer.Builder(this).build();

        mVideoView.setUseController(true);
        mVideoView.requestFocus();
        mVideoView.setPlayer(mPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));

//        ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
//        MediaSource firstSource = new ProgressiveMediaSource.Factory(new FileDataSource.Factory()).createMediaSource(mediaItem);
//        mediaSource.addMediaSource(firstSource);

        if (mPlayer != null) {

            mPlayer.addMediaItem(mediaItem);
            mPlayer.prepare();
            mPlayer.setPlayWhenReady(true);

            mPlayer.addListener(new Player.Listener() {

                @Override
                public void onPlayerError(PlaybackException error) {

                    Toast.makeText(getApplicationContext(), getString(R.string.msg_play_video_error), Toast.LENGTH_SHORT).show();

                    Log.e("as", error.getMessage());
                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if (mPlayer != null) {

            mPlayer.setPlayWhenReady(false);
            mPlayer.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {

                if (mPlayer != null) {

                    mPlayer.setPlayWhenReady(false);
                    mPlayer.stop();
                }

                finish();

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }
}
