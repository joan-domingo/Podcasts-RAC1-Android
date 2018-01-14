package cat.xojan.random1.feature.mediaplayback

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import cat.xojan.random1.R
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DURATION
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class QueueManager(val context: Context) {

    var potentialPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    private var currentPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    private var currentAllPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    lateinit var listener: MetaDataUpdateListener
    private var currentQueueId: Long = -1

    fun initListener(mediaPlaybackService: MediaPlaybackService) {
        listener = mediaPlaybackService
    }

    fun getMediaItem(mediaId: String?): MediaMetadataCompat? {
        return if (mediaId != null) {
            val item = currentPlaylist.filter { it -> it.description.mediaId == mediaId }
            val itemMediaData = item[0].description
            currentQueueId = item[0].queueId

            val downloadPath: String? = itemMediaData.extras?.getString(Podcast.PODCAST_FILE_PATH)
            val mediaUri: String = downloadPath ?: itemMediaData.mediaUri.toString()

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, itemMediaData.mediaId)
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, itemMediaData.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, mediaUri)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                            itemMediaData.iconUri.toString())
                    .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, item[0].queueId + 1)
                    .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
                            currentPlaylist.size.toLong())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                            itemMediaData.extras?.getLong(PODCAST_DURATION)!! * 1000)
                    .build()
        } else {
            null
        }
    }

    fun setQueue(mediaId: String?) {
        mediaId?.let {
            setCurrentQueue()
            updateMetadata(mediaId)
        }
    }

    private fun setCurrentQueue() {
        currentPlaylist =  potentialPlaylist
        currentAllPlaylist =  potentialPlaylist
        listener.updateQueue("title queue", potentialPlaylist)
    }

    fun updateMetadata(mediaId: String?) {
        var metadata = getMediaItem(mediaId)
        listener.updateMetadata(metadata)

        // Set the proper album artwork on the media session, so it can be shown in the
        // locked screen and in other places.
        val iconUri = metadata?.description?.iconUri
        iconUri?.let {
            Picasso.with(context)
                    .load(iconUri)
                    .placeholder(R.drawable.default_rac1)
                    .into(object: Target {
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable?) {
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            metadata = MediaMetadataCompat.Builder(metadata)
                                    // set high resolution bitmap in METADATA_KEY_ALBUM_ART. This is used, for
                                    // example, on the lockscreen background when the media session is active.
                                    .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                                    // set small version of the album art in the DISPLAY_ICON. This is used on
                                    // the MediaDescription and thus it should be small to be serialized if
                                    // necessary
                                    .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
                                    .build()
                            listener.updateMetadata(metadata)
                        }
                    })
        }
    }

    fun getNextMediaId(): String? {
        if (currentPlaylist.size == 1) {
            return null
        }
        if ((currentQueueId + 1) < currentPlaylist.size.toLong()) {
            return currentPlaylist[(currentQueueId + 1).toInt()].description.mediaId
        }
        return null
    }

    fun getPreviousMediaId(): String? {
        if (currentPlaylist.size == 1) {
            return null
        }
        if ((currentQueueId - 1) >= 0) {
            return currentPlaylist[(currentQueueId - 1).toInt()].description.mediaId
        }
        return null
    }

    fun getCurrentMediaId(): Long {
        return currentQueueId
    }

    fun setPlaylistMode(playListMode: Int) {
        when(playListMode) {
            PlaybackStateCompat.SHUFFLE_MODE_ALL -> currentPlaylist = currentAllPlaylist
            PlaybackStateCompat.SHUFFLE_MODE_GROUP -> currentPlaylist =
                    listOf(currentPlaylist[currentQueueId.toInt()])
        }
    }
}