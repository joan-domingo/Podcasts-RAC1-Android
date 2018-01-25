package cat.xojan.random1.domain.model

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


class EventLogger(context: Context) {

    val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logDownloadPodcastTry(audioId: String?, title: String, programId: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, audioId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title)
        bundle.putString("item_category_id", programId)
        firebaseAnalytics.logEvent("podcast_download_try", bundle)
    }

    fun logDownloadedPodcastSuccess(audioId: String, title: String, programTitle: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, audioId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title)
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, programTitle)
        firebaseAnalytics.logEvent("podcast_download_success", bundle)
    }

    fun logDownloadedPodcastFail(reason: Int, reasonText: String?) {
        val bundle = Bundle()
        bundle.putString("error_id", reason.toString())
        bundle.putString("error_msg", reasonText)
        firebaseAnalytics.logEvent("podcast_download_fail", bundle)
    }

    fun logDownloadedPodcastCancel() {
        firebaseAnalytics.logEvent("podcast_download_cancel", null)
    }

    /*fun logPlayedPodcast(podcast: Podcast) {
    }

    fun logExportedPodcast(podcastTitle: String) {
    }

    fun logExportedPodcastAction() {
    }*/
}