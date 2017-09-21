package cat.xojan.random1.testutil

import cat.xojan.random1.domain.entities.Audio
import cat.xojan.random1.domain.entities.Podcast
import java.util.*

val podcast1 = Podcast(Audio(), "path1", "filePath1", Date(), 0, "programId1", null, Podcast.State.DOWNLOADED, "programTitle1", 1)
val podcast2 = Podcast(Audio(), "path1", null, Date(), 0, "programId1", null, null, "programTitle1", 1)
val podcast3 = Podcast(Audio(), "path1", "filePath3", Date(), 0, "programId1", null, Podcast.State.DOWNLOADING, "programTitle1", 1)

val podcastList = listOf(podcast1, podcast2, podcast3)