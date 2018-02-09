package cat.xojan.random1.domain.model

import java.util.*

class MediaDescriptionCompatJson(
        val id: String?,
        val title: String,
        val mediaUrl: String?,
        val iconUrl: String?,
        val state: PodcastState,
        val downloadReference: Long?,
        val mediaFilePath: String?,
        val programId: String?,
        val bigImageUrl: String?,
        val duration: Long?,
        val date: Date?
)