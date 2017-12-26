package cat.xojan.random1.feature.mediaplayback

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_BIG_IMAGE_URL
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DURATION

class QueueManager {

    var items: List<MediaSessionCompat.QueueItem> = listOf()
    lateinit var listener: MetaDataUpdateListener
    private var currentQueueId: Long = -1

    fun initListener(mediaPlaybackService: MediaPlaybackService) {
        listener = mediaPlaybackService
    }

    fun getMediaItem(mediaId: String?): MediaMetadataCompat? {
        return if (mediaId != null) {
            val item = items.filter { it -> it.description.mediaId == mediaId }
            val itemMediaData = item[0].description
            currentQueueId = item[0].queueId

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, itemMediaData.mediaId)
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, itemMediaData.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                            itemMediaData.mediaUri.toString())
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                            itemMediaData.extras?.getString(PODCAST_BIG_IMAGE_URL))
                    .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, item[0].queueId + 1)
                    .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, items.size.toLong())
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                            itemMediaData.extras?.getLong(PODCAST_DURATION)!!)
                    .build()
        } else {
            null
        }
    }

    fun setQueue(mediaId: String?) {
        // TODO reuse queue or different queue
        setCurrentQueue()
        updateMetadata(mediaId)
    }

    private fun setCurrentQueue() {
        val newQueue = items
        listener.updateQueue("title queue", newQueue)
    }

    fun updateMetadata(mediaId: String?) {
        listener.updateMetadata(getMediaItem(mediaId))
        // TODO: Set the proper album artwork on the media session,
        // so it can be shown in the
        // locked screen and in other places.
    }

    fun getNextMediaId(): String? {
        val nextQueueId:Int = ((currentQueueId + 1) % items.size).toInt()
        return items[nextQueueId].description.mediaId
    }

    fun getPreviousMediaId(): String? {
        val previousQueueId:Int = ((currentQueueId + -1) % items.size).toInt()
        return items[previousQueueId].description.mediaId
    }

    fun getCurrentMediaId(): Long {
        return currentQueueId
    }
}