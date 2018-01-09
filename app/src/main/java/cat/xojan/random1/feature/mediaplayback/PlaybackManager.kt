package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.ResultReceiver
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

class PlaybackManager(appContext: Context, val queueManager: QueueManager,
                      private val listener: PlaybackStateListener, audioManager: AudioManager):
        PlayerListener {

    private val TAG = PlaybackManager::class.simpleName
    val player = Player(appContext, this, audioManager)

    val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            Log.d(TAG, "onPlay")
            handlePlayRequest()
        }

        override fun onSkipToQueueItem(queueId: Long) {
            Log.d(TAG, "OnSkipToQueueItem: " + queueId)
        }

        override fun onSkipToNext() {
            val nextMediaId = queueManager.getNextMediaId()
            Log.d(TAG, "skipToNext: " + nextMediaId)
            nextMediaId?.let {
                handlePlayRequest(nextMediaId)
                queueManager.updateMetadata(nextMediaId)
            }
        }

        override fun onSkipToPrevious() {
            Log.d(TAG, "skipToPrevious")
            val previousMediaId = queueManager.getPreviousMediaId()
            Log.d(TAG, "nextMediaId: " + previousMediaId)
            handlePlayRequest(previousMediaId)
            queueManager.updateMetadata(previousMediaId)
        }

        override fun onPause() {
            Log.d(TAG, "onPause")
            handlePauseRequest()
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            Log.d(TAG, "onPlayFromMediaId: " + mediaId)
            queueManager.setQueue(mediaId)
            handlePlayRequest(mediaId)
        }

        override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {
            Log.d(TAG, "onCommand " + command)
        }

        override fun onSeekTo(pos: Long) {
            Log.d(TAG, "onSeekTo: " + pos)
            player.seekTo(pos)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            Log.i(TAG, "onCustomAction: " + action)
        }
    }

    override fun onCompletion() {
        val nextMediaId = queueManager.getNextMediaId()
        nextMediaId?.let {
            handlePlayRequest(nextMediaId)
            queueManager.updateMetadata(nextMediaId)
        }
    }

    override fun onPlaybackStatusChanged(state: Int) {
        if (state != PlaybackStateCompat.STATE_STOPPED) {
            val position: Long = player.getCurrentPosition()
            val stateBuilder: PlaybackStateCompat.Builder = PlaybackStateCompat.Builder()
                    .setActions(getAvailableActions())

            // Set the activeQueueItemId if the current index is valid.
            val currentMediaId = queueManager.getCurrentMediaId()
            if (currentMediaId != -1L) {
                stateBuilder.setActiveQueueItemId(currentMediaId)
            }

            stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime())
            listener.updatePlaybackState(stateBuilder.build())
        }

        if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_PAUSED) {
            listener.onNotificationRequired()
        }
    }

    override fun onError(error: String) {
        Log.i(TAG, "onError")
    }

    private fun getAvailableActions(): Long {
        var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT

        actions = if (player.isPlaying()) {
            actions or PlaybackStateCompat.ACTION_PAUSE
        } else {
            actions or PlaybackStateCompat.ACTION_PLAY
        }
        return actions
    }

    fun handlePlayRequest(mediaId: String? = null) {
        Log.d(TAG, "handlePlayRequest: mediaId= " + mediaId)
        val currentMedia = queueManager.getMediaItem(mediaId)
        listener.onPlaybackStart()
        player.play(currentMedia)
    }

    fun handlePauseRequest() {
        Log.d(TAG, "handlePauseRequest")
        if(player.isPlaying()) {
            player.pause()
            listener.onPlaybackStop()
        }
    }

    fun handleStopRequest() {
        Log.d(TAG, "handleStopRequest")
        player.release()
        listener.onPlaybackStop()
    }
}