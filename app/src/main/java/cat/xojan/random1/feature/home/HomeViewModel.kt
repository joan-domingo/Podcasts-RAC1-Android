package cat.xojan.random1.feature.home

import cat.xojan.random1.domain.interactor.PodcastDataInteractor
import io.reactivex.Single
import javax.inject.Inject

class HomeViewModel @Inject
constructor(private val podcastInteractor: PodcastDataInteractor) {

    fun exportPodcasts(): Single<Unit> {
        return podcastInteractor.exportPodcasts()
    }
}