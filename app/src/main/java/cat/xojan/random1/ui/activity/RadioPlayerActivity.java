package cat.xojan.random1.ui.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.commons.PlayerUtil;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.view.CroppedImageView;

public class RadioPlayerActivity extends BaseActivity {

    public static final String EXTRA_PODCAST = "PODCAST";
    private static final String TAG = RadioPlayerActivity.class.getSimpleName();

    @BindView(R.id.buffer_bar) ProgressBar mBufferBar;
    @BindView(R.id.seek_bar) SeekBar mSeekBar;
    @BindView(R.id.image) CroppedImageView mImage;
    @BindView(R.id.category) TextView mCategory;
    @BindView(R.id.description) TextView mDescription;
    @BindView(R.id.timer) TextView mTimer;
    @BindView(R.id.duration) TextView mDuration;
    @BindView(R.id.player) ImageView mPlayer;
    @BindView(R.id.progress_bar) ProgressBar mLoader;

    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radio_player_activity);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        Podcast podcast = bundle.getParcelable(EXTRA_PODCAST);

        mBufferBar.setMax(100);
        mBufferBar.setProgress(0);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

        mCategory.setText(podcast.category());
        mDescription.setText(podcast.description());
        Picasso.with(this)
                .load(podcast.imageUrl())
                .into(mImage);

        startMediaPlayer(podcast.link());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopMediaPlayer();
    }

    private void startMediaPlayer(String url) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            Log.d(TAG, "setDataSource: " + url);
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_loading_podcasts), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mMediaPlayer.prepareAsync();
        Log.d(TAG, "prepareAsync");
        mMediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
        mMediaPlayer.setOnBufferingUpdateListener(new BufferingUpdateListener());
        mMediaPlayer.setOnCompletionListener(new MediaPlayerCompletionListener());
    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            Log.d(TAG, "stopMediaPlayer");
            mMediaPlayer.stop();
            mHandler.removeCallbacks(mUpdateTimeTask);
            mMediaPlayer.release();
        }
    }

    private class MediaPlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, "onPrepared");
            mLoader.setVisibility(View.GONE);
            mPlayer.setVisibility(View.VISIBLE);
            mPlayer.setOnClickListener(new PlayerButtonClickListener());
            updateDuration(mMediaPlayer.getDuration());
            mMediaPlayer.start();
            updateSeekBar();
        }
    }

    private void updateDuration(int duration) {
        mDuration.setText(PlayerUtil.millisToDuration(duration));
    }

    /**
     * Update timer on seek bar.
     */
    private void updateSeekBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread.
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            int currentDuration = mMediaPlayer.getCurrentPosition();
            int progress = PlayerUtil.getProgressPercentage(currentDuration, totalDuration);

            mSeekBar.setProgress(progress);
            mTimer.setText(PlayerUtil.millisToDuration(currentDuration));

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    private class BufferingUpdateListener implements MediaPlayer.OnBufferingUpdateListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mMediaPlayer.isPlaying()) {
                mBufferBar.setProgress(percent);
            }
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTask);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mMediaPlayer.getDuration();
            int currentPosition = PlayerUtil.progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mMediaPlayer.seekTo(currentPosition);

            // update timer progress again
            updateSeekBar();
        }
    }

    private class PlayerButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mPlayer.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow));
            } else {
                mPlayer.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                mMediaPlayer.start();
            }
        }
    }

    private class MediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mPlayer.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow));
            mMediaPlayer.seekTo(0);
        }
    }
}
