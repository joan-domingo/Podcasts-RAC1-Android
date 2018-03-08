package cat.xojan.random1.domain.interactor

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.text.TextUtils
import android.util.Log
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DATE
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_DOWNLOAD_REFERENCE
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_FILE_PATH
import cat.xojan.random1.domain.model.Podcast.Companion.PODCAST_STATE
import cat.xojan.random1.domain.model.PodcastState
import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.repository.DownloadPodcastRepository
import cat.xojan.random1.domain.repository.PodcastPreferencesRepository
import cat.xojan.random1.domain.repository.PodcastRepository
import cat.xojan.random1.domain.repository.ProgramRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class PodcastDataInteractor @Inject constructor(
        private val programRepo: ProgramRepository,
        private val podcastRepo: PodcastRepository,
        private val podcastPref: PodcastPreferencesRepository,
        private val downloadManager: DownloadManager,
        private val context: Context,
        private val downloadRepo: DownloadPodcastRepository,
        private val eventLogger: EventLogger)
{

    companion object {
        const val EXTENSION = ".mp3"
    }
    private val TAG = PodcastDataInteractor::class.simpleName

    private val stateUpdatesSubject: PublishSubject<List<MediaBrowserCompat.MediaItem>> =
            PublishSubject.create()

    fun getHourByHourPodcasts(programId: String, refresh: Boolean): Single<List<Podcast>> {
        val program = programRepo.getProgram(programId)
        val podcastList = podcastRepo.getPodcasts(programId, null, refresh)

        return Single.zip(program, podcastList,
                BiFunction<Program, List<Podcast>, List<Podcast>> { pr, podList ->
            for (p in podList) {
                p.programId = pr.id
                p.smallImageUrl = pr.smallImageUrl
                p.bigImageUrl = pr.bigImageUrl
            }
                    podList
        })
    }

    fun getSectionPodcasts(programId: String, sectionId: String, refresh: Boolean):
            Single<List<Podcast>> {
        val program = programRepo.getProgram(programId)
        val podcastList = podcastRepo.getPodcasts(programId, sectionId, refresh)

        return Single.zip(program, podcastList,
                BiFunction<Program, List<Podcast>, List<Podcast>> { pr, podList ->
                    for (p in podList) {
                        p.programId = pr.id
                        p.smallImageUrl = pr.smallImageUrl
                        p.bigImageUrl = pr.bigImageUrl
                    }
                    podList
                })
    }

    fun isSectionSelected(): Boolean {
        return podcastPref.isSectionSelected()
    }

    fun setSectionSelected(selected: Boolean) {
        podcastPref.setSectionSelected(selected)
    }

    fun download(podcast: MediaDescriptionCompat) {
        val uri = podcast.mediaUri
        val request = DownloadManager.Request(uri)
                .setTitle(podcast.title)
                .setDestinationInExternalFilesDir(
                        context,
                        Environment.DIRECTORY_DOWNLOADS,
                        podcast.mediaId + EXTENSION)
                .setVisibleInDownloadsUi(true)

        val reference = downloadManager.enqueue(request)
        podcast.extras?.putLong(PODCAST_DOWNLOAD_REFERENCE, reference)
        downloadRepo.addDownloadingPodcast(podcast)
    }

    fun deleteDownload(podcast: MediaDescriptionCompat) {
        val file = File(podcast.extras?.getString(PODCAST_FILE_PATH))
        if (file.delete()) {
            downloadRepo.deleteDownloadedPodcast(podcast)
        }
    }

    fun getDownloadedPodcasts(): Single<List<MediaBrowserCompat.MediaItem>> {
        return Single.just(fetchDownloadedPodcasts())
                .flatMap {
                    podcasts -> Observable.just(podcasts)
                        .flatMapIterable { p -> p }
                        .filter { p -> p.description.extras?.getSerializable(PODCAST_STATE) ==
                                PodcastState.DOWNLOADED }
                        .sorted { p1, p2 -> (p2.description.extras?.getSerializable(PODCAST_DATE)
                                as Date).compareTo(p1.description.extras?.getSerializable
                            (PODCAST_DATE) as Date) }
                        .toList()
                }
    }

    fun getPodcastStateUpdates(): PublishSubject<List<MediaBrowserCompat.MediaItem>> {
        return stateUpdatesSubject
    }

    fun refreshDownloadedPodcasts() {
        stateUpdatesSubject.onNext(fetchDownloadedPodcasts())
    }

    fun fetchDownloadedPodcasts(): List<MediaBrowserCompat.MediaItem> {
        val podcastList = HashSet<MediaDescriptionCompat>()
        val downloading = downloadRepo.getDownloadingPodcasts()
        val downloaded = downloadRepo.getDownloadedPodcasts()
        podcastList.addAll(downloading)
        podcastList.addAll(downloaded)

        Log.d(TAG, "Downloading: " + downloading.size + ", downloaded: " + downloaded.size)

        return podcastList.map { description ->
            MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE) }
    }

    fun deleteDownloading(reference: Long) {
        val podcast: MediaDescriptionCompat? = downloadRepo.getDownloadingPodcasts()
                .lastOrNull {
                    reference == it.extras?.getLong(PODCAST_DOWNLOAD_REFERENCE)
                }
        podcast?.let {
            downloadRepo.deleteDownloadingPodcast(podcast)
        }
    }

    fun addDownload(audioId: String) {
        val from = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() +
                File.separator + audioId + EXTENSION)
        val to = File(context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).toString() +
                File.separator + audioId + EXTENSION)

        if (from.renameTo(to)) {
            Log.d(TAG, "moving download from " + from.path + " to " + to.path)
            downloadRepo.setPodcastAsDownloaded(audioId, to.path)
        } else {
            from.delete()
            to.delete()
        }
    }

    fun exportPodcasts(): Single<Unit> {
        eventLogger.logExportPodcastsAction()
        return Single.create { subscriber ->
            try {
                val iternalFileDir = context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)
                val externalFilesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PODCASTS)

                externalFilesDir.mkdirs()

                for (podcastFile in iternalFileDir!!.listFiles()) {
                    val audioId = podcastFile.path
                            .split((Environment.DIRECTORY_PODCASTS + "/").toRegex())
                            .dropLastWhile({ it.isEmpty() })
                            .toTypedArray()[1].replace(".mp3", "")
                    var podcastTitle = getDownloadedPodcastTitle(audioId)

                    if (!TextUtils.isEmpty(podcastTitle)) {
                        podcastTitle = podcastTitle!!.replace("/", "-")
                        val dest = File(externalFilesDir, "$podcastTitle.mp3")
                        copy(podcastFile, dest)
                    }
                    eventLogger.logExportedPodcast(audioId, podcastTitle)
                }
                subscriber.onSuccess(Unit)
                eventLogger.logExportedPodcastsSuccess()
            } catch (e: Throwable) {
                subscriber.onError(e)
                eventLogger.logExportedPodcastsFail()
            }
        }
    }

    private fun getDownloadedPodcastTitle(audioId: String): String? {
        return downloadRepo.getDownloadedPodcastTitle(audioId)
    }

    private fun copy(src: File, dst: File) {
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(dst)
        val inChannel = inStream.channel
        val outChannel = outStream.channel
        inChannel.transferTo(0, inChannel.size(), outChannel)
        inStream.close()
        outStream.close()
    }
}