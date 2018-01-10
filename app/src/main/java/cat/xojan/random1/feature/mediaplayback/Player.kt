package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.PowerManager
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log



class Player(appContext: Context,
             private val listener: PlayerListener,
             private val audioManager: AudioManager): AudioManager.OnAudioFocusChangeListener {

    private val TAG = Player::class.simpleName
    private val mediaPlayer: MediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setWakeMode(appContext, PowerManager.PARTIAL_WAKE_LOCK)

        val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

        mediaPlayer.setAudioAttributes(audioAttributes)
        mediaPlayer.setVolume(1.0f, 1.0f)
        mediaPlayer.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.release()
        }
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun play(currentMedia: MediaMetadataCompat? = null) {
        val result = audioManager
                .requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (currentMedia != null) {
                mediaPlayer.reset()
                val mediaUri = currentMedia.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
                Log.d(TAG, mediaUri)
                mediaPlayer.setDataSource(mediaUri)
                mediaPlayer.setOnPreparedListener {
                    play()
                }
                mediaPlayer.setOnCompletionListener {
                    listener.onCompletion()
                }
                mediaPlayer.prepareAsync()
                listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_BUFFERING)
            } else {
                mediaPlayer.start()
                listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
            }
        }
    }

    fun pause() {
        mediaPlayer.pause()
        listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        audioManager.abandonAudioFocus(this)
    }

    fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    fun release() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.release()

        listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_STOPPED)

        audioManager.abandonAudioFocus(this)
    }

    fun seekTo(pos: Long) {
        mediaPlayer.seekTo((pos).toInt())
        if (mediaPlayer.isPlaying) {
            listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PLAYING)
        } else {
            listener.onPlaybackStatusChanged(PlaybackStateCompat.STATE_PAUSED)
        }
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
}