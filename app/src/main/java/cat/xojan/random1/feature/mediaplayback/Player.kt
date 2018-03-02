package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.PowerManager
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import cat.xojan.random1.domain.model.EventLogger

class Player(appContext: Context,
             private val listener: PlayerListener,
             private val audioManager: AudioManager,
             private val eventLogger: EventLogger) : AudioManager.OnAudioFocusChangeListener {

    private val TAG = Player::class.simpleName
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var countDownTimer: CountDownTimer? = null
    private var timerMilliseconds: Long = 0L
    private var timerLabel: String? = null


    init {
        mediaPlayer.setWakeMode(appContext, PowerManager.PARTIAL_WAKE_LOCK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()

            mediaPlayer.setAudioAttributes(audioAttributes)
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }

        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.release()
        }
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun play(currentMedia: MediaMetadataCompat? = null, hasNext: Boolean) {
        @Suppress("DEPRECATION")
        val result = audioManager
                .requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (currentMedia != null) {
                mediaPlayer.reset()
                val mediaUri: String? =
                        currentMedia.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
                Log.d(TAG, mediaUri)
                try {
                    mediaPlayer.setDataSource(mediaUri)
                    mediaPlayer.setOnPreparedListener {
                        startPlaying()
                    }
                    mediaPlayer.setOnCompletionListener {
                        listener.onCompletion()
                    }
                    mediaPlayer.setOnErrorListener { _, _, _ ->
                        if (mediaUri != null && !mediaUri.contains("http")) {
                            false
                        } else {
                            !hasNext
                        }
                    }
                    mediaPlayer.prepareAsync()
                    listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_BUFFERING)
                } catch (e: Exception) {
                    pause()
                }
                eventLogger.logPlayedPodcast(currentMedia)
            } else {
                startPlaying()
            }
        }
    }

    private fun startPlaying() {
        listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
        listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        @Suppress("DEPRECATION")
        audioManager.abandonAudioFocus(this)
    }

    fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    fun release() {
        if (isPlaying()) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.release()

        listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_STOPPED)

        @Suppress("DEPRECATION")
        audioManager.abandonAudioFocus(this)
        countDownTimer?.cancel()
    }

    fun seekTo(pos: Long) {
        mediaPlayer.seekTo((pos).toInt())
        notifyListener()
    }

    fun rewind() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition - 30000)
        notifyListener()
    }

    fun forward() {
        mediaPlayer.seekTo(mediaPlayer.currentPosition + 30000)
        notifyListener()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                Log.d(TAG, "resume playback")
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(TAG, "Stop playback but don't release media player")
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(TAG, "keep playing at an attenuated level")
                mediaPlayer.setVolume(0.1f, 0.1f)
            }
        }
    }

    private fun notifyListener() {
        if (mediaPlayer.isPlaying) {
            listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
        } else {
            listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        }
    }

    fun setSleepTimer(milliseconds: Long?, label: String?) {
        milliseconds?.let {
            timerMilliseconds = milliseconds
            timerLabel = label
            if (milliseconds == 0L) {
                countDownTimer?.cancel()
            } else {
                countDownTimer = object: CountDownTimer(milliseconds, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}

                    override fun onFinish() {
                        timerMilliseconds = 0
                        pause()
                    }
                }.start()
            }
            notifyListener()
        }
    }

    fun getTimerMilliseconds(): Long {
        return timerMilliseconds
    }

    fun getTimerLabel(): String? {
        return timerLabel
    }
}