package cat.xojan.random1.feature.mediaplayback

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DURATION

class QueueManager {

    companion object {
        val MEDIA_ID_PLAY_ALL = "play_all_podcasts_playlist"
    }

    var potentialPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    private var currentPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    private var currentAllPlaylist: List<MediaSessionCompat.QueueItem> = listOf()
    private lateinit var listener: MetaDataUpdateListener
    private var currentQueueId: Long = -1

    fun initListener(mediaPlaybackService: MediaPlaybackService) {
        listener = mediaPlaybackService
    }

    fun getMediaItem(mediaId: String?): MediaMetadataCompat? {
        return if (mediaId != null) {
            val item = if (mediaId == MEDIA_ID_PLAY_ALL) {
                currentPlaylist[0]
            } else {
                currentPlaylist.filter { it -> it.description.mediaId == mediaId }[0]
            }

            val itemMediaData = item.description
            currentQueueId = item.queueId

            val downloadPath: String? = itemMediaData.extras?.getString(Podcast.PODCAST_FILE_PATH)
            val mediaUri: String = downloadPath ?: itemMediaData.mediaUri.toString()

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, itemMediaData.mediaId)
                    .putText(MediaMetadataCompat.METADATA_KEY_TITLE, itemMediaData.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, mediaUri)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                            itemMediaData.iconUri.toString())
                    .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, item.queueId + 1)
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
            setCurrentQueue(mediaId)
            updateMetadata(mediaId)
        }
    }

    private fun setCurrentQueue(mediaId: String?) {
        currentPlaylist = if (mediaId != MEDIA_ID_PLAY_ALL) {
            listOf(potentialPlaylist.filter { it -> it.description.mediaId == mediaId }[0])
        } else {
            potentialPlaylist
        }
        currentAllPlaylist =  potentialPlaylist
        listener.updateQueue("Play Queue", currentPlaylist)
    }

    fun updateMetadata(mediaId: String?) {
        val metadata = getMediaItem(mediaId)
        listener.updateMetadata(metadata)
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
}