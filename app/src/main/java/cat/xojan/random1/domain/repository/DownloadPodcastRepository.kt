package cat.xojan.random1.domain.repository

import cat.xojan.random1.domain.entities.Podcast

interface DownloadPodcastRepository {

    fun addDownloadingPodcast(podcast: Podcast): Boolean

    fun deleteDownloadingPodcast(podcast: Podcast): Boolean

    fun setPodcastAsDownloaded(audioId: String, filePath: String)

    fun getDownloadingPodcasts(): MutableSet<Podcast>

    fun getDownloadedPodcasts(): Set<Podcast>

    fun deleteDownloadedPodcast(podcast: Podcast)

    fun getDownloadedPodcastTitle(audioId: String): String
}