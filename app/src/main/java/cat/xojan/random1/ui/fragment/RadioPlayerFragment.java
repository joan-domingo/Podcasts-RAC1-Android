package cat.xojan.random1.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.commons.PlayerUtil;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.injection.component.RadioPlayerComponent;
import cat.xojan.random1.service.RadioPlayerService;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.view.CroppedImageView;
import icepick.Icepick;
import icepick.State;

public class RadioPlayerFragment extends BaseFragment implements RadioPlayerService.Listener {

    private static final String ARG_PODCAST = "arg_podcast";
    public static final String TAG = RadioPlayerFragment.class.getSimpleName();

    @BindView(R.id.buffer_bar) ProgressBar mBufferBar;
    @BindView(R.id.seek_bar) SeekBar mSeekBar;
    @BindView(R.id.image) CroppedImageView mImage;
    @BindView(R.id.category) TextView mCategory;
    @BindView(R.id.description) TextView mDescription;
    @BindView(R.id.timer) TextView mTimer;
    @BindView(R.id.duration) TextView mDuration;
    @BindView(R.id.player) ImageView mPlayer;
    @BindView(R.id.progress_bar) ProgressBar mLoader;

    private MusicPlayerServiceConnection mConnection;
    private RadioPlayerService mService;

    @State int mPlayerButtonDrawable = -1;
    @State int mPlayerDuration = -1;

    public static RadioPlayerFragment newInstance(Podcast podcast) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PODCAST, podcast);

        RadioPlayerFragment radioPlayerFragment = new RadioPlayerFragment();
        radioPlayerFragment.setArguments(args);

        return radioPlayerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setRetainInstance(true);
        startService(((Podcast) getArguments().getParcelable(ARG_PODCAST)).link());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.radio_player_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBufferBar.setMax(100);
        mBufferBar.setProgress(0);
        mSeekBar.setProgress(0);
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

        Podcast podcast = getArguments().getParcelable(ARG_PODCAST);
        mCategory.setText(podcast.category());
        mDescription.setText(podcast.description());
        Picasso.with(getActivity())
                .load(podcast.imageUrl())
                .into(mImage);

        updateViewAfterRotation();
    }

    private void updateViewAfterRotation() {
        if (mPlayerDuration != -1) {
            mLoader.setVisibility(View.GONE);
            mPlayer.setVisibility(View.VISIBLE);
            if (mPlayerButtonDrawable != -1) {
                mPlayer.setImageDrawable(getResources().getDrawable(mPlayerButtonDrawable));
            }
            mPlayer.setOnClickListener(new PlayerButtonClickListener());
            updateDuration(mPlayerDuration);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(RadioPlayerComponent.class).inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        stopService();
        super.onDestroy();
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
        mPlayer.setImageDrawable(getResources().getDrawable(drawable));
    }

    private void startService(String url) {
        Intent intent = getRadioPlayerServiceIntent();
        intent.putExtra(RadioPlayerService.EXTRA_URL, url);

        //bind service
        Log.d(TAG, "BindService");
        mConnection = new MusicPlayerServiceConnection();
        getActivity().getApplicationContext()
                .bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //start service
        Log.d(TAG, "StartService");
        getActivity().startService(intent);
    }

    private void stopService() {
        getActivity().getApplicationContext().unbindService(mConnection);
        getActivity().stopService(getRadioPlayerServiceIntent());
    }

    private Intent getRadioPlayerServiceIntent() {
        Intent intent = new Intent(getActivity().getApplicationContext(), RadioPlayerService.class);
        intent.addCategory(RadioPlayerService.TAG);
        return intent;
    }

    private void updateDuration(int duration) {
        mDuration.setText(PlayerUtil.millisToDuration(duration));
    }

    private class MusicPlayerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RadioPlayerService.RadioPlayerServiceBinder binder =
                    (RadioPlayerService.RadioPlayerServiceBinder) service;
            mService = binder.getServiceInstance();
            mService.registerClient(RadioPlayerFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private class PlayerButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mService.isPlaying()) {
                mService.pause();
                mPlayerButtonDrawable = R.drawable.ic_play_arrow;
                mPlayer.setImageDrawable(getResources().getDrawable(mPlayerButtonDrawable));
            } else {
                mPlayerButtonDrawable = R.drawable.ic_pause;
                mPlayer.setImageDrawable(getResources().getDrawable(mPlayerButtonDrawable));
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
