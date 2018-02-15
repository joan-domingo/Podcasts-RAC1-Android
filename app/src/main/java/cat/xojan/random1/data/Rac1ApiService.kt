package cat.xojan.random1.data

import cat.xojan.random1.domain.model.PodcastData
import cat.xojan.random1.domain.model.ProgramData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Rac1ApiService {
    @GET("v1/programs")
    fun getProgramData(): Single<ProgramData>

    @GET("v1/sessions/{programId}")
    fun getPodcastData(@Path("programId") programId: String): Single<PodcastData>

    @GET("v1/sessions/{programId}/{sectionId}")
    fun getPodcastData(@Path("programId") programId: String,
                       @Path("sectionId") sectionId: String): Single<PodcastData>
}