package cat.xojan.random1.domain.model

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


class EventLogger(context: Context) {

    val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logDownloadedPodcastSuccess(audioId: String, title: String, programTitle: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, audioId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title)
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, programTitle)
        firebaseAnalytics.logEvent("podcast_download_success", bundle)
    }

    /*fun logPlayedPodcast(podcast: Podcast) {
    }

    fun logExportedPodcast(podcastTitle: String) {
    }

    fun logExportedPodcastAction() {
    }*/
}