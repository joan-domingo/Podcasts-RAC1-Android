package cat.xojan.random1.data

import android.content.Context
import android.content.SharedPreferences
import cat.xojan.random1.domain.entities.Podcast
import cat.xojan.random1.domain.repository.DownloadPodcastRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.util.HashSet

class PreferencesDownloadPodcastRepository(context: Context) : DownloadPodcastRepository {

    private val DOWNLOAD_PODCASTS = "dowload_podcasts_repo"
    private val DOWNLOADING_PODCASTS = "downloading_podcasts"
    private val DOWNLOADED_PODCASTS = "downloaded_podcasts"

    private val sharedPref: SharedPreferences
    private val jsonAdapter: JsonAdapter<MutableSet<Podcast>>

    init {
        sharedPref = context.getSharedPreferences(DOWNLOAD_PODCASTS, Context.MODE_PRIVATE)
        val type = Types.newParameterizedType(MutableSet::class.java, Podcast::class.java)
        jsonAdapter = Moshi.Builder().build().adapter(type)
    }

    override fun addDownloadingPodcast(podcast: Podcast): Boolean {
        podcast.state = Podcast.State.DOWNLOADING
        val podcasts = getDownloadingPodcasts()
        podcasts.add(podcast)
        return sharedPref.edit()
                .putString(DOWNLOADING_PODCASTS, setToJson(podcasts))
                .commit()
    }

    override fun deleteDownloadingPodcast(podcast: Podcast): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPodcastAsDownloaded(audioId: String, filePath: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDownloadingPodcasts(): MutableSet<Podcast> {
        val podcastsJson = sharedPref.getString(DOWNLOADING_PODCASTS, "")
        return jsonToSet(podcastsJson)
    }

    override fun getDownloadedPodcasts(): MutableSet<Podcast> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteDownloadedPodcast(podcast: Podcast) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDownloadedPodcastTitle(audioId: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun jsonToSet(json: String): MutableSet<Podcast> {
        val podcasts: MutableSet<Podcast>? = jsonAdapter.fromJson(json)
        return podcasts ?: mutableSetOf()
    }

    private fun setToJson(podcasts: MutableSet<Podcast>): String {
        return jsonAdapter.toJson(podcasts)
    }
}