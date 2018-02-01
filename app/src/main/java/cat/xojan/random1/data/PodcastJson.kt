package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Audio
import cat.xojan.random1.domain.model.Podcast

data class PodcastJson(
        val appMobileTitle: String,
        val audio: Audio,
        val dateTime: String,
        val durationSeconds: Long,
        val mFilePath: String?,
        val mImageUrl: String?,
        val mProgramId: String?,
        val mState: Podcast.State,
        val path: String
)