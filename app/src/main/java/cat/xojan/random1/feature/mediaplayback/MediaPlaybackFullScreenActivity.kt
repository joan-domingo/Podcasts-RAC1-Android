package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import cat.xojan.random1.R
import cat.xojan.random1.feature.MediaBrowserProvider
import cat.xojan.random1.feature.MediaPlayerBaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media_playback.*

class MediaPlaybackFullScreenActivity : MediaPlayerBaseActivity(), MediaBrowserProvider {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MediaPlaybackFullScreenActivity::class.java)
        }
    }

    private val TAG = MediaPlaybackFullScreenActivity::class.java.simpleName

    private val handler = Handler()
    private val updateTimerTask = object : Runnable {
        override fun run() {
            val controller = MediaControllerCompat
                    .getMediaController(this@MediaPlaybackFullScreenActivity)
            controller?.playbackState?.let {
                updateProgress(controller.playbackState)
            }
            handler.postDelayed(this, 250)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_playback)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_play_pause.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            when (controller.playbackState.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    controller.transportControls.pause()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    controller.transportControls.play()
                }
            }
        }

        button_fast_rewind.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.rewind()
        }

        button_previous.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.skipToPrevious()
        }

        button_next.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.skipToNext()
        }

        button_fast_forward.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.fastForward()
        }

        seek_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                media_timer.text = DateUtils.formatElapsedTime((progress/1000).toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                handler.removeCallbacks(updateTimerTask)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                handler.removeCallbacks(updateTimerTask)
                MediaControllerCompat.getMediaController(this@MediaPlaybackFullScreenActivity)
                        .transportControls.seekTo(seekBar.progress.toLong())
                handler.postDelayed(updateTimerTask, 100)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowser.let {
            if (mMediaBrowser.isConnected) {
                Log.d(TAG, "onStart: connected")
                onMediaControllerConnected()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(updateTimerTask, 0)
    }

    override fun onMediaControllerConnected() {
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.registerCallback(mCallback)
        updateView()
        updateDuration(controller.metadata)
    }

    private fun updateView() {
        val controller = MediaControllerCompat.getMediaController(this)

        val metadata = controller.metadata
        metadata?.let {
            title = metadata.description.title
            Picasso.with(this)
                    .load(metadata.description?.iconUri)
                    .resize(800, 800)
                    .placeholder(R.drawable.default_rac1)
                    .into(podcast_art)
        }

        val playbackState = controller.playbackState
        playbackState?.let {
            when (playbackState.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    buffer_progress_bar.visibility = View.GONE
                    button_play_pause.visibility = View.VISIBLE
                    button_play_pause.setImageResource(R.drawable.ic_pause)
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    buffer_progress_bar.visibility = View.GONE
                    button_play_pause.visibility = View.VISIBLE
                    button_play_pause.setImageResource(R.drawable.ic_play_arrow)
                }
                PlaybackStateCompat.STATE_BUFFERING -> {
                    buffer_progress_bar.visibility = View.VISIBLE
                    button_play_pause.visibility = View.GONE
                }
            }
        }
    }

    private fun updateDuration(metadata: MediaMetadataCompat?) {
        metadata?.let {
            val duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
            seek_bar.max = duration
            val textDuration = DateUtils.formatElapsedTime(((duration / 1000).toLong()))
            media_duration.text = textDuration
        }
    }

    override fun onStop() {
        super.onStop()
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.unregisterCallback(mCallback)
        handler.removeCallbacks(updateTimerTask)
    }

    private fun updateProgress(playbackState: PlaybackStateCompat) {
        var currentPosition = playbackState.position
        if (playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
            // Calculate the elapsed time between the last position update and now and unless
            // paused, we can assume (delta * speed) + current position is approximately the
            // latest position. This ensure that we do not repeatedly call the getPlaybackState()
            // on MediaControllerCompat.
            val timeDelta = SystemClock.elapsedRealtime() - playbackState.lastPositionUpdateTime
            currentPosition += timeDelta.toInt() * playbackState.playbackSpeed.toLong()
        }
        seek_bar.progress = currentPosition.toInt()
        media_timer.text = DateUtils.formatElapsedTime(currentPosition/1000)
    }

    private val mCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            if (metadata == null) {
                return
            }
            updateView()
            updateDuration(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            super.onPlaybackStateChanged(state)
            updateView()
        }
    }
}