package cat.xojan.random1.feature.browser

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import cat.xojan.random1.domain.interactor.PodcastDataInteractor
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.PodcastState
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BrowserViewModel @Inject constructor(
        private val podcastInteractor: PodcastDataInteractor,
        private val programInteractor: ProgramDataInteractor,
        private val eventLogger: EventLogger) {

    fun getPodcastStateUpdates(): PublishSubject<List<MediaBrowserCompat.MediaItem>> {
         return podcastInteractor.getPodcastStateUpdates()
    }

    fun selectedSection(b: Boolean) = podcastInteractor.setSectionSelected(b)

    fun isSectionSelected(): Boolean = podcastInteractor.isSectionSelected()

    fun hasSections(programId: String?): Single<Boolean> {
        programId?.let {
            return programInteractor.hasSections(programId)
        }
        return Single.just(false)
    }

    fun downloadPodcast(podcast: MediaDescriptionCompat) {
        podcastInteractor.download(podcast)
        eventLogger.logDownloadPodcastTry(
                podcast.mediaId,
                podcast.title.toString(),
                podcast.extras?.getString(Podcast.PODCAST_PROGRAM_ID))
    }

    fun deletePodcast(podcast: MediaDescriptionCompat) {
        podcastInteractor.deleteDownload(podcast)
    }

    fun refreshDownloadedPodcast() {
        podcastInteractor.refreshDownloadedPodcasts()
    }

    fun updatePodcastState(loaded: List<MediaBrowserCompat.MediaItem>):
            List<MediaBrowserCompat.MediaItem> {
        val updated = podcastInteractor.fetchDownloadedPodcasts()

        for (mediaItem in loaded) {
            val podcast = mediaItem.description
            podcast.extras?.putString(Podcast.PODCAST_FILE_PATH, null)
            podcast.extras?.putSerializable(Podcast.PODCAST_STATE, PodcastState.LOADED)
        }

        for (mediaItem in updated) {
            val updatedPodcast = mediaItem.description
            val currentPodcast = loaded.firstOrNull { it.mediaId == mediaItem.mediaId }
            currentPodcast?.let {
                val description = currentPodcast.description
                description.extras?.putString(Podcast.PODCAST_FILE_PATH,
                        updatedPodcast.extras?.getString(Podcast.PODCAST_FILE_PATH))
                description.extras?.putSerializable(Podcast.PODCAST_STATE,
                        updatedPodcast.extras?.getSerializable(Podcast.PODCAST_STATE)
                                as PodcastState)
            }
        }
        return loaded
    }
}
