package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.repository.PodcastRepository
import io.reactivex.Single
import java.io.IOException

class RemotePodcastRepository(private val service: Rac1ApiService): PodcastRepository {

    private var hourPodcasts = listOf<Podcast>()
    private var sectionPodcasts = listOf<Podcast>()
    private var programId:String? = null
    private var sectionId:String? = null

    @Throws(IOException::class)
    override fun getPodcasts(programId: String, sectionId: String?, refresh:Boolean):
            Single<List<Podcast>> {
        return Single.create { subscriber ->
            sectionId?.let {
                try {
                    if (sectionPodcasts.isEmpty() || refresh || programId != this.programId ||
                            sectionId != this.sectionId) {
                        sectionPodcasts = service.getPodcastData(programId, sectionId).execute()
                                .body()!!.podcasts
                    }
                    subscriber.onSuccess(sectionPodcasts)
                } catch (e: IOException) {
                    subscriber.onError(e)
                }
            }

            try {
                if (hourPodcasts.isEmpty() || refresh || programId != this.programId) {
                    hourPodcasts = service.getPodcastData(programId).execute()
                            .body()!!.podcasts
                }
                subscriber.onSuccess(hourPodcasts)
            } catch (e: IOException) {
                subscriber.onError(e)
            }

            this.programId = programId
            this.sectionId = sectionId
        }
    }
}