package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Podcast
import cat.xojan.random1.domain.repository.PodcastRepository
import io.reactivex.Single

class RemotePodcastRepository(private val service: Rac1ApiService): PodcastRepository {

    private var hourPodcasts = listOf<Podcast>()
    private var sectionPodcasts = listOf<Podcast>()
    private var programId:String? = null
    private var sectionId:String? = null

    override fun getPodcasts(programId: String, sectionId: String?, refresh:Boolean):
            Single<List<Podcast>> {
        return Single.create { subscriber ->
            try {
                if (sectionId != null) {
                    if (sectionPodcasts.isEmpty() || refresh || programId != this.programId ||
                            sectionId != this.sectionId) {
                        val response = service.getPodcastData(programId, sectionId).execute()
                        if (response.isSuccessful) {
                            sectionPodcasts = response.body()!!.podcasts
                        } else {
                            subscriber.onError(Throwable(response.message()))
                        }
                    }
                    subscriber.onSuccess(sectionPodcasts)
                } else {
                    if (hourPodcasts.isEmpty() || refresh || programId != this.programId) {
                        val response = service.getPodcastData(programId).execute()
                        if (response.isSuccessful) {
                            hourPodcasts = response.body()!!.podcasts
                        } else {
                            subscriber.onError(Throwable(response.message()))
                        }
                    }
                    subscriber.onSuccess(hourPodcasts)
                }

                this.programId = programId
                this.sectionId = sectionId
            } catch (e: Throwable) {
                subscriber.onError(e)
            }
        }
    }
}