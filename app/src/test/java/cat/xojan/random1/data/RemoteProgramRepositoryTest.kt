package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Program
import cat.xojan.random1.domain.model.Section
import cat.xojan.random1.testutil.program2
import cat.xojan.random1.testutil.programsMap
import cat.xojan.random1.testutil.sectionList
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.apache.commons.io.IOUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.InvalidKeyException
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

    @Test
    fun get_program() {
        remoteProgramRepository.programs = programsMap

        val program = remoteProgramRepository.getProgram("programId2")
        assertThat(program, equalTo(program2))
    }

    @Test
    fun has_sections() {
        remoteProgramRepository.programs = programsMap

        assertThat(remoteProgramRepository.hasSections("programId1"),
                equalTo(true))
    }

    @Test
    fun has_no_sections() {
        remoteProgramRepository.programs = programsMap

        assertThat(remoteProgramRepository.hasSections("programId2"),
                equalTo(false))
    }

    @Test
    fun get_section_list_success() {
        remoteProgramRepository.programs = programsMap
        val testSubscriber = TestObserver<List<Section>>()

        remoteProgramRepository.getSections("programId1").subscribe(testSubscriber)
        testSubscriber.assertValue(sectionList)
    }

    @Test
    fun get_section_list_fail() {
        remoteProgramRepository.programs = programsMap
        val testSubscriber = TestObserver<List<Section>>()

        remoteProgramRepository.getSections("programId104").subscribe(testSubscriber)
        testSubscriber.assertError(InvalidKeyException::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}

