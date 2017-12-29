package cat.xojan.random1.domain.repository

import cat.xojan.random1.domain.model.Podcast
import io.reactivex.Single
import java.io.IOException

interface PodcastRepository {
    @Throws(IOException::class)
    fun getPodcasts(programId: String, sectionId: String?, refresh: Boolean):
            Single<List<Podcast>>
}