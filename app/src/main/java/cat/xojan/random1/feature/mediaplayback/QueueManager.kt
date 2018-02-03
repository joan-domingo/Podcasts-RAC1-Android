package cat.xojan.random1.feature.mediaplayback

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DURATION

class QueueManager(val eventLogger: EventLogger) {

    companion object {
        val MEDIA_ID_PLAY_ALL = "play_all_podcasts_playlist"
        val METADATA_HAS_NEXT_OR_PREVIOUS = "android.media.metadata.NEXT_or_PREVIOUS"
        val METADATA_PROGRAM_ID = "android.media.metadata.PROGRAM_ID"
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

            if (mediaId == MEDIA_ID_PLAY_ALL) {
                eventLogger.logPlayAllPodcasts()
            } else {
                eventLogger.logPlaySinglePodcast()
            }

            val itemMediaData = item.description
            currentQueueId = item.queueId

            val downloadPath: String? = itemMediaData.extras?.getString(Podcast.PODCAST_FILE_PATH)
            val programId: String? = itemMediaData.extras?.getString(Podcast.PODCAST_PROGRAM_ID)
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
                    .putLong(METADATA_HAS_NEXT_OR_PREVIOUS, hasNextOrPrevious())
                    .putString(METADATA_PROGRAM_ID, programId)
                    .build()
        } else {
            null
        }
    }

    private fun hasNextOrPrevious(): Long {
        if (currentPlaylist.size == 1) {
            return 0
        }
        return 1
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