package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Program
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.*


class RemoteProgramRepositoryTest {

    private lateinit var remoteProgramRepository: RemoteProgramRepository
    private lateinit var rac1ApiService: Rac1ApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url("").toString())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        rac1ApiService = retrofit.create(Rac1ApiService::class.java)
        remoteProgramRepository = RemoteProgramRepository(rac1ApiService)
    }

    @Test
    fun get_program_list_success() {
        val body = IOUtils.toString(this.javaClass.classLoader.getResourceAsStream("programs.json"))
        mockWebServer.enqueue(MockResponse().setBody(body))
        val testSubscriber = TestObserver<List<Program>>()

        remoteProgramRepository.getPrograms().subscribe(testSubscriber)
        testSubscriber.assertValue(
                listOf(Program("el-mon"),
                        Program("la-competencia"),
                        Program("la-segona-hora")))
    }

    @Test
    fun get_program_list_fail() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val testSubscriber = TestObserver<List<Program>>()

        remoteProgramRepository.getPrograms().subscribe(testSubscriber)
        testSubscriber.assertErrorMessage("Client Error")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
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

