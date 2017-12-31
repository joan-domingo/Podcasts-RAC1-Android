package cat.xojan.random1.data


import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.ProgramData
import cat.xojan.random1.testutil.programList
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RemoteProgramRepositoryTest {

    private lateinit var remoteProgramRepository: RemoteProgramRepository
    private lateinit var rac1ApiService: Rac1ApiService

    private val programData: ProgramData
        get() {
            val programData = ProgramData()
            programData.programs = programList
            return programData
        }

    @Before
    fun setUp() {
        rac1ApiService = mock(Rac1ApiService::class.java)
        remoteProgramRepository = RemoteProgramRepository(rac1ApiService)
    }

    @Test
    fun get_program_list_success() {
        `when`<List<Program>>(rac1ApiService.getProgramData().execute().body()?.programs!!)
                .thenReturn(programList)
        assertEquals(remoteProgramRepository.getPrograms(), programList)
    }

    /*@Test
    @Throws(IOException::class)
    fun get_podcasts_list_by_program() {
        `when`<Call<PodcastData>>(mService!!.getPodcastData(anyString())).thenReturn(Single.just<T>(podcastData))
        val testSubscriber = TestSubscriber<List<Podcast>>()

        mRemoteRepository!!.getPodcast("programId", null).subscribe(testSubscriber)
        testSubscriber.assertValue(podcastList)
    }

    @Test
    @Throws(IOException::class)
    fun get_podcasts_list_by_section() {
        `when`<Call<PodcastData>>(mService!!.getPodcastData(anyString(), anyString())).thenReturn(Flowable.just<T>(podcastData))
        val testSubscriber = TestSubscriber<List<Podcast>>()

        mRemoteRepository!!.getPodcast("programId", "sectionId").subscribe(testSubscriber)
        testSubscriber.assertValue(podcastList)
    }*/
}

