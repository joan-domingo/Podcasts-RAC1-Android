package cat.xojan.random1.domain.interactor

import android.app.DownloadManager
import android.content.Context
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.repository.DownloadPodcastRepository
import cat.xojan.random1.domain.repository.PodcastPreferencesRepository
import cat.xojan.random1.domain.repository.PodcastRepository
import cat.xojan.random1.domain.repository.ProgramRepository
import org.junit.Before
import org.mockito.Mockito.mock

class PodcastDataInteractorTest {

    private lateinit var programRepository: ProgramRepository
    private lateinit var podcastRepository: PodcastRepository
    private lateinit var podcastPreferencesRepository: PodcastPreferencesRepository
    private lateinit var downloadManager: DownloadManager
    private lateinit var context: Context
    private lateinit var downloadRepository: DownloadPodcastRepository
    private lateinit var eventLogger: EventLogger

    private lateinit var podcastDataInteractor: PodcastDataInteractor

    @Before
    fun setUp() {
        programRepository = mock(ProgramRepository::class.java)
        podcastRepository = mock(PodcastRepository::class.java)
        podcastPreferencesRepository = mock(PodcastPreferencesRepository::class.java)
        downloadManager = mock(DownloadManager::class.java)
        context = mock(Context::class.java)
        downloadRepository = mock(DownloadPodcastRepository::class.java)
        eventLogger = EventLogger(null)

        podcastDataInteractor = PodcastDataInteractor(
                programRepository,
                podcastRepository,
                podcastPreferencesRepository,
                downloadManager,
                context,
                downloadRepository,
                eventLogger)
    }
}