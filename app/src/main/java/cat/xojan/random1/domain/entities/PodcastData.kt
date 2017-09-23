package cat.xojan.random1.domain.entities

import com.google.gson.annotations.SerializedName


class PodcastData {

    @SerializedName("result")
    var podcasts: List<Podcast> = listOf()
}