package cat.xojan.random1.data

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class RemotePodcastRepositoryTest {

    private lateinit var remotePodcastRepository: RemotePodcastRepository
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
        remotePodcastRepository = RemotePodcastRepository(rac1ApiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}