package cat.xojan.random1.testutil

import cat.xojan.random1.domain.entities.Audio
import cat.xojan.random1.domain.entities.Images
import cat.xojan.random1.domain.entities.Podcast
import cat.xojan.random1.domain.entities.Program
import java.util.*

val podcast1 = Podcast(Audio(), "path1", "filePath1", Date(), 0, "podcastId1", null, Podcast.State.DOWNLOADED, "programTitle1", 1)
val podcast2 = Podcast(Audio(), "path1", null, Date(), 0, "podcastId2", null, null, "programTitle1", 1)
val podcast3 = Podcast(Audio(), "path1", "filePath3", Date(), 0, "podcastId3", null, Podcast.State.DOWNLOADING, "programTitle1", 1)
val podcastList = listOf(podcast1, podcast2, podcast3)

val program1 = Program("programId1", "programTitle1", listOf(), Images("http://image.url"), true)
val program2 = Program("programId2", "programTitle2", listOf(), Images("http://image.url"),
        false)
val program3 = Program("programId3", "programTitle3", listOf(), Images("http://image.url"),
        true)
val programList = listOf(program1, program2, program3)
val programListResult1 = listOf(program1, program3)