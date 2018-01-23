package cat.xojan.random1.data

import cat.xojan.random1.domain.model.Audio
import cat.xojan.random1.domain.model.Podcast
import java.util.*

class PodcastJson(
        val appMobileTitle: String,
        val audio: Audio,
        val dateTime: Date?,
        val durationSeconds: Long,
        val mFilePath: String?,
        val mImageUrl: String?,
        val mProgramId: String?,
        val mState: Podcast.State,
        val path: String
)