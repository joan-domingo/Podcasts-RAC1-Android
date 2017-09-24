package cat.xojan.random1.domain.entities

import com.squareup.moshi.Json


class PodcastData {

    @Json(name = "result")
    var podcasts: List<Podcast> = listOf()
}