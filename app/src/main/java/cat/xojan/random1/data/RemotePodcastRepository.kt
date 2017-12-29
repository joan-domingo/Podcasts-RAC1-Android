package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.repository.PodcastRepository
import io.reactivex.Single
import java.io.IOException

class RemotePodcastRepository(private val service: Rac1ApiService): PodcastRepository {

    var hourPodcasts = listOf<Podcast>()
    var sectionPodcasts = listOf<Podcast>()

    @Throws(IOException::class)
    override fun getPodcasts(programId: String, sectionId: String?): Single<List<Podcast>> {
        return Single.create { subscriber ->
            sectionId?.let {
                try {
                    if (sectionPodcasts.isEmpty()) {
                        sectionPodcasts = service.getPodcastData(programId, sectionId).execute()
                                .body()!!.podcasts
                    }
                    subscriber.onSuccess(sectionPodcasts)
                } catch (e: IOException) {
                    subscriber.onError(e)
                }
            }

            try {
                if (hourPodcasts.isEmpty()) {
                    hourPodcasts = service.getPodcastData(programId).execute()
                            .body()!!.podcasts
                }
                subscriber.onSuccess(hourPodcasts)
            } catch (e: IOException) {
                subscriber.onError(e)
            }
        }
    }
}