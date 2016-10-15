package cat.xojan.random1.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.commons.PicassoUtil;
import cat.xojan.random1.commons.PlayerUtil;
import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.injection.component.DaggerRadioPlayerComponent;
import cat.xojan.random1.injection.component.RadioPlayerComponent;
import cat.xojan.random1.injection.module.RadioPlayerModule;
import cat.xojan.random1.service.RadioPlayerService;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.view.CroppedImageView;
import icepick.Icepick;
import icepick.State;

public class RadioPlayerActivity extends BaseActivity implements RadioPlayerService.Listener {

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

    @State int mPlayerDuration = -1;
    @State boolean mPlayerStarted = false;
    @State int mPlayerButtonDrawable = -1;
    @State Podcast mPodcast;

    private RadioPlayerService mService;
    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            RadioPlayerService.RadioPlayerServiceBinder binder =
                    (RadioPlayerService.RadioPlayerServiceBinder) service;
            mService = binder.getServiceInstance();
            mService.registerClient(RadioPlayerActivity.this);
            mBound = true;

            if (mPlayerDuration == -1 && !mPlayerStarted) {
                Log.d(TAG, "StartService");
                startService(mServiceIntent);
                mPlayerStarted = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    private Intent mServiceIntent;

    private Intent getRadioPlayerServiceIntent(Podcast podcast) {
        Intent intent = new Intent(this, RadioPlayerService.class);
        intent.addCategory(RadioPlayerService.TAG);
        intent.putExtra(RadioPlayerService.EXTRA_PODCAST, podcast);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.radio_player_activity);
        ButterKnife.bind(this);

        mPodcast = getIntent().getParcelableExtra(EXTRA_PODCAST);

        initInjector();
        initView(mPodcast);

        // Bind to the service
        Log.d(TAG, "BindService");
        mServiceIntent = getRadioPlayerServiceIntent(mPodcast);
        bindService(new Intent(this, RadioPlayerService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            Log.d(TAG, "UnbindService");
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        stopService(mServiceIntent);
        super.onBackPressed();
    }

    private void initView(Podcast podcast) {
        mBufferBar.setMax(100);
        mBufferBar.setProgress(0);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

        mCategory.setText(podcast.getProgram());
        mDescription.setText(podcast.getDescription());
        PicassoUtil.loadImage(this, podcast.getImageDrawable(), mImage, false);

        updateViewAfterRotation();
    }

    private void updateViewAfterRotation() {
        if (mPlayerDuration != -1) {
            mLoader.setVisibility(View.GONE);
            mPlayer.setVisibility(View.VISIBLE);
            if (mPlayerButtonDrawable != -1) {
                mPlayer.setImageResource(mPlayerButtonDrawable);
            }
            mPlayer.setOnClickListener(new PlayerButtonClickListener());
            updateDuration(mPlayerDuration);
        }
    }

    private void initInjector() {
        RadioPlayerComponent mComponent = DaggerRadioPlayerComponent.builder()
                .appComponent(getApplicationComponent())
                .baseActivityModule(getActivityModule())
                .radioPlayerModule(new RadioPlayerModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void onPrepared(int duration) {
        mLoader.setVisibility(View.GONE);
        mPlayer.setVisibility(View.VISIBLE);
        mPlayer.setOnClickListener(new PlayerButtonClickListener());
        updateDuration(duration);
        mPlayerDuration = duration;
    }

    @Override
    public void progressUpdate(int progress, int currentDuration) {
        mSeekBar.setProgress(progress);
        mTimer.setText(PlayerUtil.millisToDuration(currentDuration));
    }

    @Override
    public void updateBufferProgress(int percent) {
        mBufferBar.setProgress(percent);
    }

    @Override
    public void updateButton(int drawable) {
        mPlayerButtonDrawable = R.drawable.ic_play_arrow;
        mPlayer.setImageResource(mPlayerButtonDrawable);
    }

    private void updateDuration(int duration) {
        mDuration.setText(PlayerUtil.millisToDuration(duration));
    }

    private class PlayerButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mService.isPlaying()) {
                mService.pause();
                mPlayerButtonDrawable = R.drawable.ic_play_arrow;
                mPlayer.setImageResource(mPlayerButtonDrawable);
            } else {
                mPlayerButtonDrawable = R.drawable.ic_pause;
                mPlayer.setImageResource(mPlayerButtonDrawable);
                mService.start();
            }
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mService.removeCallbacks();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mService.removeCallbacks();
            int totalDuration = mService.getDuration();
            int currentPosition = PlayerUtil.progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mService.seekTo(currentPosition);

            // update timer progress again
            mService.updateSeekBar();
        }
    }
}
