package cat.xojan.random1.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.commons.PlayerUtil;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;

public class RadioPlayerService extends Service {

    public static final String TAG = RadioPlayerService.class.getSimpleName();
    public static final String EXTRA_PODCAST = "extra_podcast";
    private static final int NOTIFICATION_ID = 1;

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private IBinder mBinder;
    private Listener mListener;

    public void registerClient(Listener listener) {
        mListener = listener;
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void removeCallbacks() {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public void seekTo(int currentPosition) {
        mMediaPlayer.seekTo(currentPosition);
    }

    public class RadioPlayerServiceBinder extends Binder {
        public RadioPlayerService getServiceInstance(){
            return RadioPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        // The service is being created
        mHandler = new Handler();
        mBinder = new RadioPlayerServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mListener == null || intent == null || !intent.hasExtra(EXTRA_PODCAST)) {
            stopSelf();
        } else {
            Podcast podcast = intent.getParcelableExtra(EXTRA_PODCAST);

            Notification notification = getNotification(RadioPlayerActivity.class, podcast);
            startForeground(NOTIFICATION_ID, notification);

            startMediaPlayer(podcast.getPath(), podcast.getFilePath());
        }

        return START_REDELIVER_INTENT;
    }

    protected Notification getNotification(Class clazz, Podcast podcast) {
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext());

        builder.setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentText(podcast.getTitle());

        Intent foregroundIntent = new Intent(getApplicationContext(), clazz);

        foregroundIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                foregroundIntent, 0);

        builder.setContentIntent(contentIntent);
        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder ;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopMediaPlayer();
        stopForeground(true);
        mListener = null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (mListener != null) {
            onUnbind(rootIntent);
            onDestroy();
        }
    }

    /**
     * Update timer on seek bar.
     */
    public void updateSeekBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private void startMediaPlayer(String fileUrl, String filePath) {
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (filePath != null) {
                Log.d(TAG, "setDataSource: " + filePath);
                FileInputStream fileInputStream;
                fileInputStream = new FileInputStream(filePath);
                mMediaPlayer.setDataSource(fileInputStream.getFD());
                fileInputStream.close();
            } else {
                Log.d(TAG, "setDataSource: " + fileUrl);
                mMediaPlayer.setDataSource(fileUrl);
                mMediaPlayer.setOnBufferingUpdateListener(new BufferingUpdateListener());
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
            mMediaPlayer.setOnCompletionListener(new MediaPlayerCompletionListener());
            mMediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());

            mMediaPlayer.prepareAsync();
            Log.d(TAG, "prepareAsync()");

        } catch (IOException e) {
            ErrorUtil.logException(e);
        }
    }

    private void stopMediaPlayer() {
        Log.d(TAG, "stopMediaPlayer");
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * Background Runnable thread.
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            int currentDuration = mMediaPlayer.getCurrentPosition();
            int progress = PlayerUtil.getProgressPercentage(currentDuration, totalDuration);
            mListener.progressUpdate(progress, currentDuration);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    private class MediaPlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, "onPrepared");
            if (mListener == null) {
                stopSelf();
            } else {
                mListener.onPrepared(mMediaPlayer.getDuration());
                mMediaPlayer.start();
                updateSeekBar();
            }
        }
    }

    private class BufferingUpdateListener implements MediaPlayer.OnBufferingUpdateListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mListener.updateBufferProgress(percent);
            }
        }
    }

    private class MediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mListener != null) {
                mListener.updateButton(R.drawable.ic_play_arrow);
            }
            mMediaPlayer.seekTo(0);
        }
    }

    public interface Listener {

        void onPrepared(int duration);

        void progressUpdate(int progress, int currentDuration);

        void updateBufferProgress(int percent);

        void updateButton(int ic_play_arrow);
    }

    private class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ErrorUtil.logException("Media player error listener: " + what + ", " + extra);
            return false;
        }
    }
}
