package cat.xojan.random1.domain.interactor

import android.app.DownloadManager
import android.content.Context
import cat.xojan.random1.data.SharedPrefDownloadPodcastRepository
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.Section
import cat.xojan.random1.domain.repository.ProgramRepository
import cat.xojan.random1.testutil.program1
import cat.xojan.random1.testutil.programList
import cat.xojan.random1.testutil.sectionList
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.io.IOException

class ProgramDataInteractorTest {

    private lateinit var mProgramRepo: ProgramRepository
    private lateinit var mDownloadsRepo: SharedPrefDownloadPodcastRepository
    private lateinit var mProgramDataInteractor: ProgramDataInteractor
    private lateinit var mMockContext: Context
    private lateinit var mDownloadManager: DownloadManager
    private lateinit var mEventLogger: EventLogger

    /*private val dummyProgram: Program
        get() {
            val program = program1
            program.sections = sectionList
            return program
        }

    @Before
    fun setUp() {
        mProgramRepo = mock(ProgramRepository::class.java)
        mDownloadsRepo = mock(SharedPrefDownloadPodcastRepository::class.java)
        mMockContext = mock(Context::class.java)
        mDownloadManager = mock(DownloadManager::class.java)
        mEventLogger = mock(EventLogger::class.java)

        mProgramDataInteractor = ProgramDataInteractor(mProgramRepo, mDownloadsRepo,
                mMockContext, mEventLogger)
    }

    @Test
    @Ignore
    @Throws(IOException::class)
    fun load_programs_successfully_during_first_call() {
        `when`(mProgramRepo.getPrograms()).thenReturn(Single.just(programList))
        val testSubscriber = TestObserver<List<Program>>()
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber)
        testSubscriber.assertValue(programList)
    }*/

    /*@Test
    fun load_programs_successfully_after_first_call() {
        mProgramDataInteractor.programs = programList
        val testSubscriber = TestObserver<List<Program>>()
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber)
        testSubscriber.assertValue(programList)
    }*/

    @Test
    @Ignore
    @Throws(IOException::class)
    fun fail_to_load_programs() {
        `when`(mProgramRepo.getPrograms()).thenThrow(IOException())
        val testSubscriber = TestObserver<List<Program>>()
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber)
        testSubscriber.assertError(IOException::class.java)
    }

    @Test
    @Ignore
    fun get_sections_from_program() {
        val testSubscriber = TestObserver<List<Section>>()
        mProgramDataInteractor.loadSections("programId").subscribe(testSubscriber)
        testSubscriber.assertValue(sectionList)
    }

    /*@Test
    @Ignore
    @Throws(IOException::class)
    fun load_podcasts_by_program_successfully() {
        val program = dummyProgram
        `when`(mProgramRepo.getPodcast(anyString(), null)).thenReturn(Flowable.just(podcastList))
        val testSubscriber = TestSubscriber<List<Podcast>>()
        mProgramDataInteractor.loadPodcasts(program, null, false).subscribe(testSubscriber)
        testSubscriber.assertValue(podcastList)
    }

    @Test
    @Throws(IOException::class)
    fun load_podcasts_by_section_successfully() {
        val program = dummyProgram
        val section = sectionList[0]
        `when`(mProgramRepo.getPodcast(anyString(), anyString())).thenReturn(Flowable.just(podcastList))
        val testSubscriber = TestSubscriber<List<Podcast>>()
        mProgramDataInteractor.loadPodcasts(program, section, false).subscribe(testSubscriber)
        testSubscriber.assertValue(podcastList)
    }

    @Test
    @Ignore
    @Throws(IOException::class)
    fun fail_to_load_podcasts() {
        val program = dummyProgram
        `when`(mProgramRepo.getPodcast(anyString(), null)).thenThrow(IOException())
        val testSubscriber = TestSubscriber<List<Podcast>>()
        mProgramDataInteractor.loadPodcasts(program, null, false).subscribe(testSubscriber)
        testSubscriber.assertError(IOException::class.java)
    }

    @Test
    fun add_downloaded_podcast_and_refresh_list_fail() {
        val testSubscriber = TestObserver<List<Podcast>>()
        mProgramDataInteractor.getDownloadedPodcasts().subscribe(testSubscriber)

        `when`(mMockContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).thenReturn(File("downloads/"))
        `when`(mMockContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)).thenReturn(File("podcasts/"))
        mProgramDataInteractor.addDownload("audioId1")

        testSubscriber.assertValue(ArrayList())
    }*/
}
