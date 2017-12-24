package cat.xojan.random1.feature

import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import cat.xojan.random1.R
import cat.xojan.random1.feature.mediaplayback.MediaPlaybackControlsFragment
import cat.xojan.random1.feature.mediaplayback.MediaPlaybackService

abstract class MediaPlayerBaseActivity : BaseActivity(), MediaBrowserProvider {

    private val TAG = BaseActivity::class.simpleName
    lateinit var mMediaBrowser: MediaBrowserCompat
    private lateinit var controlsFragment: MediaPlaybackControlsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMediaBrowser = MediaBrowserCompat(
                this,
                ComponentName(this, MediaPlaybackService::class.java),
                object : MediaBrowserCompat.ConnectionCallback() {
                    override fun onConnected() {
                        try {
                            connectToSession(mMediaBrowser.sessionToken)
                        } catch (e: RemoteException) {
                            Log.e(TAG, "could not connect media controller: " + e)
                            hidePlaybackControls()
                        }
                    }
                },
                null)
    }

    override fun onStart() {
        super.onStart()
        controlsFragment = supportFragmentManager.findFragmentById(R.id.fragment_playback_controls)
                as MediaPlaybackControlsFragment
        hidePlaybackControls()

        mMediaBrowser.connect()
    }

    override fun onStop() {
        super.onStop()
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mediaControllerCallback)

        mMediaBrowser.disconnect()
    }

    @Throws(RemoteException::class)
    private fun connectToSession(token: MediaSessionCompat.Token) {
        val mediaController = MediaControllerCompat(this, token)
        MediaControllerCompat.setMediaController(this, mediaController)
        mediaController.registerCallback(mediaControllerCallback)

        if (shouldShowControls()) {
            showPlaybackControls()
        } else {
            hidePlaybackControls()
        }
        controlsFragment.onConnected()

        onMediaControllerConnected()
    }

    open fun onMediaControllerConnected() {}

    override fun getMediaBrowser(): MediaBrowserCompat = mMediaBrowser

    private fun hidePlaybackControls() {
        supportFragmentManager.beginTransaction()
                .hide(controlsFragment)
                .commit()
    }

    private fun shouldShowControls(): Boolean {
        val mediaController = MediaControllerCompat.getMediaController(this)
        if (mediaController == null ||
                mediaController.metadata == null ||
                mediaController.playbackState == null) {
            return false
        }
        return when (mediaController.playbackState.state) {
            PlaybackStateCompat.STATE_ERROR, PlaybackStateCompat.STATE_NONE,
            PlaybackStateCompat.STATE_STOPPED -> false
            else -> true
        }
    }

    private fun showPlaybackControls() {
        supportFragmentManager.beginTransaction()
                /*.setCustomAnimations(
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)*/
                .show(controlsFragment)
                .commit()
    }

    // Callback that ensures that we are showing the controls
    private val mediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            if (shouldShowControls()) {
                showPlaybackControls()
            } else {
                hidePlaybackControls()
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (shouldShowControls()) {
                showPlaybackControls()
            } else {
                hidePlaybackControls()
            }
        }
    }
}