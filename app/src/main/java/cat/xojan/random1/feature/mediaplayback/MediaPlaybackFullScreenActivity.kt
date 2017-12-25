package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.widget.SeekBar
import cat.xojan.random1.R
import cat.xojan.random1.feature.MediaBrowserProvider
import cat.xojan.random1.feature.MediaPlayerBaseActivity
import cat.xojan.random1.injection.HasComponent
import cat.xojan.random1.injection.component.DaggerMediaPlaybackComponent
import cat.xojan.random1.injection.component.MediaPlaybackComponent
import cat.xojan.random1.injection.module.MediaPlaybackModule
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media_playback.*


class MediaPlaybackFullScreenActivity : MediaPlayerBaseActivity(),
        HasComponent<MediaPlaybackComponent>, MediaBrowserProvider {

    companion object {
        val EXTRA_START_FULLSCREEN = "EXTRA_START_FULLSCREEN"
        val EXTRA_CURRENT_MEDIA_DESCRIPTION = "EXTRA_CURRENT_MEDIA_DESCRIPTION"

        fun newIntent(context: Context): Intent {
            return Intent(context, MediaPlaybackFullScreenActivity::class.java)
        }
    }

    override val component: MediaPlaybackComponent by lazy {
        DaggerMediaPlaybackComponent.builder()
                .appComponent(applicationComponent)
                .baseActivityModule(activityModule)
                .mediaPlaybackModule(MediaPlaybackModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_playback)
        component.inject(this)

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

        button_previous.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.skipToPrevious()
        }

        button_next.setOnClickListener {
            val controller = MediaControllerCompat.getMediaController(this)
            controller.transportControls.skipToNext()
        }

        seek_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                media_timer.text = DateUtils.formatElapsedTime((progress).toLong())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                MediaControllerCompat.getMediaController(this@MediaPlaybackFullScreenActivity)
                        .transportControls.seekTo(seekBar.progress.toLong())
            }
        })
    }

    override fun onMediaControllerConnected() {
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.registerCallback(mCallback)
        updateView()
        updateDuration(controller.metadata)
        updatePlaybackState(controller.playbackState)
    }

    private fun updatePlaybackState(state: PlaybackStateCompat?) {
        if (state == null) {
            return
        }
    }

    private fun updateView() {
        val controller = MediaControllerCompat.getMediaController(this)

        Picasso.with(this)
                .load(controller?.metadata?.description?.iconUri)
                .placeholder(R.drawable.default_rac1)
                .into(podcast_art)

        val playbackState = controller?.playbackState
        when (playbackState?.state) {
            PlaybackStateCompat.STATE_PLAYING ->
                button_play_pause.setImageResource(R.drawable.ic_pause)
            PlaybackStateCompat.STATE_PAUSED ->
                button_play_pause.setImageResource(R.drawable.ic_play_arrow)
        }
    }

    private fun updateDuration(metadata: MediaMetadataCompat?) {
        metadata?.let {
            val duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt()
            seek_bar.max = duration
            val textDuration = DateUtils.formatElapsedTime((duration).toLong())
            media_duration.text = textDuration
        }
    }

    override fun onStop() {
        super.onStop()
        val controller = MediaControllerCompat.getMediaController(this)
        controller?.unregisterCallback(mCallback)
    }

    private val mCallback = object : MediaControllerCompat.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            if (metadata == null) {
                return
            }
            updateView()
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            super.onPlaybackStateChanged(state)
            updateView()
            updatePlaybackState(state)
        }
    }
}